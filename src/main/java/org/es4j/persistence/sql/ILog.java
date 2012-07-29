package org.es4j.persistence.sql;

/**
 *
 * @author Esfand
 */
public interface ILog {

    public void debug(String template, Object... object);

    public void verbose(String template, Object... object);

    public void warn(String template, Object... object);
    
}
