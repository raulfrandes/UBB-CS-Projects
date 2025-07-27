using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SQLite;
using log4net;
using model;
using model.validator;
using persistence.interfaces;

namespace persistence.dbRepositories;

public class RefereeDbRepository: IRefereeRepository
{
    private static readonly ILog Log = LogManager.GetLogger("RefereeDBRepository");

    private IDictionary<String, string> _props;
    private IValidator<Referee> _refereeValidator;

    public RefereeDbRepository(IDictionary<string, string> props, IValidator<Referee> refereeValidator)
    {
        Log.Info("Creating RefereeDbRepository");
        this._props = props;
        this._refereeValidator = refereeValidator;
    }
    
    public IEnumerable<Referee> FindAll()
    {
        Log.InfoFormat("Entering FindAll");
        IDbConnection connection = DbUtils.GetConnection(_props);

        IList<Referee> referees = new List<Referee>();
        try
        {
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from referees";

                using (var dataReader = command.ExecuteReader())
                {
                    while (dataReader.Read())
                    {
                        long id = dataReader.GetInt64(0);
                        String name = dataReader.GetString(1);
                        string username = dataReader.GetString(2);
                        String password = dataReader.GetString(3);
                        Referee referee = new Referee(name, username, password);
                        referee.Id = id;
                        referees.Add(referee);
                    }
                }
            }

            Log.InfoFormat("Exiting FindAll with value {0}", referees);
            return referees;
        }
        catch (SQLiteException e)
        {
            Log.Error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }

    public Referee? FindById(long entityId)
    {
        Log.InfoFormat("Entering FindById with value {0}", entityId);
        IDbConnection connection = DbUtils.GetConnection(_props);

        try
        {
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from referees where id = @id";
                IDbDataParameter paramId = command.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = entityId;
                command.Parameters.Add(paramId);

                using (var dataReader = command.ExecuteReader())
                {
                    if (dataReader.Read())
                    {
                        long id = dataReader.GetInt64(0);
                        String name = dataReader.GetString(1);
                        string username = dataReader.GetString(2);
                        String password = dataReader.GetString(3); 
                        Referee referee = new Referee(name, username, password);
                        referee.Id = id;
                        Log.InfoFormat("Exiting FindByUsername with value {0}", referee);
                        return referee;
                    }
                    Log.InfoFormat("Exiting FindByUsername with value {0}", null);
                    return null;
                }
            }
        }
        catch (SQLiteException e)
        {
            Log.Error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }

    public Referee? Save(Referee entity)
    {
        _refereeValidator.Validate(entity);
        Log.InfoFormat("Entering Save with value {0}", entity);
        IDbConnection connection = DbUtils.GetConnection(_props);
        try
        {
            using (var command = connection.CreateCommand())
            {
                command.CommandText =
                    "insert into referees (name, username, password) values (@name, @username, @password)";

                IDbDataParameter paramName = command.CreateParameter();
                paramName.ParameterName = "@name";
                paramName.Value = entity.Name;
                command.Parameters.Add(paramName);

                IDbDataParameter paramUsername = command.CreateParameter();
                paramUsername.ParameterName = "@username";
                paramUsername.Value = entity.Username;
                command.Parameters.Add(paramUsername);

                IDbDataParameter paramPassword = command.CreateParameter();
                paramPassword.ParameterName = "password";
                paramPassword.Value = entity.Password;
                command.Parameters.Add(paramPassword);

                var rows = command.ExecuteNonQuery();
                Log.InfoFormat("saved {0} instances", rows);

                if (rows > 0)
                {
                    command.CommandText = "select last_insert_rowid()";
                    var result = command.ExecuteScalar();
                    if (result != null && result != DBNull.Value)
                    {
                        long id = Convert.ToInt64(result);
                        entity.Id = id;
                        Log.InfoFormat("Exiting Save with value {0}", entity);
                        return entity;
                    }
                }
            }
            Log.InfoFormat("Exiting Save with value {0}", null);
            return null;
        }
        catch (SQLiteException e)
        {
            Log.Error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }

    public Referee DeleteById(long entityId)
    {
        throw new NotImplementedException();
    }

    public Referee Update(Referee entity)
    {
        throw new NotImplementedException();
    }

    public Referee? FindByUsername(string username)
    {
        Log.InfoFormat("Entering FindByUsername with value {0}", username);
        IDbConnection connection = DbUtils.GetConnection(_props);

        try
        {
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from referees where username = @username";
                IDbDataParameter paramUsername = command.CreateParameter();
                paramUsername.ParameterName = "@username";
                paramUsername.Value = username;
                command.Parameters.Add(paramUsername);

                using (var dataReader = command.ExecuteReader())
                {
                    if (dataReader.Read())
                    {
                        long id = dataReader.GetInt64(0);
                        String name = dataReader.GetString(1);
                        String password = dataReader.GetString(3);
                        Referee referee = new Referee(name, username, password);
                        referee.Id = id;
                        Log.InfoFormat("Exiting FindByUsername with value {0}", referee);
                        return referee;
                    }
                    Log.InfoFormat("Exiting FindByUsername with value {0}", null);
                    return null;
                }
            }
        }
        catch (SQLiteException e)
        {
            Log.Error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }
}