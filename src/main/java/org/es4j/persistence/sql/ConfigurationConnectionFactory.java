package org.es4j.persistence.sql;

import org.es4j.dotnet.ConfigurationErrorsException;
import org.es4j.dotnet.ConnectionStringSettings;
import org.es4j.dotnet.Dictionary;
import org.es4j.dotnet.IDictionary;
import org.es4j.dotnet.IDisposable;
import org.es4j.dotnet.KeyValuePair;
import org.es4j.dotnet.data.DbProviderFactory;
import org.es4j.dotnet.data.IDbConnection;
import org.es4j.util.Guid;
import org.es4j.util.StringExt;

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
        return openScope(Guid.getEmpty(), settings.getKey());
    }
    
    public static IDisposable openScope(String connectionName) {
        return openScope(Guid.getEmpty(), connectionName);
    }
    
    public static IDisposable openScope(Guid streamId, String connectionName) {
        ConfigurationConnectionFactory factory = new ConfigurationConnectionFactory(connectionName);
        return factory.open(streamId, connectionName);
    }

    public ConnectionStringSettings getSettings() {
        return this.getConnectionStringSettings(this.masterConnectionName); 
    }

    public IDbConnection openMaster(Guid streamId) {
        logger.verbose(Messages.openingMasterConnection(), this.masterConnectionName);
        return this.open(streamId, this.masterConnectionName);
    }

    public IDbConnection openReplica(Guid streamId) {
        logger.verbose(Messages.openingReplicaConnection(), this.replicaConnectionName);
        return this.open(streamId, this.replicaConnectionName);
    }
    
    protected IDbConnection open(Guid streamId, String connectionName) {
        ConnectionStringSettings  setting = this.getSetting(connectionName);
        String connectionString = this.buildConnectionString(streamId, setting);
        return new ConnectionScope(connectionString, () => this.Open(connectionString, setting));
    }
    
    protected IDbConnection open(String connectionString, ConnectionStringSettings setting) {
        DbProviderFactory factory = this.getFactory(setting);
        var connection = factory.createConnection();
        if (connection == null) {
            throw new ConfigurationErrorsException(Messages.badConnectionName());
        }
        connection.ConnectionString = connectionString;

        try {
            logger.verbose(Messages.openingConnection(), setting.getName());
            connection.Open();
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
            //if (cachedSettings.tryGetValue(connectionName, out setting)) {
            //    return setting;
            //}
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
                
    protected ConnectionStringSettings getConnectionStringSettings(string connectionName) {
        logger.debug(Messages.discoveringConnectionSettings(), connectionName);

        var settings = ConfigurationManager.getConnectionStrings()
				.Cast<ConnectionStringSettings>()
				.FirstOrDefault(x => x.getName() == connectionName);

        if (settings == null) {
            throw new ConfigurationErrorsException(Messages.connectionNotFound().formatWith(connectionName));
        }
        if ((settings.getConnectionString()!=null? settings.getConnectionString() : string.Empty).Trim().Length == 0) {
            throw new ConfigurationErrorsException(Messages.missingConnectionString().formatWith(connectionName));
        }
        if ((settings.getProviderName()!=null? settings.getProviderName() : string.Empty).Trim().Length == 0) {
            throw new ConfigurationErrorsException(Messages.missingProviderName().formatWith(connectionName));
        }
        return settings;
    }

    protected String buildConnectionString(Guid streamId, ConnectionStringSettings setting) {
        if (this.shards == 0) {
            return setting.getConnectionString();
        }
        logger.verbose(Messages.embeddingShardKey(), setting.getName());
        
        return StringExt.formatWith(setting.getConnectionString(), this.computeHashKey(streamId));
                        
        //return setting.getConnectionString().formatWith(this.computeHashKey(streamId));
    }
    
    protected String computeHashKey(Guid streamId) {
        // simple sharding scheme which could easily be improved through such techniques
        // as consistent hashing (Amazon Dynamo) or other kinds of sharding.
        return (this.shards == 0 ? 0 : streamId.toByteArray()[0] % this.shards).toString();
    }
}
