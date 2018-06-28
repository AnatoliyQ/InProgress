package Network;

import Core.Block;
import Core.Blockchain;
import Core.Transaction;
import Network.Commands.Ping;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class Peer {
    private Thread peerThreadListen;
    private Thread peerThreadWriter;
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

        peerThreadListen = new Thread(() -> {
            try {
                listen();
                System.out.println( "Closing connection to " + socket.getInetAddress() + ":" + socket.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        peerThreadListen.start();

//        peerThreadWriter = new Thread(() -> {
//            try {
//
//                System.out.println( "Closing connection to " + socket.getInetAddress() + ":" + socket.getPort());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//        peerThreadWriter.start();


    }




    public void listen() throws IOException {
        Object command;
        while(true){
            try{
                command = in.readObject();
                if (command instanceof Ping){
                    System.out.println("Ping received");
                    out.writeObject(new Ping());
                    out.flush();
                }  else if (command instanceof Transaction){
                    System.out.println("TX received by client");
                } else if(command instanceof Block){
                    System.out.println("Blcok received");
                    Block block = (Block) command;
                    Blockchain.getBlockchain().addBlock(block);
                }

            } catch (SocketTimeoutException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (EOFException eofe) {
                peerThreadListen.interrupt();
            }


        }
    }

    @Override
    public String toString() {
        return String.format("[%s:%s]", socket.getInetAddress(), socket.getPort());
    }





}