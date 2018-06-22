package Network;

import Network.Commands.Ping;
import com.sun.org.apache.xpath.internal.SourceTree;

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
        peerThread = new Thread(new Runnable() {
            public void run() {
                try {
                    listen();
                    System.out.println( "Closing connection to " + socket.getInetAddress() + ":" + socket.getPort());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        peerThread.start();
    }


    public void listen() throws IOException {
        Object command;
        while(true){
            //System.out.println("Listening for commands");
            try{
                ObjectInputStream in = new ObjectInputStream(this.socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(this.socket.getOutputStream());
                command = in.readObject();
                if (command instanceof Ping){
                    System.out.println("Ping received");
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