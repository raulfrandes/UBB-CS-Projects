using System;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Threading;
using Google.Protobuf;
using services;
using Triathlon.Protocol;
using ParticipantDTO = model.DTOs.ParticipantDTO;
using Referee = model.Referee;
using Result = model.Result;
using TrialDTO = model.DTOs.TrialDTO;

namespace networking2;

public class ProtoWorker : IObserver
{
    private IServices _server;
    private TcpClient _connection;

    private NetworkStream _stream;
    private volatile bool _connected;
    private long _currentFilter;

    public ProtoWorker(IServices server, TcpClient connection)
    {
        _server = server;
        _connection = connection;
        try
        {
            _stream = connection.GetStream();
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
                Request request = Request.Parser.ParseDelimitedFrom(_stream);
                Response response = HandleRequest(request);
                if (response != null)
                {
                    SendResponse(response);
                    if (response.Type == Response.Types.Type.Error || request.Type == Request.Types.Type.Logout)
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
        try
        {
            SendResponse(ProtoUtils.CreateReceivePointsResponse(participants));
        }
        catch (Exception e)
        {
            throw new ServiceException("Sending error: " + e);
        }
    }

    public void PointsReceivedFiltered(List<ParticipantDTO> participants)
    {
        Console.WriteLine("Points received filtered");
        try
        {
            SendResponse(ProtoUtils.CreateReceivePointsFilteredResponse(participants));
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

    private Response HandleRequest(Request request)
    {
        Response response = null;
        Request.Types.Type requestType = request.Type;
        switch (requestType)
        {
            case Request.Types.Type.Login:
            {
                Console.WriteLine("Login request ...");
                string username = request.Username;
                try
                {
                    lock (_server)
                    { 
                        Referee referee = _server.Login(username, this);
                        return ProtoUtils.CreateLoginResponse(referee);
                    }
                }
                catch (ServiceException e)
                {
                    return ProtoUtils.CreateErrorResponse(e.Message);
                }
            }
            case Request.Types.Type.Logout:
            {
                Console.WriteLine("Logout request");
                long idReferee = request.Id;
                try
                {
                    lock (_server)
                    {
                        _server.Logout(idReferee, this);
                    }

                    return ProtoUtils.CreateOkResponse();
                }
                catch (ServiceException e)
                {
                    return ProtoUtils.CreateErrorResponse(e.Message);
                }
            }
            case Request.Types.Type.GetParticipants:
            {
                Console.WriteLine("GetParticipants request");
                try
                {
                    lock (_server)
                    {
                        List<ParticipantDTO> participants = _server.GetParticipantsAndPoints();
                        return ProtoUtils.CreateGetParticipantsResponse(participants);
                    }
                }
                catch (ServiceException e)
                {
                    return ProtoUtils.CreateErrorResponse(e.Message);
                }
            }
            case Request.Types.Type.GetTrials:
            {
                Console.WriteLine("GetTrials request");
                try
                {
                    lock (_server)
                    {
                        List<TrialDTO> trials = _server.GetTrialNames();
                        return ProtoUtils.CreateGetTrialsResponse(trials);
                    }
                }
                catch (ServiceException e)
                {
                    return ProtoUtils.CreateErrorResponse(e.Message);
                }
            }
            case Request.Types.Type.AddResult:
            {
                Console.WriteLine("AddResult request");
                Result result = ProtoUtils.GetResult(request);
                try
                {
                    lock (_server)
                    {
                        _server.AddResult(result);
                        return ProtoUtils.CreateOkResponse();
                    }
                }
                catch (ServiceException e)
                {
                    return ProtoUtils.CreateErrorResponse(e.Message);
                }
            }
            case Request.Types.Type.GetFilteredParticipants:
            {
                Console.WriteLine("GetFilteredParticipants request");
                long idTrial = request.Id;
                try
                {
                    lock (_server)
                    {
                        List<ParticipantDTO> participants = _server.FilterParticipantsByTrial(idTrial);
                        return ProtoUtils.CreateGetParticipantsResponse(participants);
                    }
                }
                catch (ServiceException e)
                {
                    return ProtoUtils.CreateErrorResponse(e.Message);
                }
            }
            case Request.Types.Type.SendFilter:
            {
                Console.WriteLine("SendFilter request");
                long idTrial = request.Id;
                try
                {
                    _currentFilter = idTrial;
                    return ProtoUtils.CreateOkResponse();
                }
                catch (ServiceException e)
                {
                    return ProtoUtils.CreateErrorResponse(e.Message);
                }
            }
        }

        return response;
    }

    private void SendResponse(Response response)
    {
        Console.WriteLine("sending response " + response);
        lock (_stream)
        {
            response.WriteDelimitedTo(_stream);
            _stream.Flush();
        }
    }
}