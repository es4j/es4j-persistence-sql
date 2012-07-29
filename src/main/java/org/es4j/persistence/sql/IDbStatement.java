package org.es4j.persistence.sql;

//using System;

import org.es4j.dotnet.data.IDataRecord;
import org.es4j.dotnet.IDisposable;
import org.es4j.dotnet.IEnumerable;
import org.es4j.perseistence.sql.sqldialects.NextPageDelegate;

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

    IEnumerable<IDataRecord> executeWithQuery(String queryText);
    IEnumerable<IDataRecord> executePagedQuery(String queryText, NextPageDelegate nextpage);
}