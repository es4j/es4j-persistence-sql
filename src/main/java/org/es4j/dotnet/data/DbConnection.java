package org.es4j.dotnet.data;

/**
 *
 * @author Esfand
 */
public class DbConnection implements IDbConnection {

    @Override
    public String getConnectionString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setConnectionString(String connectionString) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getConnectionTimeout() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getDatabase() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ConnectionState getState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IDbTransaction beginTransaction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IDbTransaction beginTransaction(IsolationLevel il) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void changeDatabase(String databaseName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IDbCommand createCommand() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void open() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
