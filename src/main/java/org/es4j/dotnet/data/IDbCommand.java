package org.es4j.dotnet.data;

import org.es4j.dotnet.IDisposable;

/**
 *
 * @author Esfand
 */
public interface IDbCommand extends IDisposable {

    public void setTransaction(IDbTransaction transaction);

    public void setCommandText(String statement);

    public int executeNonQuery();

    public Object executeScalar();

    public Object getParameters();

    public void createParameter();
    
}
