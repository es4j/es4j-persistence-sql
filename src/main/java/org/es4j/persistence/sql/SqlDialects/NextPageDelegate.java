package org.es4j.persistence.sql.SqlDialects;

import org.es4j.dotnet.data.IDataRecord;
import org.es4j.dotnet.data.IDbCommand;

//namespace EventStore.Persistence.SqlPersistence.SqlDialects
//using System.Data;

public abstract class NextPageDelegate {
    public abstract void NextPageDelegate(IDbCommand command, IDataRecord current);
}
