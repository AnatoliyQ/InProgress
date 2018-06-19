package Network;

import Core.Block;
import Util.StringUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
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

    public void stop() throws IOException{
        runningServer = false;
        try {
            serverThread.interrupt();
            socket.close();
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
        System.out.println("Server Stopped");
    }


    public void listen() throws IOException, SocketTimeoutException{
        System.out.println("Server starting...");
        server = new ServerSocket(this.port);
        System.out.println("Server started on port " + this.port);

        Peer peer;
        server.setSoTimeout(10000);
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

                ObjectInputStream deserializer = new ObjectInputStream(socket.getInputStream());
            try {
                Block tets = (Block) deserializer.readObject();
                System.out.println(StringUtil.getJson(tets));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }
    }
}
