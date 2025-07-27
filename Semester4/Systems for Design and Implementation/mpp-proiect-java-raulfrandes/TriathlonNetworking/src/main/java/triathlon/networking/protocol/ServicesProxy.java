package triathlon.networking.protocol;

import triathlon.model.DTOs.ParticipantDTO;
import triathlon.model.DTOs.TrialDTO;
import triathlon.model.Referee;
import triathlon.model.Result;
import triathlon.networking.protocol.dto.DTOUtils;
import triathlon.networking.protocol.dto.ResultDTO;
import triathlon.networking.protocol.requests.*;
import triathlon.networking.protocol.responses.*;
import triathlon.services.IObserver;
import triathlon.services.IServices;
import triathlon.services.ServiceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesProxy implements IServices {
    private String host;
    private int port;
    private IObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesProxy(String host, int port) {
        this.host = host;
        this.port = port;

        qresponses = new LinkedBlockingQueue<Response>();
    }

    @Override
    public Referee login(String username, IObserver client) throws ServiceException {
        initializedConnection();
        sendRequest(new LoginRequest(username));
        Response response = readResponse();
        if (response instanceof ErrorResponse errorResponse) {
            closeConnection();
            throw new ServiceException(errorResponse.getMessage());
        }
        this.client = client;
        LoginResponse loginResponse = (LoginResponse) response;
        return loginResponse.getReferee();
    }

    @Override
    public List<TrialDTO> getTrialNames() {
        sendRequest(new GetTrialsRequest());
        Response response = readResponse();
        if (response instanceof ErrorResponse errorResponse) {
            throw new ServiceException(errorResponse.getMessage());
        }
        GetTrialsResponse getTrialsResponse = (GetTrialsResponse) response;
        return Arrays.asList(getTrialsResponse.getTrials());
    }

    @Override
    public List<ParticipantDTO> getParticipantsAndPoints() throws ServiceException {
        sendRequest(new GetParticipantsRequest());
        Response response = readResponse();
        if (response instanceof ErrorResponse errorResponse) {
            throw new ServiceException(errorResponse.getMessage());
        }
        GetParticipantsResponse getParticipantsResponse = (GetParticipantsResponse) response;
        return Arrays.asList(getParticipantsResponse.getParticipants());
    }

    @Override
    public List<ParticipantDTO> filterParticipantsByTrial(Long idTrial) {
        sendRequest(new GetFilteredParticipantsRequest(idTrial));
        Response response = readResponse();
        if (response instanceof ErrorResponse errorResponse) {
            throw new ServiceException(errorResponse.getMessage());
        }
        GetParticipantsResponse getParticipantsResponse = (GetParticipantsResponse) response;
        return Arrays.asList(getParticipantsResponse.getParticipants());
    }

    @Override
    public void addResult(Result result) {
        ResultDTO resultDTO = DTOUtils.getDTO(result);
        sendRequest(new AddResultRequest(resultDTO));
        Response response = readResponse();
        if (response instanceof OkResponse) {
            return;
        }
        if (response instanceof ErrorResponse errorResponse) {
            throw new ServiceException(errorResponse.getMessage());
        }
    }

    @Override
    public void logout(Long idReferee, IObserver client) throws ServiceException {
        sendRequest(new LogoutRequest(idReferee));
        Response response = readResponse();
        if (response instanceof OkResponse) {
            closeConnection();
            return;
        }
        if (response instanceof ErrorResponse errorResponse) {
            throw new ServiceException(errorResponse.getMessage());
        }
    }

    @Override
    public void sendFilter(Long idTrial) {
        sendRequest(new SendFilterRequest(idTrial));
        Response response = readResponse();
        if (response instanceof OkResponse) {
            return;
        }
        if (response instanceof ErrorResponse errorResponse) {
            throw new ServiceException(errorResponse.getMessage());
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Request request) throws ServiceException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ServiceException("Error sending object " + e);
        }
    }

    private Response readResponse() throws ServiceException {
        Response response = null;
        try{
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializedConnection() throws ServiceException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(UpdateResponse updateResponse) {
        if (updateResponse instanceof ReceivePointsResponse receivePointsResponse) {
            ParticipantDTO[] participants = receivePointsResponse.getParticipants();
            List<ParticipantDTO> participantsList = Arrays.stream(participants).toList();
            System.out.println("Receive points");
            try {
                client.pointsReceived(participantsList);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
        if (updateResponse instanceof ReceivePointsFilteredResponse receivePointsFilteredResponse) {
            ParticipantDTO[] participants = receivePointsFilteredResponse.getParticipants();
            List<ParticipantDTO> participantsList = Arrays.stream(participants).toList();
            System.out.println("Receive points filtered");
            try {
                client.pointsReceivedFiltered(participantsList);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
    }

    private class ReaderThread implements Runnable {

        @Override
        public void run() {
            while(!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (response instanceof UpdateResponse) {
                        handleUpdate((UpdateResponse)  response);
                    } else {
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
