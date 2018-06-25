package Network;


import Network.Commands.NetworkCommand;
import Network.Commands.Ping;
import Network.Commands.Test;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;


public class Server {
    private final static int DELAY = 5000;
    private Timer timer;
    private int port;
    private ArrayList<Peer> peers;
    private ArrayList<ObjectOutputStream> connected;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    public Thread serverThread;
    private boolean runningServer;
    private HashMap<String, NetworkCommand> commands = new HashMap<>();
    private ServerSocket server;
    private Socket socket = null;
    private int inPacks = 0;
    private int outPacks = 0;

    public Server(int port) {
        this.port = port;
        connected = new ArrayList<>();
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

    public void start() {
        if (serverThread.isAlive()) {
            System.out.println("Server is already running.");
            return;
        }
        runningServer = true;
        serverThread.start();
    }

    public void stop() throws IOException {
        runningServer = false;
        try {
            serverThread.interrupt();
            outputStream.close();
            inputStream.close();
            server.close();
            socket.close();
        } catch (NullPointerException n) {
            System.out.println("Null pointer when closing server socket");
        }
        System.out.println("Server Stopped");
    }

    public void listen() throws IOException {
        System.out.println("Server starting...");
        server = new ServerSocket(this.port);
        System.out.println("Server started on port " + this.port);

        while (runningServer) {
            try {
                socket = server.accept();


                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());
                connected.add(outputStream);
                System.out.println("Passed Accept");
                System.out.println("Connection received from: " + socket.toString());

                pingSender();

                Thread readThread = new Thread(() -> {
                    while (runningServer) {
                        Object obj = null;
                        try {
                            obj = inputStream.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                        if (obj instanceof Ping) {
                            inPacks++;
                            System.out.println("Server reciev ping " + inPacks);
                        }
                        if (inPacks == 2) {
                            psBroadcast();
                        }

                    }
                });

                readThread.start();


            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                System.out.println("Exeptions block in server");
            }


        }
    }

    private void pingSender() {
        this.timer = new Timer(DELAY, e -> {
            try {

                if (Math.abs(inPacks - outPacks) <= 3) {
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
                try {
                    socket.close();
                    outputStream.close();
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (IOException ex2) {
                ex2.printStackTrace();
            }
        });

        this.timer.start();
    }

    public void psBroadcast() {
        for (int i = 0; i < connected.size(); i++) {
            ObjectOutputStream ob = connected.get(i);
            try {
                ob.writeObject(new Test());
                ob.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}



