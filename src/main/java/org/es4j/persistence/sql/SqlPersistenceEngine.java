package org.es4j.persistence.sql;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.es4j.dotnet.GC;
import org.es4j.dotnet.IEnumerable;
import org.es4j.dotnet.data.ConnectionFactory;
import org.es4j.dotnet.data.IDataRecord;
import org.es4j.dotnet.data.IDbCommand;
import org.es4j.dotnet.data.IDbConnection;
import org.es4j.dotnet.data.IDbTransaction;
import org.es4j.dotnet.data.TransactionScope;
import org.es4j.dotnet.data.TransactionScopeOption;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.ConcurrencyException;
import org.es4j.eventstore.api.DuplicateCommitException;
import org.es4j.eventstore.api.Snapshot;
import org.es4j.eventstore.api.persistence.IPersistStreams;
import org.es4j.eventstore.api.persistence.StreamHead;
import org.es4j.exceptions.ArgumentException;
import org.es4j.exceptions.ArgumentNullException;
import org.es4j.exceptions.ObjectDisposedException;
import org.es4j.messaging.api.EventMessage;
import org.es4j.persistence.sql.SqlDialects.NextPageDelegate;
import org.es4j.serialization.api.ISerialize;
import org.es4j.serialization.api.SerializationExtensions;
import org.es4j.util.Consts;
import org.es4j.util.DateTime;
import org.es4j.util.logging.ILog;
import org.es4j.util.logging.LogFactory;

//namespace EventStore.Persistence.SqlPersistence
//using System;
//using System.Collections.Generic;
//using System.Data;
//using System.Linq;
//using System.Threading;
//using System.Transactions;
//using Logging;
//using Persistence;
//using Serialization;

public class SqlPersistenceEngine implements IPersistStreams {
    
    private static final ILog logger = LogFactory.buildLogger(SqlPersistenceEngine.class);
    private static final DateTime epochTime = new DateTime(1970, 1, 1);
    private final IConnectionFactory connectionFactory;
    private final ISqlDialect            dialect;
    private final ISerialize             serializer;
    private final TransactionScopeOption scopeOption;
    private final int                    pageSize;
    private final AtomicInteger          initialized = new AtomicInteger();

    private       boolean                disposed;

    public SqlPersistenceEngine(
            IConnectionFactory connectionFactory,
            ISqlDialect dialect,
            ISerialize serializer,
            TransactionScopeOption scopeOption,
            int pageSize) {
        
        if (connectionFactory == null) {
            throw new ArgumentNullException("connectionFactory");
        }

        if (dialect == null) {
            throw new ArgumentNullException("dialect");
        }

        if (serializer == null) {
            throw new ArgumentNullException("serializer");
        }

        if (pageSize < 0) {
            throw new ArgumentException("pageSize");
        }

        this.connectionFactory = connectionFactory;
        this.dialect = dialect;
        this.serializer = serializer;
        this.scopeOption = scopeOption;
        this.pageSize = pageSize;

        logger.debug(Messages.usingScope(), this.scopeOption.toString());
    }

    @Override
    public void close() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dispose() {
        this.dispose(true);
        GC.suppressFinalize(this);
    }

    protected void dispose(boolean disposing) {
        if (!disposing || this.disposed) {
            return;
        }

        logger.debug(Messages.shuttingDownPersistence());
        this.disposed = true;
    }

    @Override
    public void initialize() {
        
      //if (Interlocked.Increment(ref this.initialized) > 1) {
        if(initialized.incrementAndGet() > 1) {
            return;
        }

        logger.debug(Messages.initializingStorage());
        this.executeCommand(Consts.EMPTY_UUID, new CommandDelegate<Integer>() {

            @Override
            public Integer execute(IDbStatement cmd) {
                return cmd.executeWithoutExceptions(dialect.initializeStorage());
            }
        });
    }

    @Override
    public Iterable<Commit> getFrom(final UUID streamId, final int minRevision, final int maxRevision) {
        logger.debug(Messages.gettingAllCommitsBetween(), streamId, minRevision, maxRevision);
        Iterable<IDataRecord> records = this.executeQuery(streamId, new QueryDelegate<IDataRecord>() {

            @Override
            public Iterable<IDataRecord> execute(IDbStatement query) {
                String statement = dialect.getCommitsFromStartingRevision();
                query.addParameter(dialect.streamId(),          streamId);
                query.addParameter(dialect.streamRevision(),    minRevision);
                query.addParameter(dialect.maxStreamRevision(), maxRevision);
                query.addParameter(dialect.commitSequence(), 0);
                return query.executePagedQuery(statement, new NextPageDelegate() {

                    @Override
                    public void nextPage(IDbCommand query, IDataRecord record) {
                        ExtensionMethods.setParameter(query, 
                                                      dialect.commitSequence(),                 // parameter name
                                                      CommitExtensions.commitSequence(record)); // parameter value
                    }
                });
            }
        });
        List<Commit> commits = new LinkedList<Commit>();
        for(IDataRecord x : records) {
            commits.add(CommitExtensions.getCommit(x, serializer));
        }        
        return commits;
    }
       
    @Override
    public Iterable<Commit> getFrom(final DateTime start1) {
        final DateTime finalstart = start1.isBefore(epochTime)? epochTime : start1;

        logger.debug(Messages.gettingAllCommitsFrom(), finalstart);
        Iterable<IDataRecord> records = this.executeQuery(Consts.EMPTY_UUID, new QueryDelegate<IDataRecord>() {

            @Override
            public Iterable<IDataRecord> execute(IDbStatement query) {
                String statement = dialect.getCommitsFromInstant();
                query.addParameter(dialect.commitStamp(), finalstart);
                return query.executePagedQuery(statement, new NextPageDelegate() {

                    @Override
                    public void nextPage(IDbCommand command, IDataRecord current) {
                        // Nothing to do
                    }
                });
            }
        });
        List<Commit> commits = new LinkedList<Commit>();
        for(IDataRecord x : records) {
            //.Select(x => x.GetCommit(this.serializer));
            commits.add(CommitExtensions.getCommit(x, serializer));
        }        
        return commits;
    }
                
    @Override
    public void commit(Commit attempt) {
        try {
            this.persistCommit(attempt);
            logger.debug(Messages.commitPersisted(), attempt.getCommitId());
        } catch (Exception e) {
            if(e instanceof UniqueKeyViolationException) {
                throw new RuntimeException();
            }
            if (this.detectDuplicate(attempt)) {
                logger.info(Messages.duplicateCommit());
                throw new DuplicateCommitException(e.getMessage(), e);
            }
            logger.info(Messages.concurrentWriteDetected());
            throw new ConcurrencyException(e.getMessage(), e);
        }
    }

    private void persistCommit(final Commit attempt) {
        final int eventCount = this.countof(attempt.getEvents());
        logger.debug(Messages.attemptingToCommit(), eventCount, 
                                                    attempt.getStreamId(), 
                                                    attempt.getCommitSequence());

        this.executeCommand(attempt.getStreamId(), new CommandDelegate<Integer>() {

            @Override
            public Integer execute(IDbStatement cmd) {
                cmd.addParameter(dialect.streamId(),       attempt.getStreamId());
                cmd.addParameter(dialect.streamRevision(), attempt.getStreamRevision());
                cmd.addParameter(dialect.items(),          eventCount);
                cmd.addParameter(dialect.commitId(),       attempt.getCommitId());
                cmd.addParameter(dialect.commitSequence(), attempt.getCommitSequence());
                cmd.addParameter(dialect.commitStamp(),    attempt.getCommitStamp());
                cmd.addParameter(dialect.headers(), SerializationExtensions.serialize(serializer, attempt.getHeaders()));
                cmd.addParameter(dialect.payload(), SerializationExtensions.serialize(serializer, attempt.getEvents()));
                return cmd.executeNonQuery(dialect.persistCommit());
            }
        });
    }
                
    private boolean detectDuplicate(final Commit attempt) {
        
        return this.executeCommand(attempt.getStreamId(), new CommandDelegate<Boolean>() {

            @Override
            public Boolean execute(IDbStatement cmd) {
                cmd.addParameter(dialect.streamId(), attempt.getStreamId());
                cmd.addParameter(dialect.commitId(), attempt.getCommitId());
                cmd.addParameter(dialect.commitSequence(), attempt.getCommitSequence());
                Object value = cmd.executeScalar(dialect.duplicateCommit());
                return (value.getClass().equals(Integer.class) ? (long)value : (int)value) > 0;
            }
        });
    }

    @Override
    public Iterable<Commit> getUndispatchedCommits() {
        logger.debug(Messages.gettingUndispatchedCommits());
        Iterable<IDataRecord> records = this.executeQuery(Consts.EMPTY_UUID, new QueryDelegate<IDataRecord>() {

            @Override
            public Iterable<IDataRecord> execute(IDbStatement query) {
                return query.executePagedQuery(dialect.getUndispatchedCommits(), new NextPageDelegate() {

                    @Override
                    public void nextPage(IDbCommand command, IDataRecord current) {
                        // Nothing to do
                    }
                });
            }
        });
        List<Commit> commits = new LinkedList<Commit>();
        for(IDataRecord record : records) {
            commits.add(CommitExtensions.getCommit(record, serializer));
        }        
        return commits;
    }
                
    @Override
    public void markCommitAsDispatched(final Commit commit) {
        logger.debug(Messages.markingCommitAsDispatched(), commit.getCommitId());
        this.executeCommand(commit.getStreamId(), new CommandDelegate() {

            @Override
            public Object execute(IDbStatement cmd) {
                cmd.addParameter(dialect.streamId(), commit.getStreamId());
                cmd.addParameter(dialect.commitSequence(), commit.getCommitSequence());
                return cmd.executeWithoutExceptions(dialect.markCommitAsDispatched());
            }
        });
 
    }

    @Override
    public Iterable<StreamHead> getStreamsToSnapshot(final int maxThreshold) {
        logger.debug(Messages.gettingStreamsToSnapshot());
        Iterable<IDataRecord> records = this.executeQuery(Consts.EMPTY_UUID, new QueryDelegate<IDataRecord>() {

            @Override
            public Iterable<IDataRecord> execute(IDbStatement query) {
                String statement = dialect.getStreamsRequiringSnapshots();
                query.addParameter(dialect.streamId(), Consts.EMPTY_UUID);
                query.addParameter(dialect.threshold(), maxThreshold);
                return query.executePagedQuery(statement, new NextPageDelegate() {

                    @Override
                    public void nextPage(IDbCommand query, IDataRecord record) {
                        ExtensionMethods.setParameter(query, dialect.streamId(), dialect.coalesceParameterValue(CommitExtensions.streamId(record)));
                    }
                });
            }
        });
        //.Select(x => x.GetStreamToSnapshot());
        List<StreamHead> heads = new LinkedList<StreamHead>();
        for(IDataRecord record : records) {
            heads.add(StreamHeadExtensions.getStreamToSnapshot(record));
        }        
        return heads;        
    }
    
    @Override
    public Snapshot getSnapshot(final UUID streamId, final int maxRevision) {
        logger.debug(Messages.gettingRevision(), streamId, maxRevision);
        Iterable<IDataRecord> records = this.executeQuery(streamId, new QueryDelegate() {

            @Override
            public Iterable execute(IDbStatement query) {
                String statement = dialect.getSnapshot();
                query.addParameter(dialect.streamId(),       streamId);
                query.addParameter(dialect.streamRevision(), maxRevision);
                return query.executeWithQuery(statement);
            }
        });
        //.Select(x => x.GetSnapshot(this.serializer));
        Snapshot snapshot = null;
        for(IDataRecord record : records) {
            snapshot = SnapshotExtensions.getSnapshot(record, this.serializer);
            break;
        }
        return snapshot;
    }
                
    @Override
    public boolean addSnapshot(final Snapshot snapshot) {
        logger.debug(Messages.addingSnapshot(), snapshot.getStreamId(), snapshot.getStreamRevision());
        int count = this.executeCommand(snapshot.getStreamId(), new CommandDelegate<Integer>() {

            @Override
            public Integer execute(IDbStatement cmd) {
                cmd.addParameter(dialect.streamId(),       snapshot.getStreamId());
                cmd.addParameter(dialect.streamRevision(), snapshot.getStreamRevision());
                cmd.addParameter(dialect.payload(), SerializationExtensions.serialize(serializer, snapshot.getPayload()));
                return cmd.executeWithoutExceptions(dialect.appendSnapshotToCommit());
            }
        });
        return count > 0;
    }
    
    @Override
    public void purge() {
        logger.warn(Messages.purgingStorage());
        this.executeCommand(Consts.EMPTY_UUID, new CommandDelegate<IDataRecord>() {

            @Override
            public IDataRecord execute(IDbStatement cmd) {
                cmd.executeNonQuery(dialect.purgeStorage());
                return null;
            }
        });
    }

    protected <T> Iterable<T> executeQuery(UUID streamId, QueryDelegate query) {
        this.throwWhenDisposed();
        
        try(TransactionScope scope       = this.openQueryScope();
            IDbConnection    connection  = this.connectionFactory.openReplica(streamId);
            IDbTransaction   transaction = this.dialect.openTransaction(connection);
            IDbStatement     statement   = this.dialect.buildStatement(scope, connection, transaction)) 
        {
            statement.setPageSize(this.pageSize);

            logger.verbose(Messages.executingQuery());
            return query.execute(statement);
        } 
        catch (Exception e) {
            logger.debug(Messages.storageThrewException(), e.getClass().getName());
            if (e instanceof StorageUnavailableException) {
                throw new StorageUnavailableException();
            }
            throw new StorageException(e.getMessage(), e);
        }
    }

    protected TransactionScope openQueryScope() {
        TransactionScope scope = this.openCommandScope();
        return scope!=null? scope : new TransactionScope(TransactionScopeOption.Suppress);
    }
                
    private void throwWhenDisposed() {
        if (!this.disposed) {
            return;
        }
        logger.warn(Messages.alreadyDisposed());
        throw new ObjectDisposedException(Messages.alreadyDisposed());
    }
 
    //protected <T> T executeCommand(Guid streamId, Func<IDbStatement, T> command) {
    protected <T> T executeCommand(UUID streamId, CommandDelegate<T> command) {
        this.throwWhenDisposed();

        TransactionScope   scope = this.openCommandScope();
	try (IDbConnection  connection = this.connectionFactory.openMaster(streamId);
             IDbTransaction transaction = this.dialect.openTransaction(connection);
             IDbStatement   statement   = this.dialect.buildStatement(scope, connection, transaction) ) 
        {
            T rowsAffected;
            synchronized(scope) {
                synchronized(connection) {
                    synchronized(transaction) {
                        synchronized(statement) {
                            logger.verbose(Messages.executingCommand());
                            rowsAffected = command.execute(statement);
                            logger.verbose(Messages.commandExecuted(), rowsAffected);

                            if (transaction != null) {
                                transaction.commit();
                            }
                            if (scope != null) {
                                scope.complete();
                            }
                        }
                    }
                }
            }
            return rowsAffected;
        } 
        catch (Exception e) {
            logger.debug(Messages.storageThrewException(), e.getClass().getName());
            if (!recoverableException(e)) {
                throw new StorageException(e.getMessage(), e);
            }
            logger.info(Messages.recoverableExceptionCompletesScope());
            if (scope != null) {
                scope.complete();
            }
            throw new RuntimeException(e);
        }
    }
                
    protected TransactionScope openCommandScope() {
        return new TransactionScope(this.scopeOption);
    }
                
    private static boolean recoverableException(Exception e) {
        return e.getClass().equals(UniqueKeyViolationException.class) || 
               e.getClass().equals(StorageUnavailableException.class);
    }
    
    private int countof(List<?> objects) {
        int count = 0;
        for(Object object : objects) {
            count++;
        }
        return count;
    }

    private abstract class QueryDelegate<T> {
        public abstract Iterable<T> execute(IDbStatement query);
    }

    private abstract class CommandDelegate<T> {
        public abstract T  execute(IDbStatement command);
    }
}