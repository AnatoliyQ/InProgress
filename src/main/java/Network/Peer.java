package Network;

import Core.Block;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;

public class Peer {
    private Thread peerThread;
    public Socket socket;

    public DataOutputStream out;
    public DataInputStream in;

    public Peer(String address, int port)  {
        try {
            this.socket = new Socket(address, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        peerThread = new Thread(() -> {
            try {
//                socket.connect(new InetSocketAddress("localhost", 12345));
                listen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        peerThread.start();
    }

    public Peer(Socket socket)  {

            this.socket = socket;


        peerThread = new Thread(() -> {
            try {
//                socket.connect(new InetSocketAddress("localhost", 12345));
                listen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        peerThread.start();
    }

    public void listen() throws IOException {
        String command;
        while(true){
            //System.out.println("Listening for commands");
            try{
                DataInputStream in = new DataInputStream(this.socket.getInputStream());
                DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
//                command = receive(in);
//                send(serve(command), out);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            }


        }
    }

    public void sendBlock (Block block){
        try {
            //DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
            ObjectOutputStream outObj = new ObjectOutputStream(this.socket.getOutputStream());
            outObj.writeObject(block);
            outObj.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






}
