using System;
using System.Collections.Generic;
using System.Data;
using persistence.ConnectionUtils;

namespace persistence.dbRepositories;

public static class DbUtils
{
    private static IDbConnection _instance = null;
    
    public static IDbConnection GetConnection(IDictionary<String, string> props)
    {
        if (_instance == null || _instance.State == System.Data.ConnectionState.Closed)
        {
            _instance = GetNewConnection(props);
            _instance.Open();
        }

        return _instance;
    }

    private static IDbConnection GetNewConnection(IDictionary<String, string> props)
    {
        return ConnectionFactory.GetInstance().CreateConnection(props);
    }
}