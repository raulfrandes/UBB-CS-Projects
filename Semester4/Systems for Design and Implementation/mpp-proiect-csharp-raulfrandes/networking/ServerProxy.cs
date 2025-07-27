using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Threading;
using model;
using model.DTOs;
using networking.dtos;
using services;

namespace networking;

public class ServerProxy : IServices
{
    private string _host;
    private int _port;

    private IObserver _client;
    
    private NetworkStream _stream;
    private IFormatter _formatter;
    private TcpClient _connection;
    
    private Queue<IResponse> _responses;
    private volatile bool _finished;
    private EventWaitHandle _waitHandle;

    public ServerProxy(string host, int port)
    {
        _host = host;
        _port = port;
        _responses = new Queue<IResponse>();
    }
    
    public Referee Login(string username, IObserver client)
    {
        InitializeConnection();
        SendRequest(new LoginRequest(username));
        IResponse response = ReadResponse();
        if (response is ErrorResponse)
        {
            CloseConnection();
            ErrorResponse errorResponse = (ErrorResponse)response;
            throw new ServiceException(errorResponse.Message);
        }
        _client = client;
        LoginResponse loginResponse = (LoginResponse)response;
        return loginResponse.Referee;
    }

    public List<TrialDTO> GetTrialNames()
    {
        SendRequest(new GetTrialsRequest());
        IResponse response = ReadResponse();
        if (response is ErrorResponse)
        {
            CloseConnection();
            ErrorResponse errorResponse = (ErrorResponse)response;
            throw new ServiceException(errorResponse.Message);
        }
        GetTrialsResponse getTrialsResponse = (GetTrialsResponse)response;
        return getTrialsResponse.Trials.ToList();
    }

    public List<ParticipantDTO> GetParticipantsAndPoints()
    {
        SendRequest(new GetParticipantsRequest());
        IResponse response = ReadResponse();
        if (response is ErrorResponse)
        {
            CloseConnection();
            ErrorResponse errorResponse = (ErrorResponse)response;
            throw new ServiceException(errorResponse.Message);
        }
        GetParticipantsResponse getParticipantsResponse = (GetParticipantsResponse)response;
        return getParticipantsResponse.Participants.ToList();
    }

    public List<ParticipantDTO> FilterParticipantsByTrial(long idTrial)
    {
        SendRequest(new GetFilteredParticipantsRequest(idTrial));
        IResponse response = ReadResponse();
        if (response is ErrorResponse)
        {
            CloseConnection();
            ErrorResponse errorResponse = (ErrorResponse)response;
            throw new ServiceException(errorResponse.Message);
        }
        GetParticipantsResponse getParticipantsResponse = (GetParticipantsResponse)response;
        return getParticipantsResponse.Participants.ToList();
    }

    public void AddResult(Result result)
    {
        ResultDTO resultDto = DTOUtils.GetDTO(result);
        SendRequest(new AddResultRequest(resultDto));
        IResponse response = ReadResponse();
        if (response is OkResponse)
        {
            return;
        }
        if (response is ErrorResponse)
        {
            CloseConnection();
            ErrorResponse errorResponse = (ErrorResponse)response;
            throw new ServiceException(errorResponse.Message);
        }
    }

    public void Logout(long idReferee, IObserver client)
    {
        SendRequest(new LogoutRequest(idReferee));
        IResponse response = ReadResponse();
        CloseConnection();
        if (response is ErrorResponse)
        {
            ErrorResponse errorResponse = (ErrorResponse)response;
            throw new ServiceException(errorResponse.Message);
        }
    }

    public void SendFilter(long idTrial)
    {
        SendRequest(new SendFilterRequest(idTrial));
        IResponse response = ReadResponse();
        if (response is OkResponse)
        {
            return;
        }
        if (response is ErrorResponse)
        {
            CloseConnection();
            ErrorResponse errorResponse = (ErrorResponse)response;
            throw new ServiceException(errorResponse.Message);
        }
    }

    private void CloseConnection()
    {
        _finished = true;
        try
        {
            _stream.Close();
            _connection.Close();
            _waitHandle.Close();
            _client = null;
        }
        catch (Exception e)
        {
            Console.WriteLine(e.StackTrace);
        }
    }

    private void SendRequest(IRequest request)
    {
        try
        {
            _formatter.Serialize(_stream, request);
            _stream.Flush();
        }
        catch (Exception e)
        {
            throw new ServiceException("Error sending object " + e);
        }
    }

    private IResponse ReadResponse()
    {
        IResponse response = null!;
        try
        {
            _waitHandle.WaitOne();
            lock (_responses)
            {
                response = _responses.Dequeue();
            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e.StackTrace);
        }

        return response;
    }

    private void InitializeConnection()
    {
        try
        {
            _connection = new TcpClient(_host, _port);
            _stream = _connection.GetStream();
            _formatter = new BinaryFormatter();
            _finished = false;
            _waitHandle = new AutoResetEvent(false);
            StartReader();
        }
        catch (Exception e)
        {
            Console.WriteLine(e.StackTrace);
        }
    }

    private void StartReader()
    {
        Thread tw = new Thread(Run);
        tw.Start();
    }

    private void HandleUpdate(IUpdateResponse updateResponse)
    {
        if (updateResponse is ReceivePointsResponse)
        {
            ReceivePointsResponse receivePointsResponse = (ReceivePointsResponse)updateResponse;
            ParticipantDTO[] participantsArray = receivePointsResponse.Participants;
            List<ParticipantDTO> participants = participantsArray.ToList();
            Console.WriteLine("Receive points");
            try
            {
                _client.PointsReceived(participants);
            }
            catch (ServiceException e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }

        if (updateResponse is ReceivePointsFilteredResponse)
        {
            ReceivePointsFilteredResponse receivePointsFilteredResponse = (ReceivePointsFilteredResponse)updateResponse;
            ParticipantDTO[] participantsArray = receivePointsFilteredResponse.Participants;
            List<ParticipantDTO> participants = participantsArray.ToList();
            Console.WriteLine("Receive points filtered");
            try
            {
                _client.PointsReceivedFiltered(participants);
            }
            catch (ServiceException e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }
    }

    public virtual void Run()
    {
        while (!_finished)
        {
            try
            {
                object response = _formatter.Deserialize(_stream);
                Console.WriteLine("response received " + response);
                if (response is IUpdateResponse)
                {
                    HandleUpdate((IUpdateResponse)response);
                }
                else
                {
                    lock (_responses)
                    {
                        _responses.Enqueue((IResponse)response);
                    }

                    _waitHandle.Set();
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("Logout with success");
            }
        }
    }
}