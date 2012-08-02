package org.es4j.persistence.sql.SqlDialects;

import org.es4j.dotnet.data.IDbConnection;
import org.es4j.dotnet.data.IDbTransaction;
import org.es4j.dotnet.data.TransactionScope;
import org.es4j.dotnet.data.TransactionScopeOption;
import org.es4j.persistence.sql.IDbStatement;
import org.es4j.persistence.sql.ISqlDialect;

//namespace EventStore.Persistence.SqlPersistence.SqlDialects
//using System;
//using System.Data;
//using System.Transactions;


public abstract class CommonSqlDialect implements ISqlDialect {

    @Override
    public abstract String initializeStorage();

    @Override
    public String purgeStorage() {
        return CommonSqlStatements.purgeStorage();
    }

    @Override
    public String getCommitsFromStartingRevision() {
        return CommonSqlStatements.getCommitsFromStartingRevision();
    }

    @Override
    public String getCommitsFromInstant() {

        return CommonSqlStatements.getCommitsFromInstant();
    }

    @Override
    public String persistCommit() {

        return CommonSqlStatements.persistCommit();
    }

    @Override
    public String duplicateCommit() {

        return CommonSqlStatements.duplicateCommit();
    }

    @Override
    public String getStreamsRequiringSnapshots() {

        return CommonSqlStatements.getStreamsRequiringSnapshots();
    }

    @Override
    public String getSnapshot() {
        return CommonSqlStatements.getSnapshot();
    }

    @Override
    public String appendSnapshotToCommit() {
        return CommonSqlStatements.appendSnapshotToCommit();
    }

    @Override
    public String getUndispatchedCommits() {
        return CommonSqlStatements.getUndispatchedCommits();
    }

    @Override
    public String markCommitAsDispatched() {
        return CommonSqlStatements.markCommitAsDispatched();
    }

    @Override
    public String streamId() {
        return "@StreamId";
    }

    @Override
    public String streamRevision() {
        return "@StreamRevision";
    }

    /**
     *
     * @return
     */
    @Override
    public String maxStreamRevision() {
        return "@MaxStreamRevision";
    }

    @Override
    public String items() {
        return "@Items";
    }

    @Override
    public String commitId() {
        return "@CommitId";
    }

    @Override
    public String commitSequence() {
        return "@CommitSequence";
    }

    @Override
    public String commitStamp() {
        return "@CommitStamp";
    }

    @Override
    public String headers() {
        return "@Headers";
    }

    @Override
    public String payload() {
        return "@Payload";
    }

    @Override
    public String threshold() {
        return "@Threshold";
    }

    @Override
    public String limit() {
        return "@Limit";
    }

    @Override
    public String skip() {
        return "@Skip";
    }

    @Override
    public boolean canPage() {
        return true;
    }

    @Override
    public Object coalesceParameterValue(Object value) {
        return value;
    }

    @Override
    public boolean isDuplicate(Exception exception) {
        String message = exception.getMessage().toUpperCase();
        return message.contains("DUPLICATE") || message.contains("UNIQUE") || message.contains("CONSTRAINT");
    }

    /**
     *
     * @param connection
     * @return
     */
    @Override
    public IDbTransaction openTransaction(IDbConnection connection) {
        return null;
    }

    @Override
    public IDbStatement buildStatement(TransactionScope scope, 
                                       IDbConnection    connection, 
                                       IDbTransaction   transaction) {
        return new CommonDbStatement(this, scope, connection, transaction);
    }
}
