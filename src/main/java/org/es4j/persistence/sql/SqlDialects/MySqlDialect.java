package org.es4j.persistence.sql.SqlDialects;

import org.es4j.util.DateTime;
import org.es4j.util.Guid;

//namespace EventStore.Persistence.SqlPersistence.SqlDialects
//using System;

public class MySqlDialect extends CommonSqlDialect {

    private static final int uniqueKeyViolation = 1062;

    @Override
    public String initializeStorage() {
        return MySqlStatements.initializeStorage();
    }

    @Override
    public String appendSnapshotToCommit() {
        return super.appendSnapshotToCommit().replace("/*FROM DUAL*/", "FROM DUAL");
    }

    @Override
    public String markCommitAsDispatched() {
        return super.markCommitAsDispatched().replace("1", "true");
    }

    @Override
    public String getUndispatchedCommits() {
        return super.getUndispatchedCommits().replace("0", "false");
    }

    @Override
    public Object coalesceParameterValue(Object value) {
        if (value.getClass().equals(Guid.class)) {
            return ((Guid) value).toByteArray();
        }
        if (value.getClass().equals(DateTime.class)) {
            return ((DateTime) value).getTicks();
        }
        return value;
    }
    
    @Override
    public boolean isDuplicate(Exception exception) {
        //var property = exception.getType().getProperty("Number");
        //return uniqueKeyViolation == (int) property.getValue(exception, null);
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
