package org.es4j.dotnet;


// Represents a configuration element within a configuration file.
public abstract class ConfigurationElement {
/*    
        // Summary:
        //     Initializes a new instance of the System.Configuration.ConfigurationElement
        //     class.
        protected ConfigurationElement();

        // Summary:
        //     Gets a reference to the top-level System.Configuration.Configuration instance
        //     that represents the configuration hierarchy that the current System.Configuration.ConfigurationElement
        //     instance belongs to.
        //
        // Returns:
        //     The top-level System.Configuration.Configuration instance that the current
        //     System.Configuration.ConfigurationElement instance belongs to.
        public Configuration CurrentConfiguration { get; }
        //
        // Summary:
        //     Gets an System.Configuration.ElementInformation object that contains the
        //     non-customizable information and functionality of the System.Configuration.ConfigurationElement
        //     object.
        //
        // Returns:
        //     An System.Configuration.ElementInformation that contains the non-customizable
        //     information and functionality of the System.Configuration.ConfigurationElement.
        public ElementInformation ElementInformation { get; }
        //
        // Summary:
        //     Gets the System.Configuration.ConfigurationElementProperty object that represents
        //     the System.Configuration.ConfigurationElement object itself.
        //
        // Returns:
        //     The System.Configuration.ConfigurationElementProperty that represents the
        //     System.Configuration.ConfigurationElement itself.
        protected internal virtual ConfigurationElementProperty ElementProperty { get; }
        //
        // Summary:
        //     Gets the System.Configuration.ContextInformation object for the System.Configuration.ConfigurationElement
        //     object.
        //
        // Returns:
        //     The System.Configuration.ContextInformation for the System.Configuration.ConfigurationElement.
        //
        // Exceptions:
        //   System.Configuration.ConfigurationErrorsException:
        //     The current element is not associated with a context.
        protected ContextInformation EvaluationContext { get; }
        //
        // Summary:
        //     Gets the collection of locked attributes.
        //
        // Returns:
        //     The System.Configuration.ConfigurationLockCollection of locked attributes
        //     (properties) for the element.
        public ConfigurationLockCollection LockAllAttributesExcept { get; }
        //
        // Summary:
        //     Gets the collection of locked elements.
        //
        // Returns:
        //     The System.Configuration.ConfigurationLockCollection of locked elements.
        public ConfigurationLockCollection LockAllElementsExcept { get; }
        //
        // Summary:
        //     Gets the collection of locked attributes
        //
        // Returns:
        //     The System.Configuration.ConfigurationLockCollection of locked attributes
        //     (properties) for the element.
        public ConfigurationLockCollection LockAttributes { get; }
        //
        // Summary:
        //     Gets the collection of locked elements.
        //
        // Returns:
        //     The System.Configuration.ConfigurationLockCollection of locked elements.
        public ConfigurationLockCollection LockElements { get; }
        //
        // Summary:
        //     Gets or sets a value indicating whether the element is locked.
        //
        // Returns:
        //     true if the element is locked; otherwise, false. The default is false.
        //
        // Exceptions:
        //   System.Configuration.ConfigurationErrorsException:
        //     The element has already been locked at a higher configuration level.
        public bool LockItem { get; set; }
        //
        // Summary:
        //     Gets the collection of properties.
        //
        // Returns:
        //     The System.Configuration.ConfigurationPropertyCollection of properties for
        //     the element.
        protected internal virtual ConfigurationPropertyCollection Properties { get; }

        // Summary:
        //     Gets or sets a property or attribute of this configuration element.
        //
        // Parameters:
        //   prop:
        //     The property to access.
        //
        // Returns:
        //     The specified property, attribute, or child element.
        //
        // Exceptions:
        //   System.Configuration.ConfigurationException:
        //     prop is null or does not exist within the element.
        //
        //   System.Configuration.ConfigurationErrorsException:
        //     prop is read only or locked.
        protected internal object this[ConfigurationProperty prop] { get; set; }
        //
        // Summary:
        //     Gets or sets a property, attribute, or child element of this configuration
        //     element.
        //
        // Parameters:
        //   propertyName:
        //     The name of the System.Configuration.ConfigurationProperty to access.
        //
        // Returns:
        //     The specified property, attribute, or child element
        //
        // Exceptions:
        //   System.Configuration.ConfigurationErrorsException:
        //     prop is read-only or locked.
        protected internal object this[string propertyName] { get; set; }

        // Summary:
        //     Reads XML from the configuration file.
        //
        // Parameters:
        //   reader:
        //     The System.Xml.XmlReader that reads from the configuration file.
        //
        //   serializeCollectionKey:
        //     true to serialize only the collection key properties; otherwise, false.
        //
        // Exceptions:
        //   System.Configuration.ConfigurationErrorsException:
        //     The element to read is locked.- or -An attribute of the current node is not
        //     recognized.- or -The lock status of the current node cannot be determined.
        protected internal virtual void DeserializeElement(System.Xml.XmlReader reader, bool serializeCollectionKey);
        //
        // Summary:
        //     Compares the current System.Configuration.ConfigurationElement instance to
        //     the specified object.
        //
        // Parameters:
        //   compareTo:
        //     The object to compare with.
        //
        // Returns:
        //     true if the object to compare with is equal to the current System.Configuration.ConfigurationElement
        //     instance; otherwise, false. The default is false.
        public override bool Equals(object compareTo);
        //
        // Summary:
        //     Gets a unique value representing the current System.Configuration.ConfigurationElement
        //     instance.
        //
        // Returns:
        //     A unique value representing the current System.Configuration.ConfigurationElement
        //     instance.
        public override int GetHashCode();
        //
        // Summary:
        //     Returns the transformed version of the specified assembly name.
        //
        // Parameters:
        //   assemblyName:
        //     The name of the assembly.
        //
        // Returns:
        //     The transformed version of the assembly name. If no transformer is available,
        //     the assemblyName parameter value is returned unchanged. The System.Configuration.Configuration.TypeStringTransformer
        //     property is null if no transformer is available.
        protected virtual string GetTransformedAssemblyString(string assemblyName);
        //
        // Summary:
        //     Returns the transformed version of the specified type name.
        //
        // Parameters:
        //   typeName:
        //     The name of the type.
        //
        // Returns:
        //     The transformed version of the specified type name. If no transformer is
        //     available, the typeName parameter value is returned unchanged. The System.Configuration.Configuration.TypeStringTransformer
        //     property is null if no transformer is available.
        protected virtual string GetTransformedTypeString(string typeName);
        //
        // Summary:
        //     Sets the System.Configuration.ConfigurationElement object to its initial
        //     state.
        protected internal virtual void Init();
        //
        // Summary:
        //     Used to initialize a default set of values for the System.Configuration.ConfigurationElement
        //     object.
        protected internal virtual void InitializeDefault();
        //
        // Summary:
        //     Indicates whether this configuration element has been modified since it was
        //     last saved or loaded, when implemented in a derived class.
        //
        // Returns:
        //     true if the element has been modified; otherwise, false.
        protected internal virtual bool IsModified();
        //
        // Summary:
        //     Gets a value indicating whether the System.Configuration.ConfigurationElement
        //     object is read-only.
        //
        // Returns:
        //     true if the System.Configuration.ConfigurationElement object is read-only;
        //     otherwise, false.
        public virtual bool IsReadOnly();
        //
        // Summary:
        //     Adds the invalid-property errors in this System.Configuration.ConfigurationElement
        //     object, and in all subelements, to the passed list.
        //
        // Parameters:
        //   errorList:
        //     An object that implements the System.Collections.IList interface.
        protected virtual void ListErrors(IList errorList);
        //
        // Summary:
        //     Gets a value indicating whether an unknown attribute is encountered during
        //     deserialization.
        //
        // Parameters:
        //   name:
        //     The name of the unrecognized attribute.
        //
        //   value:
        //     The value of the unrecognized attribute.
        //
        // Returns:
        //     true when an unknown attribute is encountered while deserializing; otherwise,
        //     false.
        protected virtual bool OnDeserializeUnrecognizedAttribute(string name, string value);
        //
        // Summary:
        //     Gets a value indicating whether an unknown element is encountered during
        //     deserialization.
        //
        // Parameters:
        //   elementName:
        //     The name of the unknown subelement.
        //
        //   reader:
        //     The System.Xml.XmlReader being used for deserialization.
        //
        // Returns:
        //     true when an unknown element is encountered while deserializing; otherwise,
        //     false.
        //
        // Exceptions:
        //   System.Configuration.ConfigurationErrorsException:
        //     The element identified by elementName is locked.- or -One or more of the
        //     element's attributes is locked.- or -elementName is unrecognized, or the
        //     element has an unrecognized attribute.- or -The element has a Boolean attribute
        //     with an invalid value.- or -An attempt was made to deserialize a property
        //     more than once.- or -An attempt was made to deserialize a property that is
        //     not a valid member of the element.- or -The element cannot contain a CDATA
        //     or text element.
        protected virtual bool OnDeserializeUnrecognizedElement(string elementName, System.Xml.XmlReader reader);
        //
        // Summary:
        //     Throws an exception when a required property is not found.
        //
        // Parameters:
        //   name:
        //     The name of the required attribute that was not found.
        //
        // Returns:
        //     None.
        //
        // Exceptions:
        //   System.Configuration.ConfigurationErrorsException:
        //     In all cases.
        protected virtual object OnRequiredPropertyNotFound(string name);
        //
        // Summary:
        //     Called after deserialization.
        protected virtual void PostDeserialize();
        //
        // Summary:
        //     Called before serialization.
        //
        // Parameters:
        //   writer:
        //     The System.Xml.XmlWriter that will be used to serialize the System.Configuration.ConfigurationElement.
        protected virtual void PreSerialize(System.Xml.XmlWriter writer);
        //
        // Summary:
        //     Resets the internal state of the System.Configuration.ConfigurationElement
        //     object, including the locks and the properties collections.
        //
        // Parameters:
        //   parentElement:
        //     The parent node of the configuration element.
        protected internal virtual void Reset(ConfigurationElement parentElement);
        //
        // Summary:
        //     Resets the value of the System.Configuration.ConfigurationElement.IsModified()
        //     method to false when implemented in a derived class.
        protected internal virtual void ResetModified();
        //
        // Summary:
        //     Writes the contents of this configuration element to the configuration file
        //     when implemented in a derived class.
        //
        // Parameters:
        //   writer:
        //     The System.Xml.XmlWriter that writes to the configuration file.
        //
        //   serializeCollectionKey:
        //     true to serialize only the collection key properties; otherwise, false.
        //
        // Returns:
        //     true if any data was actually serialized; otherwise, false.
        //
        // Exceptions:
        //   System.Configuration.ConfigurationErrorsException:
        //     The current attribute is locked at a higher configuration level.
        protected internal virtual bool SerializeElement(System.Xml.XmlWriter writer, bool serializeCollectionKey);
        //
        // Summary:
        //     Writes the outer tags of this configuration element to the configuration
        //     file when implemented in a derived class.
        //
        // Parameters:
        //   writer:
        //     The System.Xml.XmlWriter that writes to the configuration file.
        //
        //   elementName:
        //     The name of the System.Configuration.ConfigurationElement to be written.
        //
        // Returns:
        //     true if writing was successful; otherwise, false.
        //
        // Exceptions:
        //   System.Exception:
        //     The element has multiple child elements.
        protected internal virtual bool SerializeToXmlElement(System.Xml.XmlWriter writer, string elementName);
        //
        // Summary:
        //     Sets a property to the specified value.
        //
        // Parameters:
        //   prop:
        //     The element property to set.
        //
        //   value:
        //     The value to assign to the property.
        //
        //   ignoreLocks:
        //     true if the locks on the property should be ignored; otherwise, false.
        //
        // Exceptions:
        //   System.Configuration.ConfigurationErrorsException:
        //     Occurs if the element is read-only or ignoreLocks is true but the locks cannot
        //     be ignored.
        protected void SetPropertyValue(ConfigurationProperty prop, object value, bool ignoreLocks);
        //
        // Summary:
        //     Sets the System.Configuration.ConfigurationElement.IsReadOnly() property
        //     for the System.Configuration.ConfigurationElement object and all subelements.
        protected internal virtual void SetReadOnly();
        //
        // Summary:
        //     Modifies the System.Configuration.ConfigurationElement object to remove all
        //     values that should not be saved.
        //
        // Parameters:
        //   sourceElement:
        //     A System.Configuration.ConfigurationElement at the current level containing
        //     a merged view of the properties.
        //
        //   parentElement:
        //     The parent System.Configuration.ConfigurationElement, or null if this is
        //     the top level.
        //
        //   saveMode:
        //     A System.Configuration.ConfigurationSaveMode that determines which property
        //     values to include.
        protected internal virtual void Unmerge(ConfigurationElement sourceElement, ConfigurationElement parentElement, ConfigurationSaveMode saveMode);
*/
}