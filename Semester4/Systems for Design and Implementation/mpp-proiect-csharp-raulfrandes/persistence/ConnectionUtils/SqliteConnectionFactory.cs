using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SQLite;

namespace persistence.ConnectionUtils;

public class SqliteConnectionFactory: ConnectionFactory
{
    public override IDbConnection CreateConnection(IDictionary<String, string> props)
    {
        Console.WriteLine("creating ... sqlite connection");
        String connectionString = props["ConnectionString"];
        return new SQLiteConnection(connectionString);
    }
}