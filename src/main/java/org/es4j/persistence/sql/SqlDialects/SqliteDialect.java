package org.es4j.persistence.sql.SqlDialects;

//namespace EventStore.Persistence.SqlPersistence.SqlDialects
public class SqliteDialect extends CommonSqlDialect {

    @Override
    public String initializeStorage() {
        return SqliteStatements.initializeStorage();
    }

    // Sqlite wants all parameters to be a part of the query
    @Override
    public String getCommitsFromStartingRevision() {
        return super.getCommitsFromStartingRevision().replace("\n ORDER BY ", "\n  AND @Skip = @Skip\nORDER BY ");
    }
}

