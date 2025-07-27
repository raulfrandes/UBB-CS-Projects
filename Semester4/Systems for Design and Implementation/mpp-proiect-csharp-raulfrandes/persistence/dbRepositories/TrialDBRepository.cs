using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SQLite;
using log4net;
using model;
using model.validator;
using persistence.interfaces;

namespace persistence.dbRepositories;

public class TrialDbRepository: ITrialRepository
{
    private static readonly ILog Log = LogManager.GetLogger("TrialDBRepository");

    private IDictionary<string, string> _props;
    private IValidator<Trial> _trialValidator;

    public TrialDbRepository(IDictionary<string, string> props, IValidator<Trial> trialValidator)
    {
        Log.Info("Creating TrialDBRepository");
        this._props = props;
        this._trialValidator = trialValidator;
    }

    private Referee? FindReferee(long idReferee)
    {
        Log.InfoFormat("Entering FindReferee with value {0}", idReferee);
        IDbConnection connection = DbUtils.GetConnection(_props);

        try
        {
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from referees where id = @id";
                IDbDataParameter paramId = command.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = idReferee;
                command.Parameters.Add(paramId);

                using (var dataReader = command.ExecuteReader())
                {
                    if (dataReader.Read())
                    {
                        String name = dataReader.GetString(1);
                        String username = dataReader.GetString(2);
                        String password = dataReader.GetString(3);
                        Referee referee = new Referee(name, username, password);
                        referee.Id = idReferee;
                        Log.InfoFormat("Exiting FindReferee with value {0}", referee);
                        return referee;
                    }
                    Log.InfoFormat("Exiting FindReferee with value {0}", null);
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
    
    public IEnumerable<Trial> FindAll()
    {
        Log.InfoFormat("Entering FindAll");
        IDbConnection connection = DbUtils.GetConnection(_props);

        IList<Trial> trials = new List<Trial>();
        try
        {
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from trials";

                using (var dataReader = command.ExecuteReader())
                {
                    while (dataReader.Read())
                    {
                        long id = dataReader.GetInt64(0);
                        string name = dataReader.GetString(1);
                        string description = dataReader.GetString(2);
                        long idReferee = dataReader.GetInt64(3);
                        Referee referee = FindReferee(idReferee)!;
                        Trial trial = new Trial(name, description, referee);
                        trial.Id = id;
                        trials.Add(trial);
                    }
                }
            }

            Log.InfoFormat("Exiting FindAll with value {0}", trials);
            return trials;
        }
        catch (SQLiteException e)
        {
            Log.Error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }

    public Trial? FindById(long entityId)
    {
        Log.InfoFormat("Entering FindById with value {0}", entityId);
        IDbConnection connection = DbUtils.GetConnection(_props);

        try
        {
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from trials where id = @id";
                IDbDataParameter paramId = command.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = entityId;
                command.Parameters.Add(paramId);

                using (var dataReader = command.ExecuteReader())
                {
                    if (dataReader.Read())
                    {
                        long id = dataReader.GetInt64(0);
                        string name = dataReader.GetString(1);
                        string description = dataReader.GetString(2);
                        long idReferee = dataReader.GetInt64(3);
                        Referee referee = FindReferee(idReferee)!;
                        Trial trial = new Trial(name, description, referee);
                        trial.Id = id;
                        Log.InfoFormat("Exiting FindById with value {0}", trial);
                        return trial;
                    }
                    Log.InfoFormat("Exiting FindById with value {0}", null);
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

    public Trial? Save(Trial entity)
    {
        _trialValidator.Validate(entity);
        Log.InfoFormat("Entering Save with value {0}", entity);
        IDbConnection connection = DbUtils.GetConnection(_props);
        try
        {
            using (var command = connection.CreateCommand())
            {
                command.CommandText =
                    "insert into trials (name, description, id_referee) values (@name, @description, @id_referee)";

                IDbDataParameter paramName = command.CreateParameter();
                paramName.ParameterName = "@name";
                paramName.Value = entity.Name;
                command.Parameters.Add(paramName);

                IDbDataParameter paramDescription = command.CreateParameter();
                paramDescription.ParameterName = "@description";
                paramDescription.Value = entity.Description;
                command.Parameters.Add(paramDescription);

                IDbDataParameter paramReferee = command.CreateParameter();
                paramReferee.ParameterName = "id_referee";
                paramReferee.Value = entity.Referee.Id;
                command.Parameters.Add(paramReferee);

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

    public Trial DeleteById(long entityId)
    {
        throw new NotImplementedException();
    }

    public Trial Update(Trial entity)
    {
        throw new NotImplementedException();
    }
}