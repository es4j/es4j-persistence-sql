package org.es4j.dotnet;

    // Summa
import java.io.Serializable;

//     Defines a key/value pair that can be set or retrieved.
//
// Type parameters:
//   TKey:
//     The type of the key.
//
//   TValue:
//     The type of the value.
//[Serializable]
public class KeyValuePair<TKey, TValue> implements Serializable {
    
    //
    // Summary:
    //     Initializes a new instance of the System.Collections.Generic.KeyValuePair<TKey,TValue>
    //     structure with the specified key and value.
    //
    // Parameters:
    //   key:
    //     The object defined in each key/value pair.
    //
    //   value:
    //     The definition associated with key.

    public KeyValuePair(TKey key, TValue value) {
        this.key = key;
        this.value = value;
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // Summary:
    //     Gets the key in the key/value pair.
    //
    // Returns:
    //     A TKey that is the key of the System.Collections.Generic.KeyValuePair<TKey,TValue>.
    private TKey key; // { get; }

    public TKey getKey() {
        return key;
    }
        
    //
    // Summary:
    //     Gets the value in the key/value pair.
    //
    // Returns:
    //     A TValue that is the value of the System.Collections.Generic.KeyValuePair<TKey,TValue>.
    private TValue value; // { get; }

    public TValue getValue() {
        return value;
    }

    // Summary:
    //     Returns a string representation of the System.Collections.Generic.KeyValuePair<TKey,TValue>,
    //     using the string representations of the key and value.
    //
    // Returns:
    //     A string representation of the System.Collections.Generic.KeyValuePair<TKey,TValue>,
    //     which includes the string representations of the key and value.
    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}