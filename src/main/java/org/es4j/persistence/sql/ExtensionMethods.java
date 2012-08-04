package org.es4j.persistence.sql;

import java.math.BigDecimal;
import java.util.UUID;
import org.es4j.dotnet.data.IDataParameter;
import org.es4j.dotnet.data.IDbDataParameter;
import org.es4j.dotnet.data.IDbCommand;
import org.es4j.util.Consts;
import org.es4j.util.DateTime;
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

        byte[] bytes = (value instanceof byte[])?(byte[])value : null;
        //byte[] bytes = value as byte[];
        
        ;
        
        
        return bytes != null ? UUID.nameUUIDFromBytes(bytes) : Consts.EMPTY_UUID;
    }

    public static int toInt(/*this*/Object value) {
        return value instanceof Long ? (int) (long) value : (int) value;
    }

    public static DateTime toDateTime(/*this*/Object value) {
        value = value instanceof BigDecimal ? (long)value : value;
        return value instanceof Long ? new DateTime((long) value) : (DateTime) value;
    }

    public static IDbCommand setParameter(/*this*/ IDbCommand command, String name, Object value) {
            logger.verbose("Rebinding parameter '{0}' with value: {1}", name, value);
            IDataParameter parameter = (IDataParameter)command.getParameters().get(name);
            parameter.setValue(value);
            return command;
    }
}
