using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SQLite;
using log4net;
using model;
using model.validator;
using persistence.interfaces;

namespace persistence.dbRepositories;

public class ParticipantDbRepository: IParticipantRepository
{
    private static readonly ILog Log = LogManager.GetLogger("ParticipantDBRepository");

    private IDictionary<String, string> _props;
    private IValidator<Participant> _participantValidator;

    public ParticipantDbRepository(IDictionary<string, string> props, IValidator<Participant> participantValidator)
    {
        Log.Info("Creating ParticipantDbRepository");
        this._props = props;
        this._participantValidator = participantValidator;
    }
    
    public IEnumerable<Participant> FindAll()
    {
        Log.InfoFormat("Entering FindAll");
        IDbConnection connection = DbUtils.GetConnection(_props);
        IList<Participant> participants = new List<Participant>();
        try
        {
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from participants";

                using (var dataReader = command.ExecuteReader())
                {
                    while (dataReader.Read())
                    {
                        long id = dataReader.GetInt64(0);
                        string name = dataReader.GetString(1);
                        string code = dataReader.GetString(2);
                        Participant participant = new Participant(name, code);
                        participant.Id = id;
                        participants.Add(participant);
                    }
                }
            }

            Log.InfoFormat("Exiting FindAll with value {0}", participants);
            return participants;
        }
        catch (SQLiteException e)
        {
            Log.Error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }

    public Participant? FindById(long entityId)
    {
        Log.InfoFormat("Entering FindById with value {0}", entityId);
        IDbConnection connection = DbUtils.GetConnection(_props);

        try
        {
            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select * from participants where id = @id";
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
                        string code = dataReader.GetString(2);
                        Participant participant = new Participant(name, code);
                        participant.Id = id;
                        Log.InfoFormat("Exiting FindById with value {0}", participant);
                        return participant;
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

    public Participant? Save(Participant entity)
    {
        _participantValidator.Validate(entity);
        Log.InfoFormat("Entering Save with value {0}", entity);
        IDbConnection connection = DbUtils.GetConnection(_props);
        try
        {
            using (var command = connection.CreateCommand())
            {
                command.CommandText =
                    "insert into participants (name, code) values (@name, @code)";

                IDbDataParameter paramName = command.CreateParameter();
                paramName.ParameterName = "@name";
                paramName.Value = entity.Name;
                command.Parameters.Add(paramName);

                IDbDataParameter paramCode = command.CreateParameter();
                paramCode.ParameterName = "@code";
                paramCode.Value = entity.Code;
                command.Parameters.Add(paramCode);

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

    public Participant DeleteById(long entityId)
    {
        throw new NotImplementedException();
    }

    public Participant Update(Participant entity)
    {
        throw new NotImplementedException();
    }
}