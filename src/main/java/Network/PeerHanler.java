package Network;

import Core.Block;
import Core.Blockchain;
import Core.Transaction;
import Network.Commands.Ping;
import Network.Commands.Test;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class PeerHanler implements Runnable {
    private static Socket peer;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private boolean runner;

    public PeerHanler(Socket peer) {
        PeerHanler.peer = peer;
    }

    @Override
    public void run() {
        runner = true;
        try {
            outputStream = new ObjectOutputStream(peer.getOutputStream());
            inputStream = new ObjectInputStream(peer.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (runner){
            try {
                listen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void listen() throws IOException {
        Object command;
            try{
                command = inputStream.readObject();
                if (command instanceof Ping){
                    System.out.println("Ping received" + peer.getRemoteSocketAddress().toString());
                    outputStream.writeObject(new Ping());
                    outputStream.flush();
                }  else if (command instanceof Transaction){
                    System.out.println("TX received by client");
                } else if(command instanceof Block){
                    System.out.println("Block received");
                    Block block = (Block) command;
                    Blockchain.getBlockchain().addBlock(block);
                } else if (command instanceof Test){
                    System.out.println("New network test passed");
                }

            } catch (SocketTimeoutException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (EOFException eofe) {
                Thread.currentThread().interrupt();
            }


        }

    public void stop(){
        runner = false;
        Thread.currentThread().interrupt();
        System.out.println("Stopping peer");
    }

    public void send(Object object){
        try {
            outputStream.writeObject(object);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return String.format("[%s:%s]", peer.getInetAddress(), peer.getPort());
    }
}
