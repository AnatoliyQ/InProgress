package Network;

import Core.Block;
import Core.Blockchain;
import Network.Commands.GetHight;
import Network.Commands.Height;
import Network.Commands.Ping;
import Network.Commands.Test;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable {
    private static Socket client;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private int inPacks = 0;
    private int outPacks = 0;
    private final static int DELAY = 5000;
    private Timer timer;


    public ClientHandler(Socket client) {
        ClientHandler.client = client;
    }

    @Override
    public void run() {
        try {
            outputStream = new ObjectOutputStream(client.getOutputStream());
            inputStream = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            Object recivedObject;
            pingSender();
            while (!client.isClosed()) {
//                pingSender();
                recivedObject = inputStream.readObject();
                if (recivedObject instanceof Ping) {
                    inPacks++;
                    System.out.println("Server reciev ping " + inPacks + client.getRemoteSocketAddress().toString());
                } else if (recivedObject instanceof Block) {
                    System.out.println("Block received from sever");
                    Block block = (Block) recivedObject;
                    synchronized (Blockchain.getBlockchain().monitor) {
                        Blockchain.getBlockchain().addBlock(block);
                    }
                } else if(recivedObject instanceof GetHight){
                    Height currentHight = new Height(Blockchain.getBlockchain().getLastBlock().getHight());
                    outputStream.writeObject(currentHight);
                    outputStream.flush();
                } else if(recivedObject instanceof Test) {
                    System.out.println("Test object come to server");
                }

            }


        } catch (SocketException sce){
            System.out.println("socet exeption client");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


// for broadcast
    public synchronized void sendObject(Object object){
        try {
            outputStream.writeObject(object);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
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
                    close();
                }
            } catch (SocketException ex1) {
                System.out.println("packages not clash");
                    close();
            } catch (IOException ex2) {
                ex2.printStackTrace();
            }
        });

        this.timer.start();
    }

    public synchronized void close(){
        if(!client.isClosed()){
            try {
                timer.stop();
                outputStream.close();
                inputStream.close();
                client.close();
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
