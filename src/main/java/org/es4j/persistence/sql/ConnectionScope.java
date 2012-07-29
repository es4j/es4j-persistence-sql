package org.es4j.persistence.sql;

//using System;

import org.es4j.dotnet.data.IDbCommand;
import org.es4j.dotnet.data.IDbConnection;
import org.es4j.dotnet.data.IDbTransaction;

//using System.Data;

public class ConnectionScope extends ThreadScope<IDbConnection> implements IDbConnection 

    public ConnectionScope(String connectionName, Func<IDbConnection> factory) {
        super(connectionName, factory);
    }
    
    IDbTransaction /*IDbConnection.*/beginTransaction() {
        return this.getCurrent().beginTransaction();
    }
    
    IDbTransaction /*IDbConnection.*/beginTransaction(IsolationLevel il) {
        return this.getCurrent().beginTransaction(il);
    }
    
    void /*IDbConnection.*/close() {
        // no-op--let Dispose do the real work.
    }
    
    void /*IDbConnection.*/changeDatabase(String databaseName) {
        this.getCurrent().changeDatabase(databaseName);
    }
    
    IDbCommand /*IDbConnection.*/createCommand() {
        return this.getCurrent().createCommand();
    }
    
    void /*IDbConnection.*/open() {
        this.getCurrent().open();
    }
    
    protected String /*IDbConnection.*/connectionString;
    {
			get { return this.Current.ConnectionString; }
			set { this.Current.ConnectionString = value; }
    }
                
    int IDbConnection.ConnectionTimeout
    {
        get { return this.Current.ConnectionTimeout; }
    }
                
    String IDbConnection.Database
    {
        get { return this.Current.Database; }
    }
                
    ConnectionState IDbConnection.State
    {
        get { return this.Current.State; }
    }
}
