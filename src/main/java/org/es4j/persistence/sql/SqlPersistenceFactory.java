package org.es4j.persistence.sql;

import org.es4j.dotnet.data.ConnectionStringSettings;
import org.es4j.dotnet.data.TransactionScopeOption;
import org.es4j.eventstore.api.persistence.IPersistStreams;
import org.es4j.eventstore.api.persistence.IPersistenceFactory;
import org.es4j.exceptions.ArgumentNullException;
import org.es4j.persistence.sql.SqlDialects.MySqlDialect;
import org.es4j.serialization.api.ISerialize;

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
    protected     int                     pageSize; // { get; set; }

    public SqlPersistenceFactory(String connectionName, ISerialize serializer) {
        this(connectionName, serializer, null);
    }
                
    public SqlPersistenceFactory(String connectionName, 
                               ISerialize serializer, 
                               ISqlDialect dialect) {
        //this(serializer, TransactionScopeOption.Suppress, defaultPageSize);
        this.serializer  = serializer;
        this.scopeOption = TransactionScopeOption.Suppress;
        this.pageSize    = defaultPageSize;
    
        this.connectionFactory = new ConfigurationConnectionFactory(connectionName);
        this.dialect           = dialect!=null? dialect : resolveDialect(new ConfigurationConnectionFactory(connectionName).getSettings());
    }
    
    public SqlPersistenceFactory(IConnectionFactory factory, 
                                ISerialize        serializer, 
                                ISqlDialect       dialect) {
        this(factory, serializer, dialect, TransactionScopeOption.Suppress, defaultPageSize);
    }

    public SqlPersistenceFactory(IConnectionFactory     factory,
			        ISerialize             serializer,
			        ISqlDialect            dialect,
			        TransactionScopeOption scopeOption,
			        int                    pageSize) {
        //this(serializer, scopeOption, pageSize);
        this.serializer  = serializer;
        this.scopeOption = scopeOption;
        this.pageSize    = pageSize;
               
        if (dialect == null) {
            throw new ArgumentNullException("dialect");
        }
        this.connectionFactory = factory;
        this.dialect           = dialect;
    }

    private SqlPersistenceFactory(ISerialize            serializer, 
                                TransactionScopeOption scopeOption, 
                                int                    pageSize) {
        this.serializer  = serializer;
        this.scopeOption = scopeOption;
        this.pageSize    = pageSize;
        
        this.connectionFactory = null;
        this.dialect           = null;
    }

    protected IConnectionFactory getConnectionFactory() {
        return this.connectionFactory;
    }
    
    protected ISqlDialect getDialect() {
        return this.dialect;
    }
    
    protected ISerialize getSerializer() {
        return this.serializer;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public IPersistStreams build() {
        return new SqlPersistenceEngine(this.getConnectionFactory(), 
                                        this.getDialect(), 
                                        this.getSerializer(), 
                                        this.scopeOption, 
                                        this.getPageSize());
    }

    private static ISqlDialect resolveDialect(ConnectionStringSettings settings) {
        String connectionString = settings.getConnectionString().toUpperCase(); //ToUpperInvariant();
        String providerName     = settings.getProviderName().toUpperCase();     //ToUpperInvariant();

        if (providerName.contains("MYSQL")) {
            //return new MySqlDialect();
        }

        if (providerName.contains("SQLITE")) {
            throw new UnsupportedOperationException("Not yet implemented");
            //return new SqliteDialect();
        }

        if (providerName.contains("SQLSERVERCE") || connectionString.contains(".SDF")) {
            throw new UnsupportedOperationException("Not yet implemented");
            //return new SqlCeDialect();
        }

        if (providerName.contains("FIREBIRD")) {
            throw new UnsupportedOperationException("Not yet implemented");
            //return new FirebirdSqlDialect();
        }

        if (providerName.contains("POSTGRES") || providerName.contains("NPGSQL")) {
            throw new UnsupportedOperationException("Not yet implemented");
            //return new PostgreSqlDialect();
        }

        if (providerName.contains("FIREBIRD")) {
            throw new UnsupportedOperationException("Not yet implemented");
            //return new FirebirdSqlDialect();
        }

        if (providerName.contains("OLEDB") && connectionString.contains("MICROSOFT.JET")) {
            throw new UnsupportedOperationException("Not yet implemented");
            //return new AccessDialect();
        }
        
        if (providerName.contains("MSSQL")) {
            throw new UnsupportedOperationException("Not yet implemented");
            //return new AccessDialect();
        }

        return new MySqlDialect();
    }
}