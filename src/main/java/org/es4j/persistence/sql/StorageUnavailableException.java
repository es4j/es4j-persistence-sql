package org.es4j.persistence.sql;

//import org.es4j.serialization.SerializationInfo;

import org.es4j.dotnet.SerializationInfo;
import org.es4j.dotnet.StreamingContext;


//using System;
//using System.Runtime.Serialization;

/// <summary>
/// Indicates that the underlying persistence medium is unavailable or offline.
/// </summary>
//[Serializable]
public class StorageUnavailableException extends StorageException {
    /// <summary>
    /// Initializes a new instance of the StorageUnavailableException class.
    /// </summary>

    public StorageUnavailableException() {
    }

    /// <summary>
    /// Initializes a new instance of the StorageUnavailableException class.
    /// </summary>
    /// <param name="message">The message that describes the error.</param>
    public StorageUnavailableException(String message) {
        super(message);
		
    }

    /// <summary>
    /// Initializes a new instance of the StorageUnavailableException class.
    /// </summary>
    /// <param name="message">The message that describes the error.</param>
    /// <param name="innerException">The message that is the cause of the current exception.</param>
    public StorageUnavailableException(String message, Exception innerException) {
        super(message, innerException);
    }

    /// <summary>
    /// Initializes a new instance of the StorageUnavailableException class.
    /// </summary>
    /// <param name="info">The SerializationInfo that holds the serialized object data of the exception being thrown.</param>
    /// <param name="context">The StreamingContext that contains contextual information about the source or destination.</param>
    protected StorageUnavailableException(SerializationInfo info, StreamingContext context) {
        //super(info, context);
        throw new UnsupportedOperationException("Not yet implemented");
    }
}