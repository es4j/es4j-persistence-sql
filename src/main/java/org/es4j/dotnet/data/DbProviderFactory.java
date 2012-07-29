package org.es4j.dotnet.data;

// Summary:
//     Represents a set of methods for creating instances of a provider's implementation
//     of the data source classes.
public abstract class DbProviderFactory {

        // Summary:
        //     Initializes a new instance of a System.Data.Common.DbProviderFactory class.
        protected DbProviderFactory() {
            
        }

        // Summary:
        //     Specifies whether the specific System.Data.Common.DbProviderFactory supports
        //     the System.Data.Common.DbDataSourceEnumerator class.
        //
        // Returns:
        //     true if the instance of the System.Data.Common.DbProviderFactory supports
        //     the System.Data.Common.DbDataSourceEnumerator class; otherwise false.
        public boolean canCreateDataSourceEnumerator; // { get; }
        public boolean isCanCreateDataSourceEnumerator() {
            return canCreateDataSourceEnumerator;
        }

        // Summary:
        //     Returns a new instance of the provider's class that implements the System.Data.Common.DbCommand
        //     class.
        //
        // Returns:
        //     A new instance of System.Data.Common.DbCommand.
        public DbCommand createCommand() {
            throw new UnsupportedOperationException("Not yet implemented");
        }
        //
        // Summary:
        //     Returns a new instance of the provider's class that implements the System.Data.Common.DbCommandBuilder
        //     class.
        //
        // Returns:
        //     A new instance of System.Data.Common.DbCommandBuilder.
        public DbCommandBuilder createCommandBuilder() {
            throw new UnsupportedOperationException("Not yet implemented");
        }
        //
        // Summary:
        //     Returns a new instance of the provider's class that implements the System.Data.Common.DbConnection
        //     class.
        //
        // Returns:
        //     A new instance of System.Data.Common.DbConnection.
        public DbConnection createConnection() {
            throw new UnsupportedOperationException("Not yet implemented");
        }
        
        //
        // Summary:
        //     Returns a new instance of the provider's class that implements the System.Data.Common.DbConnectionStringBuilder
        //     class.
        //
        // Returns:
        //     A new instance of System.Data.Common.DbConnectionStringBuilder.
        public DbConnectionStringBuilder createConnectionStringBuilder() {
            throw new UnsupportedOperationException("Not yet implemented");
        }
        
        //
        // Summary:
        //     Returns a new instance of the provider's class that implements the System.Data.Common.DbDataAdapter
        //     class.
        //
        // Returns:
        //     A new instance of System.Data.Common.DbDataAdapter.
        public DbDataAdapter createDataAdapter() {
            throw new UnsupportedOperationException("Not yet implemented");
        }
        
        //
        // Summary:
        //     Returns a new instance of the provider's class that implements the System.Data.Common.DbDataSourceEnumerator
        //     class.
        //
        // Returns:
        //     A new instance of System.Data.Common.DbDataSourceEnumerator.
        public DbDataSourceEnumerator createDataSourceEnumerator() {
            throw new UnsupportedOperationException("Not yet implemented");
        }
        
        //
        // Summary:
        //     Returns a new instance of the provider's class that implements the System.Data.Common.DbParameter
        //     class.
        //
        // Returns:
        //     A new instance of System.Data.Common.DbParameter.
        public DbParameter createParameter() {
            throw new UnsupportedOperationException("Not yet implemented");
        }
        //
        // Summary:
        //     Returns a new instance of the provider's class that implements the provider's
        //     version of the System.Security.CodeAccessPermission class.
        //
        // Parameters:
        //   state:
        //     One of the System.Security.Permissions.PermissionState values.
        //
        // Returns:
        //     A System.Security.CodeAccessPermission object for the specified System.Security.Permissions.PermissionState.
        public CodeAccessPermission createPermission(PermissionState state) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
}