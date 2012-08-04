package org.es4j.persistence.sql.SqlDialects;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.es4j.dotnet.GC;
import org.es4j.dotnet.IDisposable;
import org.es4j.dotnet.data.TransactionScope;
import org.es4j.dotnet.data.IDataReader;
import org.es4j.dotnet.data.IDataRecord;
import org.es4j.dotnet.data.IDbCommand;
import org.es4j.exceptions.ObjectDisposedException;
import org.es4j.persistence.sql.ExtensionMethods;
import org.es4j.persistence.sql.ISqlDialect;
import org.es4j.persistence.sql.Messages;
import org.es4j.persistence.sql.StorageUnavailableException;
import org.es4j.util.logging.ILog;
import org.es4j.util.logging.LogFactory;

//namespace EventStore.Persistence.SqlPersistence.SqlDialects
//using System;
//using System.Collections;
//using System.Collections.Generic;
//using System.Data;
//using System.Transactions;
//using Logging;

//public class PagedEnumerationCollection implements IEnumerable<IDataRecord>, IEnumerator<IDataRecord> {
//public class PagedEnumerationCollection extends AbstractList<IDataRecord> {
public class PagedEnumerationCollection implements Iterable<IDataRecord> {    
    private static final ILog logger = LogFactory.buildLogger(PagedEnumerationCollection.class);
    private final Iterable<IDisposable> disposable; // = new IDisposable[] { };
    private final ISqlDialect      dialect;
    private final IDbCommand       command;
    private final NextPageDelegate nextpageDelegate;
    private final int              pageSize;
    private final TransactionScope scope;

    private IDataReader reader;
    private int         position;
    private IDataRecord current;
    private boolean     disposed;

    public PagedEnumerationCollection(TransactionScope      scope,
			              ISqlDialect           dialect,
			              IDbCommand            command,
			              NextPageDelegate      nextpage,
			              int                   pageSize,
			              Iterable<IDisposable> disposable)
    {
        this.scope      = scope;
        this.dialect    = dialect;
        this.command    = command;
        this.nextpageDelegate   = nextpage;
        this.pageSize   = pageSize;
        this.disposable = disposable != null ? disposable : new LinkedList<IDisposable>();
    }

    public void dispose() {
        this.dispose(true);
        GC.suppressFinalize(this);
    }

    protected void dispose(boolean disposing) {
        if (!disposing || this.disposed) {
            return;
        }

        this.disposed = true;
        this.position = 0;
        this.current = null;

        if (this.reader != null) {
            this.reader.dispose();
        }

        this.reader = null;

        if (this.command != null) {
            try {
                this.command.dispose();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        // queries do not modify state and thus calling Complete() on a so-called 'failed' query only
        // allows any outer transaction scope to decide the fate of the transaction
        if (this.scope != null) {
            this.scope.complete(); // caller will dispose scope.
        }
        for (IDisposable disp : this.disposable) {
            try {
                disp.dispose();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public Iterable<IDataRecord> getEnumerator() {
        if (this.disposed) {
            throw new ObjectDisposedException(Messages.objectAlreadyDisposed());
        }
        return this;
    }

    Iterable IEnumerable_getEnumerator() {
        return this.getEnumerator();
    }

    boolean IEnumerator_moveNext() {
        if (this.disposed) {
            throw new ObjectDisposedException(Messages.objectAlreadyDisposed());
        }
        if (this.moveToNextRecord()) {
            return true;
        }

        logger.verbose(Messages.queryCompleted());
        return false;
    }

    private boolean moveToNextRecord() {
        if (this.pageSize > 0 && this.position >= this.pageSize) {
            ExtensionMethods.setParameter(this.command, this.dialect.skip(), this.position);
            (this.nextpageDelegate).nextPage(this.command, this.current);
        }

        this.reader = this.reader != null ? this.reader : this.openNextPage();

        if (this.reader.read()) {
            return this.incrementPosition();
        }

        if (!this.pagingEnabled()) {
            return false;
        }

        if (!this.pageCompletelyEnumerated()) {
            return false;
        }

        logger.verbose(Messages.enumeratedRowCount(), this.position);
        this.reader.dispose();
        this.reader = this.openNextPage();

        if (this.reader.read()) {
            return this.incrementPosition();
        }

        return false;
    }

    private boolean incrementPosition() {
        this.position++;
        return true;
    }

    private boolean pagingEnabled() {
        return this.pageSize > 0;
    }

    private boolean pageCompletelyEnumerated() {
        return this.position > 0 && 0 == this.position % this.pageSize;
    }

    private IDataReader openNextPage() {
        try {
            return this.command.executeReader();
        } catch (Exception e) {
            logger.debug(Messages.enumerationThrewException(), e.getClass().getName());
            throw new StorageUnavailableException(e.getMessage(), e);
        }
    }

    public void reset() {
        //throw new NotSupportedException("Forward-only readers.");
        throw new UnsupportedOperationException("Forward-only readers.");
    }

    public IDataRecord getCurrent() {
        if (this.disposed) {
            throw new ObjectDisposedException(Messages.objectAlreadyDisposed());
        }
        return this.current = this.reader;
    }

    Object IEnumerator_getCurrent() {
        return this/*((Iterable<IDataRecord>)this)*/.getCurrent();
    }

/*
    @Override
    public IDataRecord get(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
*/
    @Override
    public Iterator<IDataRecord> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}