package Network;

import Core.Miner;
import Core.Transaction;
import Core.Wallet;
import Util.StringUtil;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Node {
    private ConcurrentHashMap<String, Transaction> transactionPool;
    PublicKey minerKey;
    private static Node NODE;
    private Miner myMiner;
    private final Wallet myWallet;
    private int blockNumber;
    private int COINBASE;
    public Server myServer;
    private Peer myPeer;
    private final int port = 12345;


    private Node(){
        transactionPool = new ConcurrentHashMap<>();
        myWallet = new Wallet();
        myMiner = Miner.getInstance();
        myServer = new Server(port);

    }

    public static Node getInstance(){
        if(NODE ==null){
            NODE = new Node();
        }
        NODE.myServer.start();
        return NODE;
    }

    public void addTransactionToPool(Transaction tx){
        transactionPool.put(tx.getTransactionId(), tx);

        System.out.println("Done");
    }

    public void startMining (Boolean flag){

        myMiner.startMiner();
    }

    public ArrayList<Transaction> getAllTransactions (){
        ArrayList<Transaction> tempForSend = new ArrayList<>(transactionPool.values());
        transactionPool.clear();
        return tempForSend;

    }

    public static void main(String[] args) {

    }







}
