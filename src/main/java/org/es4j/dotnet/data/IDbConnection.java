package org.es4j.dotnet.data;

import org.es4j.dotnet.IDisposable;
//namespace System.Data

// Represents an open connection to a data source, and is implemented by .NET
// Framework data providers that access relational databases.
public interface IDbConnection extends IDisposable {

    // Gets or sets the string used to open a database.
    // Returns:
    //     A string containing connection settings.
    //String connectionString; // { get; set; }
    public String getConnectionString();
    public void   setConnectionString(String connectionString);

    // Gets the time to wait while trying to establish a connection before terminating
    // the attempt and generating an error.
    //
    // Returns:
    //     The time (in seconds) to wait for a connection to open. The default value
    //     is 15 seconds.
    //int connectionTimeout; // { get; }
    public int getConnectionTimeout();

    // Gets the name of the current database or the database to be used after a
    // connection is opened.
    //
    // Returns:
    //     The name of the current database or the name of the database to be used once
    //     a connection is open. The default value is an empty string.
    //String database; // { get; }
    public String getDatabase();

    // Gets the current state of the connection.
    //
    // Returns:
    //     One of the System.Data.ConnectionState values.
    //ConnectionState state; // { get; }
    public ConnectionState getState();

    // Begins a database transaction.
    //
    // Returns:
    //     An object representing the new transaction.
    IDbTransaction beginTransaction();

    // Begins a database transaction with the specified System.Data.IsolationLevel
    // value.
    //
    // Parameters:
    //   il:
    //     One of the System.Data.IsolationLevel values.
    //
    // Returns:
    //     An object representing the new transaction.
    IDbTransaction beginTransaction(IsolationLevel il);

    // Changes the current database for an open Connection object.
    //
    // Parameters:
    //   databaseName:
    //     The name of the database to use in place of the current database.
    void changeDatabase(String databaseName);

    // Closes the connection to the database.
    void close();

    // Creates and returns a Command object associated with the connection.
    //
    // Returns:
    //     A Command object associated with the connection.
    IDbCommand createCommand();

    // Opens a database connection with the settings specified by the ConnectionString
    // property of the provider-specific Connection object.
    void open();
}