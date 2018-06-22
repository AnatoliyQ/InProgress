package Network;

import Core.Block;
import Network.Commands.NetworkCommand;
import Network.Commands.Ping;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

public class Server {
    private final static int DELAY = 5000;
    private Timer timer;
    private int port;
    private ArrayList<Peer> peers;
    private ArrayList<?> connected;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    public     Thread serverThread;
    private boolean runningServer;
    private HashMap<String, NetworkCommand> commands = new HashMap<>();
    private ServerSocket server;
    private Socket socket = null;
    private int inPacks = 0;
    private int outPacks = 0;

    public Server(int port){
        this.port = port;
        peers = new ArrayList<>();
        serverThread = new Thread(new Runnable() {
            public void run() {
                try {
                    listen();

                    System.out.println("Connection Ended");
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
            System.out.println("Null pointer when closing server socket");
        }
        System.out.println( "Server Stopped");
    }

    public void listen() throws IOException {
        System.out.println( "Server starting...");
        server = new ServerSocket(this.port);
        System.out.println( "Server started on port " + this.port);

        Peer peer;
        server.setSoTimeout(10000);
        Object obj;
        while(runningServer){
            try{
                socket = server.accept();
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());
                System.out.println("Passed Accept");
                peer = new Peer(socket);
                System.out.println( "Connection received from: " + peer.toString());
                peers.add(peer);
                System.out.println( "New peer: " + peer.toString());
//                outputStream.writeObject(new Ping());
//                outputStream.flush();

                this.timer = new Timer(DELAY, e -> {
                    try {
                        if (inPacks == outPacks) {
                            outputStream.writeObject(new Ping());
                            outPacks++;
                            System.out.println(outPacks + " out");
                        } else {
                            socket.close();
                            throw new SocketException();
                        }
                    } catch (SocketException ex1) {
                        System.out.println("packages not clash");

                        timer.stop();
                    }  catch (IOException ex2) {
                        ex2.printStackTrace();
                    }
                });

                this.timer.start();



                //outputStream.writeObject(new Ping());
                //this.outPacks++;
                System.out.println(outPacks + " out");



            } catch (SocketTimeoutException  e) {
                e.printStackTrace();
            }


        }
    }



}