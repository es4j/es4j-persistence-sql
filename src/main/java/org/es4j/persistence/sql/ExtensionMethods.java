package org.es4j.persistence.sql;

import java.util.UUID;
import org.es4j.dotnet.data.IDataParameter;
import org.es4j.dotnet.data.IDbCommand;
import org.es4j.util.DateTime;
import org.es4j.util.Guid;
import org.es4j.util.logging.ILog;
import org.es4j.util.logging.LogFactory;

//namespace EventStore.Persistence.SqlPersistence
//using System;
//using System.Data;
//using Logging;


public class ExtensionMethods {
    
    private static final ILog logger = LogFactory.buildLogger(ExtensionMethods.class);

    public static UUID toGuid(/*this*/Object value) {
        if (value instanceof UUID) {
            return (UUID) value;
        }

        byte[] bytes = value as byte[];
        return bytes != null ? new Guid(bytes) : Guid.Empty;
    }

    public static int toInt(/*this*/Object value) {
        return value instanceof long ? (int) (long) value : (int) value;
    }

    public static DateTime toDateTime(/*this*/Object value) {
        value = value instanceof decimal ? (long) (decimal) value : value;
        return value instanceof long ? new DateTime((long) value) : (DateTime) value;
    }

    public static IDbCommand setParameter(/*this*/IDbCommand command, String name, Object value) {
        logger.verbose("Rebinding parameter '{0}' with value: {1}", name, value);
        IDataParameter parameter = (IDataParameter) command.Parameters[name];
        parameter.getValue = value;
        return command;
    }
}
