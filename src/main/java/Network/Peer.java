package Network;

import Core.Transaction;
import Network.Commands.Ping;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class Peer {
    private Thread peerThread;
    public Socket socket;
    public ObjectOutputStream out;
    public ObjectInputStream in;

    public Peer(Socket socket)  {
        this.socket = socket;
        try {
            in = new ObjectInputStream(this.socket.getInputStream());
            out = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        peerThread = new Thread(() -> {
            try {
                listen();
                System.out.println( "Closing connection to " + socket.getInetAddress() + ":" + socket.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        peerThread.start();
    }


    public void listen() throws IOException {
        Object command;
        while(true){
            //System.out.println("Listening for commands");
            try{
                command = in.readObject();
                if (command instanceof Ping){
                    System.out.println("Ping received");
                    out.writeObject(new Ping());
                    out.flush();
                }  else if (command instanceof Transaction){
                    System.out.println("TX received by client");
                }

            } catch (SocketTimeoutException | ClassNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public String toString() {
        return String.format("[%s:%s]", socket.getInetAddress(), socket.getPort());
    }





}