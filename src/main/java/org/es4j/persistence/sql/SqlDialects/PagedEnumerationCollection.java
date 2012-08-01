package org.es4j.persistence.sql.SqlDialects;

import java.util.LinkedList;
import java.util.List;
import org.es4j.dotnet.GC;
import org.es4j.dotnet.IDisposable;
import org.es4j.dotnet.IEnumerable;
import org.es4j.dotnet.TransactionScope;
import org.es4j.dotnet.data.IDataRecord;
import org.es4j.dotnet.data.IDbCommand;
import org.es4j.exceptions.ObjectDisposedException;
import org.es4j.persistence.sql.ISqlDialect;
import org.es4j.util.logging.ILog;
import org.es4j.util.logging.LogFactory;

//namespace EventStore.Persistence.SqlPersistence.SqlDialects
//using System;
//using System.Collections;
//using System.Collections.Generic;
//using System.Data;
//using System.Transactions;
//using Logging;

public class PagedEnumerationCollection implements IEnumerable<IDataRecord>, IEnumerator<IDataRecord> {
    
    private static final ILog Logger = LogFactory.buildLogger(PagedEnumerationCollection.class);
    private final List<IDisposable> disposable; // = new IDisposable[] { };
    private final ISqlDialect      dialect;
    private final IDbCommand       command;
    private final NextPageDelegate nextpage;
    private final int              pageSize;
    private final TransactionScope scope;

    private IDataReader reader;
    private int         position;
    private IDataRecord current;
    private boolean     disposed;

    public PagedEnumerationCollection(TransactionScope  scope,
			              ISqlDialect       dialect,
			              IDbCommand        command,
			              NextPageDelegate  nextpage,
			              int               pageSize,
			              List<IDisposable> disposable)
    {
        this.scope = scope;
        this.dialect = dialect;
        this.command = command;
        this.nextpage = nextpage;
        this.pageSize = pageSize;
        this.disposable = disposable != null ? disposable : new LinkedList<IDisposable>();
    }

    public void Dispose() {
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
            this.command.dispose();
        }

        // queries do not modify state and thus calling Complete() on a so-called 'failed' query only
        // allows any outer transaction scope to decide the fate of the transaction
        if (this.scope != null) {
            this.scope.complete(); // caller will dispose scope.
        }
        for (IDisposable disposable : this.disposable) {
            disposable.dispose();
        }
    }

    public IEnumerator<IDataRecord> getEnumerator()
		{
			if (this.disposed)
				throw new ObjectDisposedException(Messages.objectAlreadyDisposed());

			return this;
		}
                
		IEnumerator IEnumerable.GetEnumerator()
		{
			return this.GetEnumerator();
		}

		bool IEnumerator.MoveNext()
		{
			if (this.disposed)
				throw new ObjectDisposedException(Messages.ObjectAlreadyDisposed);

			if (this.MoveToNextRecord())
				return true;

			Logger.Verbose(Messages.QueryCompleted);
			return false;
		}
		private bool MoveToNextRecord()
		{
			if (this.pageSize > 0 && this.position >= this.pageSize)
			{
				this.command.SetParameter(this.dialect.Skip, this.position);
				this.nextpage(this.command, this.current);
			}

			this.reader = this.reader ?? this.OpenNextPage();

			if (this.reader.Read())
				return this.IncrementPosition();

			if (!this.PagingEnabled())
				return false;

			if (!this.PageCompletelyEnumerated())
				return false;

			Logger.Verbose(Messages.EnumeratedRowCount, this.position);
			this.reader.Dispose();
			this.reader = this.OpenNextPage();

			if (this.reader.Read())
				return this.IncrementPosition();

			return false;
		}

		private bool IncrementPosition()
		{
			this.position++;
			return true;
		}

		private bool PagingEnabled()
		{
			return this.pageSize > 0;
		}
		private bool PageCompletelyEnumerated()
		{
			return this.position > 0 && 0 == this.position % this.pageSize;
		}
		private IDataReader OpenNextPage()
		{
			try
			{
				return this.command.ExecuteReader();
			}
			catch (Exception e)
			{
				Logger.Debug(Messages.EnumerationThrewException, e.GetType());
				throw new StorageUnavailableException(e.Message, e);
			}
		}

		public virtual void Reset()
		{
			throw new NotSupportedException("Forward-only readers.");
		}
		public virtual IDataRecord Current
		{
			get
			{
				if (this.disposed)
					throw new ObjectDisposedException(Messages.ObjectAlreadyDisposed);

				return this.current = this.reader;
			}
		}
		object IEnumerator.Current
		{
			get { return ((IEnumerator<IDataRecord>)this).Current; }
		}
	}
}