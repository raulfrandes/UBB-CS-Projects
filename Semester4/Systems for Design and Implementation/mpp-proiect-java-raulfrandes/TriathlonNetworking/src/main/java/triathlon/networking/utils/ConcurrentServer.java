package triathlon.networking.utils;

import triathlon.networking.protocol.ClientWorker;
import triathlon.services.IServices;

import java.net.Socket;

public class ConcurrentServer extends AbsConcurrentServer{
    private IServices server;

    public ConcurrentServer(int port, IServices server) {
        super(port);
        this.server = server;
        System.out.println("Triathlon - ConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientWorker worker = new ClientWorker(server, client);
        return new Thread(worker);
    }
}
