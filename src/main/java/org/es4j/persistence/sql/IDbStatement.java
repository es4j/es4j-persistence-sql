package org.es4j.persistence.sql;

import org.es4j.dotnet.IDisposable;
import org.es4j.dotnet.data.IDataRecord;
import org.es4j.persistence.sql.SqlDialects.NextPageDelegate;

//using System;
//using System.Collections.Generic;
//using System.Data;
//using SqlDialects;

public interface IDbStatement extends IDisposable {
    
    void addParameter(String name, Object value);

    int executeNonQuery(String commandText);
    int executeWithoutExceptions(String commandText);

    Object executeScalar(String commandText);

    int  getPageSize(); // { get; set; }
    void setPageSize(int pageSize);

    Iterable<IDataRecord> executeWithQuery(String queryText);
    Iterable<IDataRecord> executePagedQuery(String queryText, NextPageDelegate nextpage);
}