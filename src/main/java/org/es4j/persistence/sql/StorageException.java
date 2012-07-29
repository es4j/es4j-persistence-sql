package org.es4j.persistence.sql;

//using System;
//using System.Runtime.Serialization;

/// <summary>
/// Represents a general failure of the storage engine or persistence infrastructure.
/// </summary>
//[Serializable]
public class StorageException extends RuntimeException {

    /// <summary>
    /// Initializes a new instance of the StorageException class.
    /// </summary>
    public StorageException() {
    }

    /// <summary>
    /// Initializes a new instance of the StorageException class.
    /// </summary>
    /// <param name="message">The message that describes the error.</param>
    public StorageException(String message) {
        super(message);
    }

    /// <summary>
    /// Initializes a new instance of the StorageException class.
    /// </summary>
    /// <param name="message">The message that describes the error.</param>
    /// <param name="innerException">The message that is the cause of the current exception.</param>
    public StorageException(String message, Exception innerException) {
        //super(message, innerException)
    }

    /// <summary>
    /// Initializes a new instance of the StorageException class.
    /// </summary>
    /// <param name="info">The SerializationInfo that holds the serialized object data of the exception being thrown.</param>
    /// <param name="context">The StreamingContext that contains contextual information about the source or destination.</param>
    //protected StorageException(SerializationInfo info, StreamingContext context) {
        //super(info, context)
    //}
}