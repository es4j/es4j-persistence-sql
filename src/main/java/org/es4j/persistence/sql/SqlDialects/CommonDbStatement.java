package org.es4j.persistence.sql.SqlDialects;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.es4j.dotnet.GC;
import org.es4j.dotnet.IDisposable;
import org.es4j.dotnet.data.DBNull;
import org.es4j.dotnet.data.DbType;
import org.es4j.dotnet.data.IDataParameter;
import org.es4j.dotnet.data.IDataRecord;
import org.es4j.dotnet.data.IDbCommand;
import org.es4j.dotnet.data.IDbConnection;
import org.es4j.dotnet.data.IDbTransaction;
import org.es4j.dotnet.data.TransactionScope;
import org.es4j.persistence.sql.IDbStatement;
import org.es4j.persistence.sql.ISqlDialect;
import org.es4j.persistence.sql.Messages;
import org.es4j.persistence.sql.UniqueKeyViolationException;
import org.es4j.util.logging.ILog;
import org.es4j.util.logging.LogFactory;

//namespace EventStore.Persistence.SqlPersistence.SqlDialects
//using System;
//using System.Collections.Generic;
//using System.Data;
//using System.Transactions;
//using Logging;

public class CommonDbStatement implements IDbStatement {

    private static final int infinitePageSize = 0;
    private static final ILog logger = LogFactory.buildLogger(CommonDbStatement.class);
    
    private final ISqlDialect      dialect;
    private final TransactionScope scope;
    private final IDbConnection    connection;
    private final IDbTransaction   transaction;
    private       int              pageSize;    // { get; set; }
    private Map<String, Object>    parameters;  // { get; private set; }

    public Map<String, Object> getParameters() {
        return parameters;
    }
    
    private void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public CommonDbStatement(ISqlDialect      dialect,
                               TransactionScope scope,
                               IDbConnection    connection,
                               IDbTransaction   transaction)
    {
        this.parameters = new HashMap<String, Object>();

        this.dialect     = dialect;
        this.scope       = scope;
        this.connection  = connection;
        this.transaction = transaction;
    }

    @Override
    public void close() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
	
    @Override
    public void dispose() {
        this.dispose(true);
        GC.suppressFinalize(this);
    }

    protected void dispose(boolean disposing) {
        logger.verbose(Messages.disposingStatement());

        if (this.transaction != null) {
            try {
                this.transaction.dispose();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        if (this.connection != null) {
            try {
                this.connection.dispose();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        if (this.scope != null) {
            this.scope.dispose();
        }
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }
    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public void addParameter(String name, Object value) {
        logger.debug(Messages.addingParameter(), name);
        this.parameters.put(name, this.dialect.coalesceParameterValue(value));
    }

    @Override
    public int executeWithoutExceptions(String commandText) {
        try {
            return this.executeNonQuery(commandText);
        } catch (Exception ex) {
            logger.debug(Messages.exceptionSuppressed());
            return 0;
        }
    }

    @Override
    public int executeNonQuery(String commandText) {
        try (IDbCommand command = this.buildCommand(commandText)) {
            //using (var command = this.BuildCommand(commandText))
            return command.executeNonQuery();
        } catch (Exception e) {
            if (this.dialect.isDuplicate(e)) {
                throw new UniqueKeyViolationException(e.getMessage(), e);
            }

            throw new RuntimeException();
        }
    }

    @Override
    public Object executeScalar(String commandText) {
        try (IDbCommand command = this.buildCommand(commandText)) {
            return command.executeScalar();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public Iterable<IDataRecord> executeWithQuery(String queryText) {
        return this.executeQuery(queryText, new NextPageDelegate(){

            @Override
            public void nextPage(IDbCommand query, IDataRecord latest) {
            }
        }, infinitePageSize);
    }

    @Override
    public Iterable<IDataRecord> executePagedQuery(String queryText, NextPageDelegate nextpage) {
        int pageSize = this.dialect.canPage() ? this.pageSize : infinitePageSize;
        if (pageSize > 0) {
            logger.verbose(Messages.maxPageSize(), pageSize);
            this.getParameters().put(this.dialect.limit(), pageSize);
        }

        return this.executeQuery(queryText, nextpage, pageSize);
    }
       
    protected Iterable<IDataRecord> executeQuery(String queryText, NextPageDelegate nextpage, int pageSize) {
        this.getParameters().put(this.dialect.skip(), 0);
        IDbCommand command = this.buildCommand(queryText);

        try {
            return new PagedEnumerationCollection(this.scope, 
                                                  this.dialect, 
                                                  command, 
                                                  nextpage, 
                                                  pageSize,
                                                  Arrays.asList((IDisposable)this));
        } catch (Exception ex) {
            try {
                command.dispose();
            } catch (Exception ex1) {
                throw new RuntimeException(ex1);
            }
            throw new RuntimeException(ex);
        }
    }
       
    protected IDbCommand buildCommand(String statement) {

        logger.verbose(Messages.creatingCommand());
        IDbCommand command = this.connection.createCommand();
        command.setTransaction(this.transaction);
        command.setCommandText(statement);

        logger.verbose(Messages.clientControlledTransaction(), this.transaction != null);
        logger.verbose(Messages.commandTextToExecute(), statement);

        this.buildParameters(command);

        return command;
    }

    protected final void buildParameters(IDbCommand command) {
        for (Entry<String,Object> item : this.getParameters().entrySet()) {
            this.buildParameter(command, item.getKey(), item.getValue());
        }
    }
    
    protected void buildParameter(IDbCommand command, String name, Object value) {
        IDataParameter parameter = command.createParameter();
        parameter.setParameterName(name);
        this.setParameterValue(parameter, value, null);

        logger.verbose(Messages.bindingParameter(), name, parameter.getValue());
        command.getParameters().add(parameter);
    }
    
    protected void setParameterValue(IDataParameter param, Object value, DbType type) {
        param.setValue(value!=null? value : DBNull.getValue());
        param.setDbType(type!=null?type : (value == null ? DbType.Binary : param.getDbType()));
    }
}