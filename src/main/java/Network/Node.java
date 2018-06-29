package Network;

import Core.*;
import Util.NetworkUtil;


import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Node {
    private ConcurrentHashMap<String, Transaction> transactionPool;
    public PublicKey minerKey;
    private static Node NODE;
    private Miner myMiner;
    public final Wallet myWallet;
    public Server myServer;
    public PeerHanler myPeer;
    private final int port = 12345;
    public Blockchain blockchain;
    public ArrayList<Peer> peers;
    private boolean mining;


    private Node(){
        transactionPool = new ConcurrentHashMap<>();
        myWallet = new Wallet();
        minerKey = myWallet.getPublicKey();
        myMiner = Miner.getInstance();
        myMiner.setMiner(minerKey);
        myServer = new Server(port);
        blockchain = Blockchain.getBlockchain();
        peers = new ArrayList<>();
        myServer.start();
        try {
            connectPeer();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static Node getInstance(){
        if(NODE ==null){
            NODE = new Node();
        }
        return NODE;
    }



    public void addTransactionToPool(Transaction tx){
        transactionPool.put(tx.getTransactionId(), tx);

        System.out.println("Done");
    }

    public void startMining (){
        mining = true;
        myMiner.startMiner();
    }

    public void stopMining(){
        if(mining){
        mining = false;
        myMiner.stopMining();
        } else {
            System.out.println("Mining not started");
        }

    }

    public ArrayList<Transaction> getAllTransactions (){
        ArrayList<Transaction> tempForSend = new ArrayList<>(transactionPool.values());
        transactionPool.clear();
        return tempForSend;

    }

    private void connectPeer() throws IOException {
//        String host = "192.168.1.126";
        String host = "localhost";
        int port = 12345;
        if (NetworkUtil.isReachableByPing(host)){
            Socket fSocket = new Socket(host, port);

            myPeer = new PeerHanler(fSocket);
            Thread peerThread = new Thread(myPeer);
            peerThread.start();

        } else {
            System.out.println("Host is not available");
        }


    }

    public void addMyTXout (TransactionOutput txOut){
        myWallet.addTxOut(txOut);
    }

    public static boolean checkNodeStatus (){
        return NODE != null;
    }

    public void stopNetwork(){
        try {
            if (mining) {
                stopMining();
            }
            myServer.stop();
            if (myPeer!=null) {
                myPeer.stop();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
