package org.es4j.dotnet;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Esfand
 */
// Provides access to configuration files for client applications. This class
// cannot be inherited.
public final class ConfigurationManager {
    
    private static final List<ConnectionStringSettings>  connectionStrings = new LinkedList<ConnectionStringSettings>();

    // Gets the System.Configuration.ConnectionStringsSection data for the current
    // application's default configuration.
    //
    // Returns:
    //     Returns a System.Configuration.ConnectionStringSettingsCollection object
    //     that contains the contents of the System.Configuration.ConnectionStringsSection
    //     object for the current application's default configuration.
    //
    // Exceptions:
    //   System.Configuration.ConfigurationErrorsException:
    //     Could not retrieve a System.Configuration.ConnectionStringSettingsCollection
    //     object.
    //private static Iterable<ConnectionStringSettings> connectionStrings; // { get; }
    public static Iterable<ConnectionStringSettings> getConnectionStrings() {
        if(true) throw new UnsupportedOperationException("Not yet implemented");
        return connectionStrings;
    }
}
