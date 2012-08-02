package org.es4j.persistence.sql;

import java.util.concurrent.atomic.AtomicInteger;
import org.es4j.dotnet.GC;
import org.es4j.util.Guid;
import org.es4j.dotnet.IEnumerable;
import org.es4j.dotnet.data.TransactionScope;
import org.es4j.dotnet.data.IDataRecord;
import org.es4j.dotnet.data.IDbCommand;
import org.es4j.dotnet.data.TransactionScopeOption;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.ConcurrencyException;
import org.es4j.eventstore.api.Snapshot;
import org.es4j.eventstore.api.persistence.IPersistStreams;
import org.es4j.exceptions.ArgumentException;
import org.es4j.exceptions.ArgumentNullException;
import org.es4j.exceptions.ObjectDisposedException;
//import org.es4j.perseistence.sql.sqldialects.NextPageDelegate;
import org.es4j.serialization.api.ISerialize;
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
        this.executeCommand(Guid.empty(), new CommandDelegate() {

            @Override
            public Object execute(IDbStatement cmd) {
                return cmd.executeWithoutExceptions(dialect.initializeStorage());
            }
        });
    }

    public Iterable<Commit> getFrom(final Guid streamId, final int minRevision, final int maxRevision) {
        logger.debug(Messages.gettingAllCommitsBetween(), streamId, minRevision, maxRevision);
        return this.executeQuery(streamId, new QueryDelegate<Commit>() {

    @Override
    public Iterable<Commit> execute(IDbStatement query) {
                String statement = dialect.getCommitsFromStartingRevision();
                query.addParameter(dialect.streamId(),          streamId);
                query.addParameter(dialect.streamRevision(),    minRevision);
                query.addParameter(dialect.maxStreamRevision(), maxRevision);
                query.addParameter(dialect.commitSequence(), 0);
                return query.executePagedQuery(statement, new NextPageDelegate() {

                    @Override
                    public void nextPage(IDbCommand query, IDataRecord record) {
                        query.setParameter(dialect.commitSequence(), record.commitSequence()));
                    }
                });
                                (q, r) => q.setParameter(this.dialect.CommitSequence, r.CommitSequence()))
                        .Select(x => x.getCommit(this.serializer));
            }
        });
    }
                
    @Override
    public Iterable<Commit> getFrom(DateTime start) {
        final DateTime finalstart = start < epochTime ? epochTime : start;

        logger.debug(Messages.gettingAllCommitsFrom(), finalstart);
        return this.executeQuery(Guid.empty(), new QueryDelegate<Commit>() {

            @Override
            public Iterable<Commit> execute(IDbStatement query) {
                String statement = dialect.getCommitsFromInstant();
                query.addParameter(dialect.commitStamp(), finalstart);
                return query.executePagedQuery(statement, new PagedQuery());
                
                (q, r) => { })
					.Select(x => x.GetCommit(this.serializer));
            }
        });
        
			{

			});
    }
                
    @Override
    public void commit(Commit attempt) {
        try {
            this.persistCommit(attempt);
            logger.debug(Messages.commitPersisted(), attempt.getCommitId());
        } catch (Exception e) {
            if (!(e.getClass().equals(UniqueKeyViolationException.class))  {
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
        logger.debug(Messages.attemptingToCommit(), attempt.getEvents().Count, 
                                                    attempt.getStreamId(), 
                                                    attempt.getCommitSequence());

        this.executeCommand(attempt.getStreamId(), new CommandDelegate() {

            @Override
            public Object execute(IDbStatement cmd) {
                cmd.addParameter(dialect.streamId(),       attempt.getStreamId());
                cmd.addParameter(dialect.streamRevision(), attempt.getStreamRevision());
                cmd.addParameter(dialect.items(),          attempt.getEvents().Count);
                cmd.addParameter(dialect.commitId(),       attempt.getCommitId());
                cmd.addParameter(dialect.commitSequence(), attempt.getCommitSequence());
                cmd.addParameter(dialect.commitStamp(),    attempt.getCommitStamp());
                cmd.addParameter(dialect.headers(), serializer.Serialize(attempt.getHeaders()));
                cmd.addParameter(dialect.payload(), serializer.Serialize(attempt.getEvents()));
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
    public IEnumerable<Commit> getUndispatchedCommits() {
        logger.debug(Messages.gettingUndispatchedCommits());
        return this.executeQuery(Guid.empty(), new QueryDelegate<Commit>() {

            @Override
            public IEnumerable<Commit> execute(IDbStatement query) {
                query.executePagedQuery(this.dialect.getUndispatchedCommits(), (q, r) => { }))
					.Select(x => x.GetCommit(this.serializer))
					.ToArray(); // avoid paging
            }
        });
    }
                
    @Override
    public void markCommitAsDispatched(Commit commit) {
			Logger.Debug(Messages.MarkingCommitAsDispatched, commit.CommitId);
			this.ExecuteCommand(commit.StreamId, cmd =>
			{
				cmd.AddParameter(this.dialect.StreamId, commit.StreamId);
				cmd.AddParameter(this.dialect.CommitSequence, commit.CommitSequence);
				return cmd.ExecuteWithoutExceptions(this.dialect.MarkCommitAsDispatched);
			});
    }

    public IEnumerable<StreamHead> getStreamsToSnapshot(int maxThreshold) {
			logger.debug(Messages.gettingStreamsToSnapshot());
			return this.ExecuteQuery(Guid.Empty, query =>
			{
				var statement = this.dialect.GetStreamsRequiringSnapshots;
				query.AddParameter(this.dialect.StreamId, Guid.Empty);
				query.AddParameter(this.dialect.Threshold, maxThreshold);
				return query.ExecutePagedQuery(statement,
						(q, s) => q.SetParameter(this.dialect.StreamId, this.dialect.CoalesceParameterValue(s.StreamId())))
					.Select(x => x.GetStreamToSnapshot());
			});
    }
                
    public Snapshot getSnapshot(Guid streamId, int maxRevision) {
			Logger.Debug(Messages.GettingRevision, streamId, maxRevision);
			return this.ExecuteQuery(streamId, query =>
			{
				var statement = this.dialect.GetSnapshot;
				query.AddParameter(this.dialect.StreamId, streamId);
				query.AddParameter(this.dialect.StreamRevision, maxRevision);
				return query.ExecuteWithQuery(statement).Select(x => x.GetSnapshot(this.serializer));
			}).FirstOrDefault();
    }
                
    public boolean addSnapshot(Snapshot snapshot) {
			logger.debug(Messages.addingSnapshot(), snapshot.StreamId, snapshot.StreamRevision);
			return this.ExecuteCommand(snapshot.StreamId, cmd =>
			{
				cmd.AddParameter(this.dialect.StreamId, snapshot.StreamId);
				cmd.AddParameter(this.dialect.StreamRevision, snapshot.StreamRevision);
				cmd.AddParameter(this.dialect.Payload, this.serializer.Serialize(snapshot.Payload));
				return cmd.ExecuteWithoutExceptions(this.dialect.AppendSnapshotToCommit);
			}) > 0;
    }

    public void purge() {
        logger.warn(Messages.purgingStorage());
        this.executeCommand(Guid.empty(), new QueryDelegate() {

            @Override
            public IEnumerable execute(IDbStatement query) {
                return executeNonQuery(dialect.purgeStorage());
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
				this.executeNonQuery(this.dialect.purgeStorage());
    }

  //protected IEnumerable<T> ExecuteQuery<T>(Guid streamId, Func<IDbStatement, IEnumerable<T>> query) {
    protected <T> Iterable<T> executeQuery(Guid streamId, QueryDelegate query) {
                    
			this.ThrowWhenDisposed();

			var scope = this.OpenQueryScope();
			IDbConnection connection = null;
			IDbTransaction transaction = null;
			IDbStatement statement = null;

			try
			{
				connection = this.connectionFactory.OpenReplica(streamId);
				transaction = this.dialect.OpenTransaction(connection);
				statement = this.dialect.BuildStatement(scope, connection, transaction);
				statement.PageSize = this.pageSize;

				Logger.Verbose(Messages.ExecutingQuery);
				return query(statement);
			}
			catch (Exception e)
			{
				if (statement != null)
					statement.Dispose();
				if (transaction != null)
					transaction.Dispose();
				if (connection != null)
					connection.Dispose();
				if (scope != null)
					scope.Dispose();

				Logger.Debug(Messages.StorageThrewException, e.GetType());
				if (e is StorageUnavailableException)
					throw;

				throw new StorageException(e.Message, e);
			}
    }
    
    protected TransactionScope openQueryScope() {
        return this.openCommandScope() ?? new TransactionScope(TransactionScopeOption.Suppress);
    }
                
    private void ThrowWhenDisposed() {
        if (!this.disposed) {
            return;
        }

        logger.warn(Messages.alreadyDisposed());
        throw new ObjectDisposedException(Messages.alreadyDisposed());
    }

    //protected <T> T executeCommand(Guid streamId, Func<IDbStatement, T> command) {
    protected <T> T executeCommand(Guid streamId, CommandDelegate<T> command) {
    
			this.throwWhenDisposed();

			using (var scope = this.OpenCommandScope())
			using (var connection = this.connectionFactory.OpenMaster(streamId))
			using (var transaction = this.dialect.OpenTransaction(connection))
			using (var statement = this.dialect.BuildStatement(scope, connection, transaction))
			{
				try
				{
					Logger.Verbose(Messages.ExecutingCommand);
					var rowsAffected = command(statement);
					Logger.Verbose(Messages.CommandExecuted, rowsAffected);

					if (transaction != null)
						transaction.Commit();

					if (scope != null)
						scope.Complete();

					return rowsAffected;
				}
				catch (Exception e)
				{
					Logger.Debug(Messages.StorageThrewException, e.GetType());
					if (!RecoverableException(e))
						throw new StorageException(e.Message, e);

					Logger.Info(Messages.RecoverableExceptionCompletesScope);
					if (scope != null)
						scope.Complete();

					throw;
				}
			}
    }
                
    protected TransactionScope openCommandScope() {
        return new TransactionScope(this.scopeOption);
    }
                
    private static boolean recoverableException(Exception e) {
        return e.getClass().equals(UniqueKeyViolationException.class) || 
               e.getClass().equals(StorageUnavailableException.class);
    }

    private abstract class QueryDelegate<T> {
        public abstract Iterable<T> execute(IDbStatement query);
    }

    private abstract class CommandDelegate<T> {
        public abstract T  execute(IDbStatement statement);
    }

}