package org.es4j.persistence.sql;

import java.util.List;
import org.es4j.dotnet.data.IDataRecord;
import org.es4j.eventstore.api.Commit;
import org.es4j.messaging.api.EventMessage;
import org.es4j.serialization.api.ISerialize;
import org.es4j.util.Guid;
import org.es4j.util.logging.ILog;
import org.es4j.util.logging.LogFactory;

//namespace EventStore.Persistence.SqlPersistence
//using System;
//using System.Collections.Generic;
//using System.Data;
//using Logging;
//using Serialization;

public class CommitExtensions {
    
    private static final int StreamIdIndex = 0;
    private static final int StreamRevisionIndex = 1;
    private static final int CommitIdIndex = 2;
    private static final int CommitSequenceIndex = 3;
    private static final int CommitStampIndex = 4;
    private static final int HeadersIndex = 5;
    private static final int PayloadIndex = 6;
    
    private static final ILog logger = LogFactory.buildLogger(CommitExtensions.class);

    public static Commit getCommit(/*this*/ IDataRecord record, ISerialize serializer) {
        logger.verbose(Messages.deserializingCommit(), serializer.getClass());
        Map<String, Object> headers = serializer.deserialize<Map<String, Object>>(record, headersIndex);
        List<EventMessage>  events = serializer.deserialize<List<EventMessage>>(record, PayloadIndex);

        return new Commit(record[StreamIdIndex].toGuid(),
                          record[StreamRevisionIndex].toInt(),
                          record[CommitIdIndex].toGuid(),
                          record[CommitSequenceIndex].toInt(),
                          record[CommitStampIndex].toDateTime(),
                          headers,
                          events);
    }

		public static Guid StreamId(this IDataRecord record)
		{
			return record[StreamIdIndex].ToGuid();
		}
                
		public static int CommitSequence(this IDataRecord record)
		{
			return record[CommitSequenceIndex].ToInt();
		}

    public static <T> T deserialize/*<T>*/(/*this*/ ISerialize serializer, IDataRecord record, int index) {
			if (index >= record.FieldCount)
				return null; //default(T);

			T value = record[index];
			if (value == null || value == DBNull.Value)
				return null; //default(T);

			byte[] bytes = (byte[])value;
			return bytes.length == 0 ? null/*default(T)*/ : serializer.deserialize<T>(bytes);
    }
}
