package org.es4j.persistence.sql;

import org.es4j.dotnet.data.ConnectionState;
import org.es4j.dotnet.data.IDbCommand;
import org.es4j.dotnet.data.IDbConnection;
import org.es4j.dotnet.data.IDbTransaction;
import org.es4j.dotnet.data.IsolationLevel;

//using System;
//using System.Data;

public class ConnectionScope extends    ThreadScope<IDbConnection> 
                             implements IDbConnection {

    public ConnectionScope(String                         connectionName, 
                           FactoryDelegate<IDbConnection> factory) {
        super(connectionName, factory);
    }
    
    @Override
    public IDbTransaction beginTransaction() {
        return this.getCurrent().beginTransaction();
    }
    
    @Override
    public IDbTransaction beginTransaction(IsolationLevel il) {
        return this.getCurrent().beginTransaction(il);
    }
    
    @Override
    public void close() {
        // no-op--let Dispose do the real work.
    }
    
    @Override
    public void changeDatabase(String databaseName) {
        this.getCurrent().changeDatabase(databaseName);
    }
    
    @Override
    public IDbCommand createCommand() {
        return this.getCurrent().createCommand();
    }
    
    @Override
    public void open() {
        this.getCurrent().open();
    }

    @Override
    public String getConnectionString() {
        return this.getCurrent().getConnectionString();
    }
    @Override
    public void setConnectionString(String connectionString) {
        this.getCurrent().setConnectionString(connectionString);
    }

    @Override
    public int getConnectionTimeout() {
        return this.getCurrent().getConnectionTimeout();
    }

    @Override
    public String getDatabase() {
        return this.getCurrent().getDatabase();
    }

    @Override
    public ConnectionState getState() {
        return this.getCurrent().getState();
    }
}
