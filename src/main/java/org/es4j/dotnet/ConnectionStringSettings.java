package org.es4j.dotnet;

/**
 *
 * @author Esfand
 */
    // Summary:
    //     Represents a single, named connection string in the connection strings configuration
    //     file section.
public final class ConnectionStringSettings extends ConfigurationElement {
    
    // Summary:
    //     Initializes a new instance of a System.Configuration.ConnectionStringSettings
    //     class.
    public ConnectionStringSettings() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //
    // Summary:
    //     Initializes a new instance of a System.Configuration.ConnectionStringSettings
    //     class.
    //
    // Parameters:
    //   name:
    //     The name of the connection string.
    //
    //   connectionString:
    //     The connection string.
    public ConnectionStringSettings(String name, String connectionString) {
        this.name = name;
        this.connectionString = connectionString;
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //
    // Summary:
    //     Initializes a new instance of a System.Configuration.ConnectionStringSettings
    //     object.
    //
    // Parameters:
    //   name:
    //     The name of the connection string.
    //
    //   connectionString:
    //     The connection string.
    //
    //   providerName:
    //     The name of the provider to use with the connection string.
    public ConnectionStringSettings(String name, String connectionString, String providerName) {
        this.name = name;
        this.connectionString = connectionString;
        this.providerName = providerName;
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // Summary:
    //     Gets or sets the connection string.
    //
    // Returns:
    //     The string value assigned to the System.Configuration.ConnectionStringSettings.ConnectionString
    //     property.
    private String connectionString; // { get; set; }

    public String getConnectionString() {
        return connectionString;
    }
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
        
    //
    // Summary:
    //     Gets or sets the System.Configuration.ConnectionStringSettings name.
    //
    // Returns:
    //     The string value assigned to the System.Configuration.ConnectionStringSettings.Name
    //     property.
    private String name; //{ get; set; }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
        
    //@Override
    protected ConfigurationPropertyCollection properties; // { get; }
    protected ConfigurationPropertyCollection getProperties() {
        return this.properties;
    }
        
    //
    // Summary:
    //     Gets or sets the provider name property.
    //
    // Returns:
    //     Gets or sets the System.Configuration.ConnectionStringSettings.ProviderName
    //     property.
    private String providerName; // { get; set; }

    public String getProviderName() {
        return providerName;
    }
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

        
        // Summary:
        //     Returns a string representation of the object.
        //
        // Returns:
        //     A string representation of the object.
        //@Override
        //public String toString();
}