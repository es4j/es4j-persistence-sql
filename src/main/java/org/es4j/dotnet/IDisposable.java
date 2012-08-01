package org.es4j.dotnet;

import java.io.Closeable;


public interface IDisposable extends Closeable {

    public void dispose();
    
}
