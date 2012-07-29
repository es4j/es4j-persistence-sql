package org.es4j.persistence.sql;

//using System;

import org.es4j.dotnet.data.DbProviderFactory;

//using System.Data;

// Summary:
//     Represents a set of static methods for creating one or more instances of
//     System.Data.Common.DbProviderFactory classes.
public class DbProviderFactories {

    // Summary:
    //     Returns an instance of a System.Data.Common.DbProviderFactory.
    //
    // Parameters:
    //   providerRow:
    //     System.Data.DataRow containing the provider's configuration information.
    //
    // Returns:
    //     An instance of a System.Data.Common.DbProviderFactory for a specified System.Data.DataRow.
    //public static DbProviderFactory getFactory(DataRow providerRow) {
    //    throw new UnsupportedOperationException("Not yet implemented");
    //}

    //
    // Summary:
    //     Returns an instance of a System.Data.Common.DbProviderFactory.
    //
    // Parameters:
    //   providerInvariantName:
    //     Invariant name of a provider.
    //
    // Returns:
    //     An instance of a System.Data.Common.DbProviderFactory for a specified provider
    //     name.
    public static DbProviderFactory getFactory(String providerInvariantName) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //
    // Summary:
    //     Returns a System.Data.DataTable that contains information about all installed
    //     providers that implement System.Data.Common.DbProviderFactory.
    //
    // Returns:
    //     Returns a System.Data.DataTable containing System.Data.DataRow objects that
    //     contain the following data. Column ordinalColumn nameDescription0NameHuman-readable
    //     name for the data provider.1DescriptionHuman-readable description of the
    //     data provider.2InvariantNameName that can be used programmatically to refer
    //     to the data provider.3AssemblyQualifiedNameFully qualified name of the factory
    //     class, which contains enough information to instantiate the object.
    //public static DataTable getFactoryClasses() {
    //    throw new UnsupportedOperationException("Not yet implemented");
    //}
}