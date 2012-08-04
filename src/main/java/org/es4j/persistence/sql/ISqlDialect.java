package org.es4j.persistence.sql;

import org.es4j.dotnet.data.IDbConnection;
import org.es4j.dotnet.data.IDbTransaction;
import org.es4j.dotnet.data.TransactionScope;

//using System;
//using System.Data;
//using System.Transactions;


public interface ISqlDialect {

    String initializeStorage(); // { get; }
    String purgeStorage();      // { get; }
    
    String getCommitsFromStartingRevision(); // { get; }
    String getCommitsFromInstant();          // { get; }
    
    String persistCommit();   // { get; }
    String duplicateCommit(); // { get; }
    
    String getStreamsRequiringSnapshots(); // { get; }
    String getSnapshot();                  // { get; }
    String appendSnapshotToCommit();       // { get; }
    
    String getUndispatchedCommits(); // { get; }
    String markCommitAsDispatched(); // { get; }
    
    String streamId();          // { get; }
    String streamRevision();    // { get; }
    String maxStreamRevision(); // { get; }
    String items();             // { get; }
    String commitId(); // { get; }
    String commitSequence(); // { get; }
    String commitStamp(); // { get; }
    String headers(); // { get; }
    String payload(); // { get; }
    String threshold(); // { get; }
    
    String limit();    // { get; }
    String skip();     // { get; }
    boolean canPage(); // { get; }
    
    Object coalesceParameterValue(Object value);
    
    IDbTransaction openTransaction(IDbConnection connection);
    IDbStatement buildStatement(TransactionScope scope, 
                                IDbConnection    connection, 
                                IDbTransaction   transaction);
    
    boolean isDuplicate(Exception exception);
}
