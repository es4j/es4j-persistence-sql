package org.es4j.persistence.sql;

import java.util.UUID;
import org.es4j.dotnet.data.IDbConnection;

//using System.Data;

public interface IConnectionFactory {

    IDbConnection openMaster(UUID streamId);
    IDbConnection openReplica(UUID streamId);
}