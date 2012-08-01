package org.es4j.persistence.sql.SqlDialects;

//namespace EventStore.Persistence.SqlPersistence.SqlDialects
import org.es4j.util.DateTime;
import org.es4j.util.Guid;

//using System;
public class MySqlDialect extends CommonSqlDialect {

    private static final int uniqueKeyViolation = 1062;

    public String initializeStorage() {
        return MySqlStatements.initializeStorage();
    }

    public String AppendSnapshotToCommit() {
        return base.AppendSnapshotToCommit.Replace("/*FROM DUAL*/", "FROM DUAL");
    }

    public String MarkCommitAsDispatched() {
        return base.MarkCommitAsDispatched.Replace("1", "true");
    }

    public String GetUndispatchedCommits() {
        return super.getUndispatchedCommits().replace("0", "false");
    }

    public Object CoalesceParameterValue(Object value) {
        if (value.getClass().equals(Guid.class)) {
            return ((Guid) value).toByteArray();
        }

        if (value.getClass().equals(DateTime.class))  {

				return ((DateTime) value).getTicks();
        }

        return value;
    }

    public boolean isDuplicate(Exception exception) {
        var property = exception.GetType().GetProperty("Number");
        return UniqueKeyViolation == (int) property.GetValue(exception, null);
    }
}
}