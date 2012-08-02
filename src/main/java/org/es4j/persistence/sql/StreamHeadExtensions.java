package org.es4j.persistence.sql;

import java.util.UUID;
import org.es4j.dotnet.data.IDataRecord;
import org.es4j.eventstore.api.persistence.StreamHead;

//namespace EventStore.Persistence.SqlPersistence
//using System.Data;
//using Persistence;


public class StreamHeadExtensions {

    private static final int streamIdIndex         = 0;
    private static final int headRevisionIndex     = 1;
    private static final int snapshotRevisionIndex = 2;

    public static StreamHead getStreamToSnapshot(/*this*/IDataRecord record) {
        return new StreamHead(
                ExtensionMethods.toGuid(record.get(streamIdIndex)),
                ExtensionMethods.toInt(record.get(headRevisionIndex)),
                ExtensionMethods.toInt(record.get(snapshotRevisionIndex)));
    }
}
