package org.es4j.persistence.sql;

import org.es4j.util.Guid;
import org.es4j.dotnet.data.IDbConnection;

//using System.Data;

public interface IConnectionFactory {

    IDbConnection openMaster(Guid streamId);
    IDbConnection openReplica(Guid streamId);
}