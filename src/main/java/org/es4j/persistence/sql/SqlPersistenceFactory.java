package org.es4j.persistence.sql;

import org.es4j.dotnet.data.TransactionScopeOption;
import org.es4j.persistence.IPersistenceFactory;
import org.es4j.persistence.ISerialize;

//using System;
//using System.Configuration;
//using System.Transactions;
//using Serialization;
//using SqlDialects;

public class SqlPersistenceFactory implements IPersistenceFactory {
    private static final int             defaultPageSize = 128;
    private final IConnectionFactory     connectionFactory;
    private final ISqlDialect            dialect;
    private final ISerialize             serializer;
    private final TransactionScopeOption scopeOption;

    public SqlPersistenceFactory(String connectionName, ISerialize serializer) {
        this(connectionName, serializer, null);
    }
                
    public SqlPersistenceFactory(String connectionName, ISerialize serializer, ISqlDialect dialect) {
        this(serializer, TransactionScopeOption.Suppress, defaultPageSize);
        this.connectionFactory = new ConfigurationConnectionFactory(connectionName);
        this.dialect = dialect ?? ResolveDialect(new ConfigurationConnectionFactory(connectionName).Settings);
    }
    
    public SqlPersistenceFactory(IConnectionFactory factory, ISerialize serializer, ISqlDialect dialect)
        this(factory, serializer, dialect, TransactionScopeOption.Suppress, DefaultPageSize)
    }

    public SqlPersistenceFactory(IConnectionFactory     factory,
			         ISerialize             serializer,
			         ISqlDialect            dialect,
			         TransactionScopeOption scopeOption,
			         int                    pageSize) {
        this(serializer, scopeOption, pageSize);
        if (dialect == null) {
            throw new ArgumentNullException("dialect");
        }

        this.connectionFactory = factory;
        this.dialect = dialect;
    }

		private SqlPersistenceFactory(ISerialize serializer, TransactionScopeOption scopeOption, int pageSize)
		{
			this.serializer = serializer;
			this.scopeOption = scopeOption;

			this.PageSize = pageSize;
		}

		protected virtual IConnectionFactory ConnectionFactory
		{
			get { return this.connectionFactory; }
		}
		protected virtual ISqlDialect Dialect
		{
			get { return this.dialect; }
		}
		protected virtual ISerialize Serializer
		{
			get { return this.serializer; }
		}
		protected int PageSize { get; set; }

		public virtual IPersistStreams Build()
		{
			return new SqlPersistenceEngine(
				this.ConnectionFactory, this.Dialect, this.Serializer, this.scopeOption, this.PageSize);
		}

		private static ISqlDialect ResolveDialect(ConnectionStringSettings settings)
		{
			var connectionString = settings.ConnectionString.ToUpperInvariant();
			var providerName = settings.ProviderName.ToUpperInvariant();

			if (providerName.Contains("MYSQL"))
				return new MySqlDialect();

			if (providerName.Contains("SQLITE"))
				return new SqliteDialect();

			if (providerName.Contains("SQLSERVERCE") || connectionString.Contains(".SDF"))
				return new SqlCeDialect();

			if (providerName.Contains("FIREBIRD"))
				return new FirebirdSqlDialect();

			if (providerName.Contains("POSTGRES") || providerName.Contains("NPGSQL"))
				return new PostgreSqlDialect();

			if (providerName.Contains("FIREBIRD"))
				return new FirebirdSqlDialect();

			if (providerName.Contains("OLEDB") && connectionString.Contains("MICROSOFT.JET"))
				return new AccessDialect();

			return new MsSqlDialect();
		}
	}
}