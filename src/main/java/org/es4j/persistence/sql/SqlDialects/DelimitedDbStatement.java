package org.es4j.persistence.sql.SqlDialects;

import java.util.LinkedList;
import java.util.List;
import org.es4j.dotnet.data.IDbConnection;
import org.es4j.dotnet.data.IDbTransaction;
import org.es4j.dotnet.data.TransactionScope;
import org.es4j.persistence.sql.ISqlDialect;
import org.es4j.util.StringExt;

//namespace EventStore.Persistence.SqlPersistence.SqlDialects
//using System;
//using System.Collections.Generic;
//using System.Data;
//using System.Linq;
//using System.Transactions;


public class DelimitedDbStatement extends CommonDbStatement {

    private static final String delimiter = ";";

    public DelimitedDbStatement(
            ISqlDialect dialect,
            TransactionScope scope,
            IDbConnection connection,
            IDbTransaction transaction) {
        super(dialect, scope, connection, transaction);
    }

    @Override
    public int executeNonQuery(String commandText) {
        int sum = 0;
        for(String command : splitCommandText(commandText)) {
            sum += super.executeNonQuery(command);
        }
        return sum;
    }
    
    private static Iterable<String> splitCommandText(String delimited) {
        if (StringExt.isNullOrEmpty(delimited)) {
            return new LinkedList<String>();
        }
        List<String> parts = new LinkedList<String>();
        for (String part : delimited.split(delimited)) {
            if (!part.trim().isEmpty()) {
                parts.add(part + delimiter);
            }
        }
        return parts;
    }
}