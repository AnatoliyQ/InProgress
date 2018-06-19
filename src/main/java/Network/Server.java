package Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class Server {

    private int port;
    private ArrayList<Peer> peers;
    public Thread serverThread;
    private ServerSocket server;
    private Socket socket = null;
    private boolean runningServer;


    public Server (int port){
        this.port = port;
        peers = new ArrayList<>();
        serverThread = new Thread(() -> {
            try {
                listen();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }

    public void start(){
        if(serverThread.isAlive()){
            System.out.println("Server is already running.");
            return;
        }
        runningServer = true;
        serverThread.start();
    }


    public void listen() throws IOException, SocketTimeoutException{
        System.out.println("Server starting...");
        server = new ServerSocket(this.port);
        System.out.println("Server started on port " + this.port);

        Peer peer;
        server.setSoTimeout(100);
        while(runningServer){
            try{
                socket = server.accept();

                peer = new Peer(socket);
                System.out.println("Connection received from: " + peer.toString());
                peers.add(peer);
                System.out.println("New peer: " + peer.toString());
            } catch (SocketTimeoutException e) {
                //e.printStackTrace();
            }


        }
    }
}
