package org.es4j.persistence.sql;

import org.es4j.dotnet.data.IDataRecord;
import org.es4j.eventstore.api.Snapshot;
import org.es4j.serialization.api.ISerialize;
import org.es4j.util.logging.ILog;
import org.es4j.util.logging.LogFactory;

//namespace EventStore.Persistence.SqlPersistence
//using System.Data;
//using Logging;
//using Serialization;

public class SnapshotExtensions {
    
    private static final int  streamIdIndex       = 0;
    private static final int  streamRevisionIndex = 1;
    private static final int  payloadIndex        = 2;
    
    private static final ILog logger = LogFactory.buildLogger(SnapshotExtensions.class);

    public static Snapshot getSnapshot(/*this*/ IDataRecord record, ISerialize<Object> serializer) {
        logger.verbose(Messages.deserializingSnapshot());

        return new Snapshot(record[streamIdIndex].toGuid(),
			    record[streamRevisionIndex].toInt(),
			    serializer.deserialize/*<Object>*/(record, payloadIndex));
    }
}