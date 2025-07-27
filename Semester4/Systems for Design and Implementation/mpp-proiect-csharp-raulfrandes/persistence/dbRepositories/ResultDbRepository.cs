using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SQLite;
using log4net;
using model;
using model.validator;
using persistence.interfaces;

namespace persistence.dbRepositories;

public class ResultDbRepository: IResultRepository
{
    private static readonly ILog Log = LogManager.GetLogger("ParticipantDBRepository");

    private IDictionary<string, string> _props;
    private IValidator<Result> _resultValidator;

    public ResultDbRepository(IDictionary<string, string> props, IValidator<Result> resultValidator)
    {
        Log.Info("Creating ResultDbRepository");
        this._props = props;
        this._resultValidator = resultValidator;
    }

    private Participant? FindParticipant(long idParticipant)
    {
        Log.InfoFormat("Entering FindParticipant with value {0}", idParticipant);
        IDbConnection connection = DbUtils.GetConnection(_props);

        try
        {
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from participants where id = @id";
                IDbDataParameter paramId = command.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = idParticipant;
                command.Parameters.Add(paramId);

                using (var dataReader = command.ExecuteReader())
                {
                    if (dataReader.Read())
                    {
                        long id = dataReader.GetInt64(0);
                        string name = dataReader.GetString(1);
                        string code = dataReader.GetString(2);
                        Participant participant = new Participant(name, code);
                        participant.Id = id;
                        Log.InfoFormat("Exiting FindParticipant with value {0}", participant);
                        return participant;
                    }
                    Log.InfoFormat("Exiting FindParticipant with value {0}", null);
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

    private Trial? FindTrial(long idTrial)
    {
        Log.InfoFormat("Entering FindTrial with value {0}", idTrial);
        IDbConnection connection = DbUtils.GetConnection(_props);

        try
        {
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from trials where id = @id";
                IDbDataParameter paramId = command.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = idTrial;
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
                        Log.InfoFormat("Exiting FindTrial with value {0}", trial);
                        return trial;
                    }
                    Log.InfoFormat("Exiting FindTrial with value {0}", null);
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
    
    public IEnumerable<Result> FindAll()
    {
        Log.InfoFormat("Entering FindAll");
        IDbConnection connection = DbUtils.GetConnection(_props);

        IList<Result> results = new List<Result>();
        try
        {
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from results";

                using (var dataReader = command.ExecuteReader())
                {
                    while (dataReader.Read())
                    {
                        long id = dataReader.GetInt64(0);
                        long idParticipant = dataReader.GetInt64(1);
                        Participant participant = FindParticipant(idParticipant)!;
                        long idTrial = dataReader.GetInt64(2);
                        Trial trial = FindTrial(idTrial)!;
                        int points = dataReader.GetInt32(3);
                        Result result = new Result(participant, trial, points);
                        result.Id = id;
                        results.Add(result);
                    }
                }
            }
            Log.InfoFormat("Exiting FindAll with value {0}", results);
            return results;
        }
        catch (SQLiteException e)
        {
            Log.Error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }

    public Result FindById(long entityId)
    {
        throw new NotImplementedException();
    }

    public Result? Save(Result entity)
    {
        _resultValidator.Validate(entity);
        Log.InfoFormat("Entering Save with value {0}", entity);
        IDbConnection connection = DbUtils.GetConnection(_props);
        try
        {
            using (var command = connection.CreateCommand())
            {
                command.CommandText =
                    "insert into results (id_participant, id_trial, points) values (@id_participant, @id_trial, @points)";

                IDbDataParameter paramParticipant = command.CreateParameter();
                paramParticipant.ParameterName = "@id_participant";
                paramParticipant.Value = entity.Participant.Id;
                command.Parameters.Add(paramParticipant);

                IDbDataParameter paramTrial = command.CreateParameter();
                paramTrial.ParameterName = "@id_trial";
                paramTrial.Value = entity.Trial.Id;
                command.Parameters.Add(paramTrial);

                IDbDataParameter paramPoints = command.CreateParameter();
                paramPoints.ParameterName = "@points";
                paramPoints.Value = entity.Points;
                command.Parameters.Add(paramPoints);

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

    public Result DeleteById(long entityId)
    {
        throw new NotImplementedException();
    }

    public Result Update(Result entity)
    {
        throw new NotImplementedException();
    }

    public IList<Result> FindByParticipant(long idParticipant)
    {
        Log.InfoFormat("Entering FindByParticipant with value {0}", idParticipant);
        IDbConnection connection = DbUtils.GetConnection(_props);

        IList<Result> results = new List<Result>();
        try
        {
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from results where id_participant = @id_participant";
                IDbDataParameter paramParticipant = command.CreateParameter();
                paramParticipant.ParameterName = "@id_participant";
                paramParticipant.Value = idParticipant;
                command.Parameters.Add(paramParticipant);

                using (var dataReader = command.ExecuteReader())
                {
                    while (dataReader.Read())
                    {
                        long id = dataReader.GetInt64(0);
                        Participant participant = FindParticipant(idParticipant)!;
                        long idTrial = dataReader.GetInt64(2);
                        Trial trial = FindTrial(idTrial)!;
                        int points = dataReader.GetInt32(3);
                        Result result = new Result(participant, trial, points);
                        result.Id = id;
                        results.Add(result);
                    }
                }
            }
            Log.InfoFormat("Exiting FindByParticipant with value {0}", results);
            return results;
        }
        catch (SQLiteException e)
        {
            Log.Error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }

    public IList<Result> FindByTrial(long idTrial)
    {
        Log.InfoFormat("Entering FindByTrial with value {0}", idTrial);
        IDbConnection connection = DbUtils.GetConnection(_props);

        IList<Result> results = new List<Result>();
        try
        {
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from results where id_trial = @id_trial";
                IDbDataParameter paramTrial = command.CreateParameter();
                paramTrial.ParameterName = "@id_trial";
                paramTrial.Value = idTrial;
                command.Parameters.Add(paramTrial);

                using (var dataReader = command.ExecuteReader())
                {
                    while (dataReader.Read())
                    {
                        long id = dataReader.GetInt64(0);
                        long idParticipant = dataReader.GetInt64(1);
                        Participant participant = FindParticipant(idParticipant)!;
                        Trial trial = FindTrial(idTrial)!;
                        int points = dataReader.GetInt32(3);
                        Result result = new Result(participant, trial, points);
                        result.Id = id;
                        results.Add(result);
                    }
                }
            }
            Log.InfoFormat("Exiting FindByTrial with value {0}", results);
            return results;
        }
        catch (SQLiteException e)
        {
            Log.Error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }
}