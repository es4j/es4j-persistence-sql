package org.es4j.persistence.sql;

import org.es4j.dotnet.GC;
import org.es4j.dotnet.HttpContext;
import org.es4j.dotnet.IDisposable;
import  org.es4j.dotnet.Thread;

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

    public ThreadScope(String key, Func<T> factory) {
        
        this.threadKey = typeof(ThreadScope<T>).Name + ":[{0}]".formatWith(key ?? string.Empty);

        T parent = this.load();
        this.rootScope = parent == null;
        this.logger.debug(Messages.openingThreadScope(), this.threadKey, this.rootScope);

        this.current = parent!=null? parent : factory();

        if (this.current == null) {
            throw new ArgumentException(Messages.badFactoryResult(), "factory");
        }

        if (this.rootScope) {
            this.store(this.current);
        }
    }

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
        resource.dispose();
    }

    private T load() {
        if (this.context != null) {
            return (T)this.context.Items[this.threadKey];
        }
        return (T)Thread.getData(Thread.getNamedDataSlot(this.threadKey));
    }
    
    private void store(T value) {
        if (this.context != null) {
            this.context.Items[this.threadKey] = value;
        }
        else {
            Thread.setData(Thread.getNamedDataSlot(this.threadKey), value);
        }
    }
                
    public T getCurrent() {
        return this.current;
    }
	
}
