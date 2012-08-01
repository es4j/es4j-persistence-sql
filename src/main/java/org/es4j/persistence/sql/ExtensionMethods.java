namespace EventStore.Persistence.SqlPersistence
{
	using System;
	using System.Data;
	using Logging;

	internal static class ExtensionMethods
	{
		private static readonly ILog Logger = LogFactory.BuildLogger(typeof(ExtensionMethods));

		public static Guid ToGuid(this object value)
		{
			if (value is Guid)
				return (Guid)value;

			var bytes = value as byte[];
			return bytes != null ? new Guid(bytes) : Guid.Empty;
		}
		public static int ToInt(this object value)
		{
			return value is long ? (int)(long)value : (int)value;
		}
		public static DateTime ToDateTime(this object value)
		{
			value = value is decimal ? (long)(decimal)value : value;
			return value is long ? new DateTime((long)value) : (DateTime)value;
		}

		public static IDbCommand SetParameter(this IDbCommand command, string name, object value)
		{
			Logger.Verbose("Rebinding parameter '{0}' with value: {1}", name, value);
			var parameter = (IDataParameter)command.Parameters[name];
			parameter.Value = value;
			return command;
		}
	}
}