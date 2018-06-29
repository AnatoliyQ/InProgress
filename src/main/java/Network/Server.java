package Network;


import DB.Storage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class Server {
    private int port;
    ArrayList<ClientHandler> connections;
    public Thread serverThread;
    private volatile boolean runningServer;

    private ServerSocket server;
    private Socket socket = null;




    public Server(int port) {
        this.port = port;
        connections = new ArrayList<>();
        serverThread = new Thread(() -> {
            try {
                listen();
                System.out.println("Connection Ended");
            } catch (IOException e) {
                e.printStackTrace();
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
            for (ClientHandler connection : connections) {
                connection.close();
            }
            serverThread.interrupt();
            server.close();
            Storage.getInstance().commitAll();
            Storage.getInstance().closeAll();
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
        System.out.println("Server Stopped");
    }

    public void listen() throws IOException {
        System.out.println("Server starting...");
        server = new ServerSocket(this.port);
        System.out.println("Server started on port " + this.port + "  runningServer = "+runningServer);

        while (runningServer) {
            try {
                socket = server.accept();
                final ClientHandler clinet = new ClientHandler(socket);
                final Thread thread = new Thread(clinet);
                thread.start();
                connections.add(clinet);

                System.out.println("Passed Accept");
                System.out.println("Connection received from: " + socket.toString());

            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                System.out.println("Exeptions block in server");
            } catch (SocketException sc){}



        }
    }

    public void psBroadcast(Object object) {
        for (ClientHandler connection : connections) {
            connection.sendObject(object);
        }
    }

}



