package org.es4j.persistence.sql;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.es4j.dotnet.data.DBNull;
import org.es4j.dotnet.data.IDataRecord;
import org.es4j.eventstore.api.Commit;
import org.es4j.messaging.api.EventMessage;
import org.es4j.serialization.api.ISerialize;
import org.es4j.util.GenericType;
//import org.es4j.util.Guid;
import org.es4j.util.logging.ILog;
import org.es4j.util.logging.LogFactory;

//namespace EventStore.Persistence.SqlPersistence
//using System;
//using System.Collections.Generic;
//using System.Data;
//using Logging;
//using Serialization;
public class CommitExtensions {

    private static final int streamIdIndex       = 0;
    private static final int streamRevisionIndex = 1;
    private static final int commitIdIndex       = 2;
    private static final int commitSequenceIndex = 3;
    private static final int commitStampIndex    = 4;
    private static final int headersIndex        = 5;
    private static final int payloadIndex        = 6;
    
    private static final ILog logger = LogFactory.buildLogger(CommitExtensions.class);

    public static Commit getCommit(/*this*/IDataRecord record, ISerialize serializer) {
        logger.verbose(Messages.deserializingCommit(), serializer.getClass());
        
        GenericType<Map<String,Object>> mapType = new GenericType<Map<String,Object>>(){};
        Map<String,Object> headers = serializer.deserialize/*<Map< String,Object>>*/(record, headersIndex);
        
        GenericType<List<EventMessage>> listType = new GenericType<List<EventMessage>>(){};
        List<EventMessage> events = serializer.deserialize/*<List<EventMessage>>*/(record, payloadIndex);

        return new Commit(ExtensionMethods.toGuid(record.get(streamIdIndex)),
                ExtensionMethods.toInt(record.get(streamRevisionIndex)),
                ExtensionMethods.toGuid(record.get(commitIdIndex)),
                ExtensionMethods.toInt(record.get(commitSequenceIndex)),
                ExtensionMethods.toDateTime(record.get(commitStampIndex)),
                headers,
                events);
    }

    public static UUID StreamId(/*this*/IDataRecord record) {
        return ExtensionMethods.toGuid(record.get(streamIdIndex));
    }

    public static int commitSequence(/*this*/IDataRecord record) {
        return ExtensionMethods.toInt(record.get(commitSequenceIndex));
    }

    public static <T> T deserialize(/*this*/ISerialize serializer, GenericType type, IDataRecord record, int index) {
        if (index >= record.getFieldCount()) {
            return null; //default(T);
        }

        T value = (T) record.get(index);
        if (value == null || value == DBNull.getValue()) {
            return null; //default(T);
        }

        byte[] bytes = (byte[]) value;
        return bytes.length == 0 ? null/*default(T)*/ : serializer.deserialize(bytes, type);
    }
}
