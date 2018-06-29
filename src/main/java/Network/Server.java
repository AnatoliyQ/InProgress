package Network;


import DB.Storage;
import Network.Commands.Test;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Server {
    private int port;
    private ArrayList<Peer> peers;
    private ArrayList<ObjectOutputStream> connected;
    ArrayList<ClientHandler> connections;
    public Thread serverThread;
    private volatile boolean runningServer;

    private ServerSocket server;
    private Socket socket = null;




    public Server(int port) {
        this.port = port;
        connected = new ArrayList<>();
        peers = new ArrayList<>();
        connections = new ArrayList<>();
        serverThread = new Thread(() -> {
            try {
                listen();

                System.out.println("Connection Ended");
            } catch (SocketException sce){
                System.out.println("server socket exeption");
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
//            socket.close();
            Storage.getInstance().commitAll();
            Storage.getInstance().closeAll();
        } catch (NullPointerException n) {
            n.printStackTrace();
            System.out.println("Null pointer when closing server socket");
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





//                outputStream = new ObjectOutputStream(socket.getOutputStream());
//                inputStream = new ObjectInputStream(socket.getInputStream());
//                connected.add(outputStream);
                System.out.println("Passed Accept");
                System.out.println("Connection received from: " + socket.toString());

//                pingSender();

//                readThread = new Thread(() -> {
//                    pingSender();
//                    while (true) {
//                        Object obj;
//                        try {
//                            obj = inputStream.readObject();
//
//                            if (obj instanceof Ping) {
//                                inPacks++;
//                                System.out.println("Server reciev ping " + inPacks);
//                            } else if (obj instanceof Block) {
//                                System.out.println("Block received");
//                            }
//                        } catch (IOException | ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }
//
//
//                    }
//                });
//
//                readThread.start();

            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                System.out.println("Exeptions block in server");
            } catch (SocketException sc){}



        }
    }

//    private void pingSender() {
//        this.timer = new Timer(DELAY, e -> {
//            try {
//
//                if (Math.abs(inPacks - outPacks) <= 3) {
//                    outputStream.writeObject(new Ping());
//                    outPacks++;
//                    System.out.println(outPacks + " out");
//                } else {
//                    socket.close();
//                    throw new SocketException();
//                }
//            } catch (SocketException ex1) {
//                System.out.println("packages not clash");
//                timer.stop();
//                try {
//                    Thread.currentThread().interrupt();
//                    outputStream.close();
//                    inputStream.close();
//                    socket.close();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            } catch (IOException ex2) {
//                ex2.printStackTrace();
//            }
//        });
//
//        this.timer.start();
//    }

    public void psBroadcast(Object object) {
        for (ClientHandler connection : connections) {
            connection.sendObject(object);
        }
    }


}



