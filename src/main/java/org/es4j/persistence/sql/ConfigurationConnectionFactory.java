package org.es4j.persistence.sql;

import java.util.UUID;
import org.es4j.dotnet.ConfigurationErrorsException;
import org.es4j.dotnet.ConfigurationManager;
import org.es4j.dotnet.Dictionary;
import org.es4j.dotnet.IDictionary;
import org.es4j.dotnet.IDisposable;
import org.es4j.dotnet.KeyValuePair;
import org.es4j.dotnet.data.ConnectionStringSettings;
import org.es4j.dotnet.data.DbProviderFactory;
import org.es4j.dotnet.data.IDbConnection;
import org.es4j.util.Consts;
import org.es4j.util.StringExt;
import org.es4j.util.UUIDExt;
import org.es4j.util.logging.ILog;
import org.es4j.util.logging.LogFactory;

//using System;
//using System.Collections.Generic;
//using System.Configuration;
//using System.Data;
//using System.Data.Common;
//using System.Linq;
//using Logging;

public class ConfigurationConnectionFactory implements IConnectionFactory {
    private static final int defaultShards = 16;
    private static final String defaultConnectionName = "EventStore";

    private static final ILog logger = LogFactory.buildLogger(ConfigurationConnectionFactory.class);
    private static final IDictionary<String, ConnectionStringSettings> cachedSettings  = new Dictionary<String, ConnectionStringSettings>();
    private static final IDictionary<String, DbProviderFactory>        cachedFactories = new Dictionary<String, DbProviderFactory>();

    private final String masterConnectionName;
    private final String replicaConnectionName;
    private final int shards;

    public ConfigurationConnectionFactory(String connectionName) {
        this(connectionName, connectionName, defaultShards);
    }
    
    public ConfigurationConnectionFactory(String masterConnectionName, 
                                         String replicaConnectionName, 
                                         int shards) {
        this.masterConnectionName  = masterConnectionName !=null? masterConnectionName : defaultConnectionName;
        this.replicaConnectionName = replicaConnectionName!=null? replicaConnectionName : this.masterConnectionName;
        this.shards = shards >= 0 ? shards : defaultShards;

        logger.debug(Messages.configuringConnections(), this.masterConnectionName, this.replicaConnectionName, this.shards);
    }

    public static IDisposable openScope() {
        KeyValuePair<String, ConnectionStringSettings> settings = cachedSettings.firstOrDefault();
        if (StringExt.isNullOrEmpty(settings.getKey())) {
            throw new ConfigurationErrorsException(Messages.notConnectionsAvailable());
        }
        return openScope(Consts.EMPTY_UUID, settings.getKey());
    }
    
    public static IDisposable openScope(String connectionName) {
        return openScope(Consts.EMPTY_UUID, connectionName);
    }
    
    public static IDisposable openScope(UUID streamId, String connectionName) {
        ConfigurationConnectionFactory factory = new ConfigurationConnectionFactory(connectionName);
        return factory.open(streamId, connectionName);
    }

    public ConnectionStringSettings getSettings() {
        return this.getConnectionStringSettings(this.masterConnectionName); 
    }

    @Override
    public IDbConnection openMaster(UUID streamId) {
        logger.verbose(Messages.openingMasterConnection(), this.masterConnectionName);
        return this.open(streamId, this.masterConnectionName);
    }

    @Override
    public IDbConnection openReplica(UUID streamId) {
        logger.verbose(Messages.openingReplicaConnection(), this.replicaConnectionName);
        return this.open(streamId, this.replicaConnectionName);
    }
    
    protected IDbConnection open(UUID streamId, String connectionName) {
        final ConnectionStringSettings  setting = this.getSetting(connectionName);
        final String connectionString = this.buildConnectionString(streamId, setting);
        return new ConnectionScope(connectionString, new FactoryDelegate<IDbConnection>() {

            @Override
            public IDbConnection getFactory() {
                return open(connectionString, setting);
            }
        });
    }
    
    protected IDbConnection open(String connectionString, ConnectionStringSettings setting) {
        DbProviderFactory factory = this.getFactory(setting);
        IDbConnection connection = factory.createConnection();
        if (connection == null) {
            throw new ConfigurationErrorsException(Messages.badConnectionName());
        }
        connection.setConnectionString(connectionString);

        try {
            logger.verbose(Messages.openingConnection(), setting.getName());
            connection.open();
        }
        catch (Exception e) {
            logger.warn(Messages.openFailed(), setting.getName());
            throw new StorageUnavailableException(e.getMessage(), e);
        }
        return connection;
    }

    protected ConnectionStringSettings getSetting(String connectionName) {
        synchronized (cachedSettings) {
            ConnectionStringSettings setting;
            if(cachedSettings.contains(connectionName)) {
                setting = cachedSettings.get(connectionName);
                return setting;
            }
            setting = this.getConnectionStringSettings(connectionName);
            cachedSettings.put(connectionName, setting);
            return setting;
        }
    }
                
    protected DbProviderFactory getFactory(ConnectionStringSettings setting) {
        synchronized (cachedFactories) {
            DbProviderFactory factory;
            if (cachedFactories.contains(setting.getName())) {
                factory = cachedFactories.get(setting.getName());
                return factory;
            }
            factory = DbProviderFactories.getFactory(setting.getProviderName());
            logger.debug(Messages.discoveredConnectionProvider(), setting.getName(), factory.getClass().getName());
            cachedFactories.put(setting.getName(), factory);
            return factory;
        }
    }
                
    protected ConnectionStringSettings getConnectionStringSettings(String connectionName) {
        logger.debug(Messages.discoveringConnectionSettings(), connectionName);

        Iterable<ConnectionStringSettings> settingsCollection = ConfigurationManager.getConnectionStrings();
        ConnectionStringSettings settings = null;
        for(ConnectionStringSettings stngs : settingsCollection) {
            if(stngs.getName() == connectionName) {
                settings = stngs;
                break;
            }
        }
        if (settings == null) {
            String msg = StringExt.formatWith(Messages.connectionNotFound(), connectionName);
            throw new ConfigurationErrorsException(msg);
        }
        if ((settings.getConnectionString()!=null?settings.getConnectionString() : StringExt.Empty()).trim().length() == 0) {
            throw new ConfigurationErrorsException(StringExt.formatWith(Messages.missingConnectionString(), connectionName));
        }
        if ((settings.getProviderName()!=null? settings.getProviderName() : StringExt.Empty()).trim().length() == 0) {
            throw new ConfigurationErrorsException(StringExt.formatWith(Messages.missingProviderName(),connectionName));
        }
        return settings;
    }

    protected String buildConnectionString(UUID streamId, ConnectionStringSettings setting) {
        if (this.shards == 0) {
            return setting.getConnectionString();
        }
        logger.verbose(Messages.embeddingShardKey(), setting.getName());
        
        return StringExt.formatWith(setting.getConnectionString(), this.computeHashKey(streamId));
    }
    
    protected String computeHashKey(UUID streamId) {
        // simple sharding scheme which could easily be improved through such techniques
        // as consistent hashing (Amazon Dynamo) or other kinds of sharding.
        return new Integer(this.shards == 0 ? 0 : UUIDExt.toByteArray(streamId)[0] % this.shards).toString();
    }
}
