package Network;

import Core.*;
import Util.NetworkUtil;
import Util.StringUtil;
import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Node {
    private ConcurrentHashMap<String, Transaction> transactionPool;
    private PublicKey minerKey;
    private static Node NODE;
    private Miner myMiner;
    private final Wallet myWallet;
    private Block currentBlock;
    private int blockNumber;
    private int COINBASE;
    public Server myServer;
    private Peer myPeer;
    private final int port = 12345;
    public Blockchain blockchain;
    public ArrayList<Peer> peers;


    private Node(){
        transactionPool = new ConcurrentHashMap<>();
        myWallet = new Wallet();
        minerKey = myWallet.getPublicKey();
        myMiner = Miner.getInstance();
        myServer = new Server(port);
        blockchain = Blockchain.getBlockchain();

    }

    public static Node getInstance(){
        if(NODE ==null){
            NODE = new Node();
        }
        NODE.myServer.start();
        try {
            NODE.connectPeer();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    private void connectPeer() throws IOException {
//        String host = "123.78.96.784";
        String host = "localhost";
        int port = 12345;
        if (NetworkUtil.isReachableByPing(host)){
            Socket fSocket = new Socket(host, port);
            Peer myPeer = new Peer(fSocket);
            peers.add(myPeer);

        } else {
            System.out.println("Host is not available");
        }


    }

    public void addBlock (Block block){

    }





    public static void main(String[] args) throws IOException {
        Server test = new Server(12345);
        test.start();
//        Boolean check = Node.isReachableByPing("123.78.96.784");
//        System.out.println(check);


    }







}
