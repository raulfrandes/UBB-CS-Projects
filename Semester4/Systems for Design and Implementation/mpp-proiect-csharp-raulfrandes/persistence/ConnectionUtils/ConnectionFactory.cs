using System;
using System.Collections.Generic;
using System.Data;
using System.Reflection;

namespace persistence.ConnectionUtils;

public abstract class ConnectionFactory
{
    private static ConnectionFactory _instance;

    protected ConnectionFactory()
    {
    }

    public static ConnectionFactory GetInstance()
    {
        if (_instance == null)
        {
            Assembly assembly = Assembly.GetExecutingAssembly();
            Type[] types = assembly.GetTypes();
            foreach (var type in types)
            {
                if (type.IsSubclassOf(typeof(ConnectionFactory)))
                {
                    _instance = (ConnectionFactory)Activator.CreateInstance(type);
                }
            }
        }

        return _instance;
    }

    public abstract IDbConnection CreateConnection(IDictionary<String, string> props);
}