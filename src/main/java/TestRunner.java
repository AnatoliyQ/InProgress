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

        Node testNode = Node.getInstance();
        testNode.startMining();
//        Thread.sleep(5000);
//        testNode.myServer.stop();





    }
}
