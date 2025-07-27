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

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientWorker implements Runnable, IObserver {
    private IServices server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    private volatile Long currentFilter;

    public ClientWorker(IServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Object response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse((Response) response);
                    if (response instanceof ErrorResponse) {
                        output.flush();
                        connected = false;
                    }
                    if (request instanceof LogoutRequest) {
                        output.flush();
                        connected = false;
                    }
                }
            } catch (EOFException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    @Override
    public void pointsReceived(List<ParticipantDTO> participants) throws ServiceException {
        System.out.println("Points received");
        ParticipantDTO[] participantsArray = participants.toArray(ParticipantDTO[]::new);
        try {
            sendResponse(new ReceivePointsResponse(participantsArray));
        } catch (IOException e) {
            throw new ServiceException("Sending error: " + e);
        }
    }

    @Override
    public void pointsReceivedFiltered(List<ParticipantDTO> participants) throws ServiceException {
        System.out.println("Points received filtered");
        ParticipantDTO[] participantsArray = participants.toArray(ParticipantDTO[]::new);
        try {
            sendResponse(new ReceivePointsFilteredResponse(participantsArray));
        } catch (IOException e) {
            throw new ServiceException("Sending error: " + e);
        }
    }

    @Override
    public Long getFilter() {
        return currentFilter;
    }

    private Response handleRequest(Request request) {
        Response response = null;
        if (request instanceof LoginRequest loginRequest) {
            System.out.println("Login request ...");
            String username = loginRequest.getUsername();
            try {
                Referee referee = server.login(username, this);
                return new LoginResponse(referee);
            } catch (ServiceException e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof GetParticipantsRequest) {
            System.out.println("Get participants request");
            try {
                List<ParticipantDTO> participants = server.getParticipantsAndPoints();
                ParticipantDTO[] participantsArray = participants.toArray(ParticipantDTO[]::new);
                return new GetParticipantsResponse(participantsArray);
            } catch (ServiceException e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof GetTrialsRequest) {
            System.out.println("Get trials request");
            try {
                List<TrialDTO> trials = server.getTrialNames();
                TrialDTO[] trialsArray = trials.toArray(TrialDTO[]::new);
                return new GetTrialsResponse(trialsArray);
            } catch (ServiceException e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof AddResultRequest addResultRequest) {
            System.out.println("Add result request");
            ResultDTO resultDTO = addResultRequest.getResult();
            Result result = DTOUtils.getFromDTO(resultDTO);
            try {
                server.addResult(result);
                return new OkResponse();
            } catch (ServiceException e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof GetFilteredParticipantsRequest getFilteredParticipantsRequest) {
            System.out.println("Get filtered participants request");
            Long idTrial = getFilteredParticipantsRequest.getIdTrial();
            try {
                List<ParticipantDTO> participants = server.filterParticipantsByTrial(idTrial);
                ParticipantDTO[] participantsArray = participants.toArray(ParticipantDTO[]::new);
                return new GetParticipantsResponse(participantsArray);
            } catch (ServiceException e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof LogoutRequest logoutRequest) {
            Long idReferee = logoutRequest.getId();
            try {
                server.logout(idReferee, this);
                return new OkResponse();
            } catch (ServiceException e) {
                return new ErrorResponse(e.getMessage());
            }
        }

        if (request instanceof SendFilterRequest sendFilterRequest) {
            Long idTrial = sendFilterRequest.getIdTrial();
            try {
                currentFilter = idTrial;
                return new OkResponse();
            } catch (ServiceException e) {
                return new ErrorResponse(e.getMessage());
            }
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response " + response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }
}
