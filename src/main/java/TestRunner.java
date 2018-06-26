import Core.*;
import DB.Storage;
import DB.StorageMaps;
import Network.Node;
import Network.Peer;
import Network.Server;
import Util.StringUtil;
import com.sun.xml.internal.stream.util.ThreadLocalBufferAllocator;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import static DB.StorageMaps.TRANSACTIONOUTPUT;

public class TestRunner {
    public static void main(String[] args) throws Exception {
//        ArrayList<Transaction> tx = new ArrayList<>();
//        Wallet myWallet = new Wallet();
//        Block genesis = new Block("0");
//        Miner miner = new Miner(genesis, myWallet.publicKey, tx, 2 );
//        Block minde  = miner.startMiner();
//        Thread.sleep(100);
//        System.out.println(StringUtil.getJson(minde.getTransactions().get(0).outputs.get(0)));
//        //System.out.println(minde.getTransactions().get(0));
//
//
//
//        //System.out.println(myWallet.getBalance());
        // _______________________________

//        Server newServer = new Server(12345);
//
//
//
//        newServer.start();
//
//        Socket socket = new Socket("localhost", 12345);
//
//        Peer p = new Peer(socket);

//        Node testNode = Node.getInstance();
//        testNode.myServer.start();
//        Thread.sleep(10000);
//        Wallet w = new Wallet();
//        Transaction tx  = new Transaction(w.publicKey, 60L);
//        testNode.addTransactionToPool(tx);

        TransactionOutput txo = new TransactionOutput("Test", 10L, "test2");

        Storage.getInstance().putToDB(TRANSACTIONOUTPUT, txo.id, txo);

        HashMap<String, TransactionOutput> test = new HashMap<>();
        test = (HashMap<String, TransactionOutput>) Storage.getInstance().getFromDB(TRANSACTIONOUTPUT);

        System.out.println(test.size());

        System.out.println(test.keySet());
        System.out.println(StringUtil.getJson(test.get("78ff6e078cab6bae051ea2e87bc4bb2268669ce4855e04e6e7af3e16081ebcb3")));








    }
}
