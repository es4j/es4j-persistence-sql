package org.es4j.persistence.sql;

//using System;

import org.es4j.dotnet.data.IDbConnection;
import org.es4j.util.Guid;

//using System.Data;
public interface IConnectionFactory {

    IDbConnection openMaster(Guid streamId);
    IDbConnection openReplica(Guid streamId);
}