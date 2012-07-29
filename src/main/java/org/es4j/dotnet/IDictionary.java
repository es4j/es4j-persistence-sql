package org.es4j.dotnet;

/**
 *
 * @author Esfand
 */
public interface IDictionary<T0, T1> {

    public KeyValuePair<T0, T1> firstOrDefault();

    public void put(T0 key, T1 value);

    public boolean contains(String connectionName);

    public T1 get(T0 key);
    
}
