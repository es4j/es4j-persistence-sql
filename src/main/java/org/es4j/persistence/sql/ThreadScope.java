package org.es4j.persistence.sql;

import java.io.IOException;
import org.es4j.dotnet.GC;
import org.es4j.dotnet.HttpContext;
import org.es4j.dotnet.IDisposable;
import org.es4j.dotnet.Thread;
import org.es4j.exceptions.ArgumentException;
import org.es4j.util.StringExt;
import org.es4j.util.logging.ILog;
import org.es4j.util.logging.LogFactory;

//using System;
//using System.Threading;
//using System.Web;
//using Logging;

public class ThreadScope<T> implements IDisposable {

    private final ILog        logger  = LogFactory.buildLogger(ThreadScope.class);
    private final HttpContext context = HttpContext.getCurrent();
    private final String      threadKey;
    private final T           current;
    private final boolean     rootScope;
    private       boolean     disposed;   

    public ThreadScope(String key, FactoryDelegate<T> factory) {
        
      //this.threadKey = typeof(ThreadScope<T>).Name + ":[{0}]".formatWith(key ?? string.Empty);
        this.threadKey = StringExt.formatWith("typeof(ThreadScope<T>).Name" + ":[{0}]", key!=null?key:"");

        T parent = this.load();
        this.rootScope = parent == null;
        this.logger.debug(Messages.openingThreadScope(), this.threadKey, this.rootScope);

        this.current = parent!=null? parent : factory.getFactory();

        if (this.current == null) {
            throw new ArgumentException(Messages.badFactoryResult(), "factory");
        }

        if (this.rootScope) {
            this.store(this.current);
        }
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
	
    @Override
    public void dispose() {
        this.dispose(true);
        GC.suppressFinalize(this);
    }

    protected void dispose(boolean disposing) {
        if (!disposing || this.disposed) {
            return;
        }

        this.logger.debug(Messages.disposingThreadScope(), this.rootScope);
        this.disposed = true;
        if (!this.rootScope) {
            return;
        }

        this.logger.verbose(Messages.cleaningRootThreadScope());
        this.store(null);

        IDisposable resource = (IDisposable)this.current;
        if (resource == null) {
            return;
        }

        this.logger.verbose(Messages.disposingRootThreadScopeResources());
        try {
            resource.dispose();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private T load() {
        if (this.context != null) {
            return (T)this.context.getItems().get(this.threadKey);
        }
        T object;
        object = (T)Thread.getData(Thread.getNamedDataSlot(this.threadKey));
        return object;
    }
    
    private void store(T value) {
        if (this.context != null) {
            this.context.getItems().put(this.threadKey, value);
        }
        else {
            Thread.setData(Thread.getNamedDataSlot(this.threadKey), value);
        }
    }
                
    public T getCurrent() {
        return this.current;
    }

}
