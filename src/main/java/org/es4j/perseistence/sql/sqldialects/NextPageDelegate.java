package org.es4j.perseistence.sql.sqldialects;

import org.es4j.dotnet.data.IDataRecord;
import org.es4j.dotnet.data.IDbCommand;

//using System.Data;
//public delegate void NextPageDelegate(IDbCommand command, IDataRecord current);

public abstract class NextPageDelegate {

    public abstract void nextPage(IDbCommand command, IDataRecord current);

}
