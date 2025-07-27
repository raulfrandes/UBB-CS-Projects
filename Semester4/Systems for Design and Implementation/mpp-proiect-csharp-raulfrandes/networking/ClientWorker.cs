using System;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Threading;
using model;
using model.DTOs;
using networking.dtos;
using services;

namespace networking;

public class ClientWorker : IObserver
{
    private IServices _server;
    private TcpClient _connection;

    private NetworkStream _stream;
    private IFormatter _formatter;
    private volatile bool _connected;
    private long _currentFilter;

    public ClientWorker(IServices server, TcpClient connection)
    {
        _server = server;
        _connection = connection;
        try
        {
            _stream = connection.GetStream();
            _formatter = new BinaryFormatter();
            _connected = true;
        }
        catch (Exception e)
        {
            Console.WriteLine(e.StackTrace);
        }
    }
    
    public virtual void Run()
    {
        while (_connected)
        {
            try
            {
                object request = _formatter.Deserialize(_stream);
                object response = HandleRequest((IRequest)request);
                if (response != null)
                {
                    SendResponse((IResponse)response);
                    if (response is ErrorResponse or LogoutResponse)
                    {
                        _stream.Flush();
                        _connected = false;
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }

            try
            {
                Thread.Sleep(5000);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }

        try
        {
            _stream.Close();
            _connection.Close();
        }
        catch (Exception e)
        {
            Console.WriteLine("Error " + e);
        }
    }
    
    public void PointsReceived(List<ParticipantDTO> participants)
    {
        Console.WriteLine("Points received");
        ParticipantDTO[] participantsArray = participants.ToArray();
        try
        {
            SendResponse(new ReceivePointsResponse(participantsArray));
        }
        catch (Exception e)
        {
            throw new ServiceException("Sending error: " + e);
        }
    }

    public void PointsReceivedFiltered(List<ParticipantDTO> participants)
    {
        Console.WriteLine("Points received filtered");
        ParticipantDTO[] participantsArray = participants.ToArray();
        try
        {
            SendResponse(new ReceivePointsFilteredResponse(participantsArray));
        }
        catch (Exception e)
        {
            throw new ServiceException("Sending error: " + e);
        }
    }

    public long GetFilter()
    {
        return _currentFilter;
    }

    private IResponse HandleRequest(IRequest request)
    {
        IResponse response = null;
        if (request is LoginRequest)
        {
            Console.WriteLine("Login request ...");
            LoginRequest loginRequest = (LoginRequest)request;
            string username = loginRequest.Username;
            try
            {
                lock (_server)
                { 
                    Referee referee = _server.Login(username, this);
                    return new LoginResponse(referee);
                }
            }
            catch (ServiceException e)
            {
                _connected = false;
                return new ErrorResponse(e.Message);
            }
        }

        if (request is LogoutRequest)
        {
            Console.WriteLine("Logout request");
            LogoutRequest logoutRequest = (LogoutRequest)request;
            long idReferee = logoutRequest.Id;
            try
            {
                lock (_server)
                {
                    _server.Logout(idReferee, this);
                }

                return new LogoutResponse();
            }
            catch (ServiceException e)
            {
                return new ErrorResponse(e.Message);
            }
        }

        if (request is GetParticipantsRequest)
        {
            Console.WriteLine("GetParticipants request");
            try
            {
                lock (_server)
                {
                    List<ParticipantDTO> participants = _server.GetParticipantsAndPoints();
                    ParticipantDTO[] participantsArray = participants.ToArray();
                    return new GetParticipantsResponse(participantsArray);
                }
            }
            catch (ServiceException e)
            {
                return new ErrorResponse(e.Message);
            }
        }

        if (request is GetTrialsRequest)
        {
            Console.WriteLine("GetTrials request");
            try
            {
                lock (_server)
                {
                    List<TrialDTO> trials = _server.GetTrialNames();
                    TrialDTO[] trialsArray = trials.ToArray();
                    return new GetTrialsResponse(trialsArray);
                }
            }
            catch (ServiceException e)
            {
                return new ErrorResponse(e.Message);
            }
        }

        if (request is AddResultRequest)
        {
            Console.WriteLine("AddResult request");
            AddResultRequest addResultRequest = (AddResultRequest)request;
            ResultDTO resultDto = addResultRequest.Result;
            Result result = DTOUtils.GetFromDTO(resultDto);
            try
            {
                lock (_server)
                {
                    _server.AddResult(result);
                    return new OkResponse();
                }
            }
            catch (ServiceException e)
            {
                return new ErrorResponse(e.Message);
            }
        }

        if (request is GetFilteredParticipantsRequest)
        {
            Console.WriteLine("GetFilteredParticipants request");
            GetFilteredParticipantsRequest getFilteredParticipantsRequest = (GetFilteredParticipantsRequest)request;
            long idTrial = getFilteredParticipantsRequest.IdTrial;
            try
            {
                lock (_server)
                {
                    List<ParticipantDTO> participants = _server.FilterParticipantsByTrial(idTrial);
                    ParticipantDTO[] participantsArray = participants.ToArray();
                    return new GetParticipantsResponse(participantsArray);
                }
            }
            catch (ServiceException e)
            {
                return new ErrorResponse(e.Message);
            }
        }

        if (request is SendFilterRequest)
        {
            Console.WriteLine("SendFilter request");
            SendFilterRequest sendFilterRequest = (SendFilterRequest)request;
            long idTrial = sendFilterRequest.IdTrial;
            try
            {
                _currentFilter = idTrial;
                return new OkResponse();
            }
            catch (ServiceException e)
            {
                return new ErrorResponse(e.Message);
            }
        }

        return response;
    }

    private void SendResponse(IResponse response)
    {
        Console.WriteLine("sending response " + response);
        lock (_stream)
        {
            _formatter.Serialize(_stream, response);
            _stream.Flush();
        }
    }
}