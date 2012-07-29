package org.es4j.serialization;

//using System;

import java.lang.reflect.Type;

//using System.Runtime.InteropServices;
//using System.Security;
// Summary:
//     Stores all the data needed to serialize or deserialize an object. This class
//     cannot be inherited.
//[ComVisible(true)]
public final class SerializationInfo {

    // Summary:
    //     Creates a new instance of the System.Runtime.Serialization.SerializationInfo
    //     class.
    //
    // Parameters:
    //   type:
    //     The System.Type of the object to serialize.
    //
    //   converter:
    //     The System.Runtime.Serialization.IFormatterConverter used during deserialization.
    //
    // Exceptions:
    //   System.ArgumentNullException:
    //     type or converter is null.
    //[CLSCompliant(false)]
    public SerializationInfo(Type type, IFormatterConverter converter) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

        // Summary:
        //     Gets or sets the assembly name of the type to serialize during serialization
        //     only.
        //
        // Returns:
        //     The full name of the assembly of the type to serialize.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     The value the property is set to is null.
        private String assemblyName; // { get; set; }
        
        //
        // Summary:
        //     Gets or sets the full name of the System.Type to serialize.
        //
        // Returns:
        //     The full name of the type to serialize.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     The value this property is set to is null.
        private String  fullTypeName; // { get; set; }
        private boolean fsAssemblyNameSetExplicit; // { get; }
        private boolean isFullTypeNameSetExplicit; // { get; }
        
        //
        // Summary:
        //     Gets the number of members that have been added to the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Returns:
        //     The number of members that have been added to the current System.Runtime.Serialization.SerializationInfo.
        private int  memberCount; // { get; }
        private Type objectType; // { get; }

        // Summary:
        //     Adds a Boolean value into the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name to associate with the value, so it can be deserialized later.
        //
        //   value:
        //     The Boolean value to serialize.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     The name parameter is null.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     A value has already been associated with name.
        public void addValue(String name, boolean value) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
        
        //
        // Summary:
        //     Adds an 8-bit unsigned integer value into the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name to associate with the value, so it can be deserialized later.
        //
        //   value:
        //     The byte value to serialize.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     The name parameter is null.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     A value has already been associated with name.
        public void addValue(String name, byte value) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
        
        //
        // Summary:
        //     Adds a Unicode character value into the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name to associate with the value, so it can be deserialized later.
        //
        //   value:
        //     The character value to serialize.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     The name parameter is null.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     A value has already been associated with name.
        public void addValue(String name, char value) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
        
        //
        // Summary:
        //     Adds a System.DateTime value into the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name to associate with the value, so it can be deserialized later.
        //
        //   value:
        //     The System.DateTime value to serialize.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     The name parameter is null.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     A value has already been associated with name.
        public void addValue(String name, DateTime value);
        //
        // Summary:
        //     Adds a decimal value into the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name to associate with the value, so it can be deserialized later.
        //
        //   value:
        //     The decimal value to serialize.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     If The name parameter is null.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     If a value has already been associated with name.
        public void AddValue(string name, decimal value);
        //
        // Summary:
        //     Adds a double-precision floating-point value into the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name to associate with the value, so it can be deserialized later.
        //
        //   value:
        //     The double value to serialize.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     The name parameter is null.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     A value has already been associated with name.
        public void AddValue(string name, double value);
        //
        // Summary:
        //     Adds a single-precision floating-point value into the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name to associate with the value, so it can be deserialized later.
        //
        //   value:
        //     The single value to serialize.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     The name parameter is null.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     A value has already been associated with name.
        public void AddValue(string name, float value);
        //
        // Summary:
        //     Adds a 32-bit signed integer value into the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name to associate with the value, so it can be deserialized later.
        //
        //   value:
        //     The Int32 value to serialize.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     The name parameter is null.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     A value has already been associated with name.
        public void AddValue(string name, int value);
        //
        // Summary:
        //     Adds a 64-bit signed integer value into the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name to associate with the value, so it can be deserialized later.
        //
        //   value:
        //     The Int64 value to serialize.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     The name parameter is null.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     A value has already been associated with name.
        public void AddValue(string name, long value);
        //
        // Summary:
        //     Adds the specified object into the System.Runtime.Serialization.SerializationInfo
        //     store, where it is associated with a specified name.
        //
        // Parameters:
        //   name:
        //     The name to associate with the value, so it can be deserialized later.
        //
        //   value:
        //     The value to be serialized. Any children of this object will automatically
        //     be serialized.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     name is null.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     A value has already been associated with name.
        public void AddValue(string name, object value);
        //
        // Summary:
        //     Adds an 8-bit signed integer value into the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name to associate with the value, so it can be deserialized later.
        //
        //   value:
        //     The Sbyte value to serialize.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     The name parameter is null.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     A value has already been associated with name.
        [CLSCompliant(false)]
        public void AddValue(string name, sbyte value);
        //
        // Summary:
        //     Adds a 16-bit signed integer value into the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name to associate with the value, so it can be deserialized later.
        //
        //   value:
        //     The Int16 value to serialize.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     The name parameter is null.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     A value has already been associated with name.
        public void AddValue(string name, short value);
        //
        // Summary:
        //     Adds a 32-bit unsigned integer value into the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name to associate with the value, so it can be deserialized later.
        //
        //   value:
        //     The UInt32 value to serialize.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     The name parameter is null.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     A value has already been associated with name.
        [CLSCompliant(false)]
        public void AddValue(string name, uint value);
        //
        // Summary:
        //     Adds a 64-bit unsigned integer value into the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name to associate with the value, so it can be deserialized later.
        //
        //   value:
        //     The UInt64 value to serialize.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     The name parameter is null.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     A value has already been associated with name.
        [CLSCompliant(false)]
        public void AddValue(string name, ulong value);
        //
        // Summary:
        //     Adds a 16-bit unsigned integer value into the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name to associate with the value, so it can be deserialized later.
        //
        //   value:
        //     The UInt16 value to serialize.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     The name parameter is null.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     A value has already been associated with name.
        [CLSCompliant(false)]
        public void AddValue(string name, ushort value);
        //
        // Summary:
        //     Adds a value into the System.Runtime.Serialization.SerializationInfo store,
        //     where value is associated with name and is serialized as being of System.Typetype.
        //
        // Parameters:
        //   name:
        //     The name to associate with the value, so it can be deserialized later.
        //
        //   value:
        //     The value to be serialized. Any children of this object will automatically
        //     be serialized.
        //
        //   type:
        //     The System.Type to associate with the current object. This parameter must
        //     always be the type of the object itself or of one of its base classes.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     If name or type is null.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     A value has already been associated with name.
        public void AddValue(string name, object value, Type type);
        //
        // Summary:
        //     Retrieves a Boolean value from the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name associated with the value to retrieve.
        //
        // Returns:
        //     The Boolean value associated with name.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     name is null.
        //
        //   System.InvalidCastException:
        //     The value associated with name cannot be converted to a Boolean value.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     An element with the specified name is not found in the current instance.
        public bool GetBoolean(string name);
        //
        // Summary:
        //     Retrieves an 8-bit unsigned integer value from the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name associated with the value to retrieve.
        //
        // Returns:
        //     The 8-bit unsigned integer associated with name.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     name is null.
        //
        //   System.InvalidCastException:
        //     The value associated with name cannot be converted to an 8-bit unsigned integer.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     An element with the specified name is not found in the current instance.
        public byte GetByte(string name);
        //
        // Summary:
        //     Retrieves a Unicode character value from the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name associated with the value to retrieve.
        //
        // Returns:
        //     The Unicode character associated with name.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     name is null.
        //
        //   System.InvalidCastException:
        //     The value associated with name cannot be converted to a Unicode character.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     An element with the specified name is not found in the current instance.
        public char GetChar(string name);
        //
        // Summary:
        //     Retrieves a System.DateTime value from the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name associated with the value to retrieve.
        //
        // Returns:
        //     The System.DateTime value associated with name.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     name is null.
        //
        //   System.InvalidCastException:
        //     The value associated with name cannot be converted to a System.DateTime value.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     An element with the specified name is not found in the current instance.
        public DateTime GetDateTime(string name);
        //
        // Summary:
        //     Retrieves a decimal value from the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name associated with the value to retrieve.
        //
        // Returns:
        //     A decimal value from the System.Runtime.Serialization.SerializationInfo.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     name is null.
        //
        //   System.InvalidCastException:
        //     The value associated with name cannot be converted to a decimal.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     An element with the specified name is not found in the current instance.
        public decimal GetDecimal(string name);
        //
        // Summary:
        //     Retrieves a double-precision floating-point value from the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name associated with the value to retrieve.
        //
        // Returns:
        //     The double-precision floating-point value associated with name.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     name is null.
        //
        //   System.InvalidCastException:
        //     The value associated with name cannot be converted to a double-precision
        //     floating-point value.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     An element with the specified name is not found in the current instance.
        public double GetDouble(string name);
        //
        // Summary:
        //     Returns a System.Runtime.Serialization.SerializationInfoEnumerator used to
        //     iterate through the name-value pairs in the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Returns:
        //     A System.Runtime.Serialization.SerializationInfoEnumerator for parsing the
        //     name-value pairs contained in the System.Runtime.Serialization.SerializationInfo
        //     store.
        public SerializationInfoEnumerator GetEnumerator();
        //
        // Summary:
        //     Retrieves a 16-bit signed integer value from the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name associated with the value to retrieve.
        //
        // Returns:
        //     The 16-bit signed integer associated with name.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     name is null.
        //
        //   System.InvalidCastException:
        //     The value associated with name cannot be converted to a 16-bit signed integer.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     An element with the specified name is not found in the current instance.
        public short GetInt16(string name);
        //
        // Summary:
        //     Retrieves a 32-bit signed integer value from the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name of the value to retrieve.
        //
        // Returns:
        //     The 32-bit signed integer associated with name.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     name is null.
        //
        //   System.InvalidCastException:
        //     The value associated with name cannot be converted to a 32-bit signed integer.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     An element with the specified name is not found in the current instance.
        public int GetInt32(string name);
        //
        // Summary:
        //     Retrieves a 64-bit signed integer value from the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name associated with the value to retrieve.
        //
        // Returns:
        //     The 64-bit signed integer associated with name.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     name is null.
        //
        //   System.InvalidCastException:
        //     The value associated with name cannot be converted to a 64-bit signed integer.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     An element with the specified name is not found in the current instance.
        public long GetInt64(string name);
        //
        // Summary:
        //     Retrieves an 8-bit signed integer value from the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name associated with the value to retrieve.
        //
        // Returns:
        //     The 8-bit signed integer associated with name.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     name is null.
        //
        //   System.InvalidCastException:
        //     The value associated with name cannot be converted to an 8-bit signed integer.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     An element with the specified name is not found in the current instance.
        [CLSCompliant(false)]
        public sbyte GetSByte(string name);
        //
        // Summary:
        //     Retrieves a single-precision floating-point value from the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name of the value to retrieve.
        //
        // Returns:
        //     The single-precision floating-point value associated with name.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     name is null.
        //
        //   System.InvalidCastException:
        //     The value associated with name cannot be converted to a single-precision
        //     floating-point value.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     An element with the specified name is not found in the current instance.
        public float GetSingle(string name);
        //
        // Summary:
        //     Retrieves a System.String value from the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name associated with the value to retrieve.
        //
        // Returns:
        //     The System.String associated with name.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     name is null.
        //
        //   System.InvalidCastException:
        //     The value associated with name cannot be converted to a System.String.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     An element with the specified name is not found in the current instance.
        public string GetString(string name);
        //
        // Summary:
        //     Retrieves a 16-bit unsigned integer value from the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name associated with the value to retrieve.
        //
        // Returns:
        //     The 16-bit unsigned integer associated with name.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     name is null.
        //
        //   System.InvalidCastException:
        //     The value associated with name cannot be converted to a 16-bit unsigned integer.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     An element with the specified name is not found in the current instance.
        [CLSCompliant(false)]
        public ushort GetUInt16(string name);
        //
        // Summary:
        //     Retrieves a 32-bit unsigned integer value from the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name associated with the value to retrieve.
        //
        // Returns:
        //     The 32-bit unsigned integer associated with name.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     name is null.
        //
        //   System.InvalidCastException:
        //     The value associated with name cannot be converted to a 32-bit unsigned integer.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     An element with the specified name is not found in the current instance.
        [CLSCompliant(false)]
        public uint GetUInt32(string name);
        //
        // Summary:
        //     Retrieves a 64-bit unsigned integer value from the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name associated with the value to retrieve.
        //
        // Returns:
        //     The 64-bit unsigned integer associated with name.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     name is null.
        //
        //   System.InvalidCastException:
        //     The value associated with name cannot be converted to a 64-bit unsigned integer.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     An element with the specified name is not found in the current instance.
        [CLSCompliant(false)]
        public ulong GetUInt64(string name);
        //
        // Summary:
        //     Retrieves a value from the System.Runtime.Serialization.SerializationInfo
        //     store.
        //
        // Parameters:
        //   name:
        //     The name associated with the value to retrieve.
        //
        //   type:
        //     The System.Type of the value to retrieve. If the stored value cannot be converted
        //     to this type, the system will throw a System.InvalidCastException.
        //
        // Returns:
        //     The object of the specified System.Type associated with name.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     name or type is null.
        //
        //   System.InvalidCastException:
        //     The value associated with name cannot be converted to type.
        //
        //   System.Runtime.Serialization.SerializationException:
        //     An element with the specified name is not found in the current instance.
        [SecuritySafeCritical]
        public object GetValue(string name, Type type);
        //
        // Summary:
        //     Sets the System.Type of the object to serialize.
        //
        // Parameters:
        //   type:
        //     The System.Type of the object to serialize.
        //
        // Exceptions:
        //   System.ArgumentNullException:
        //     The type parameter is null.
        public void SetType(Type type);
}