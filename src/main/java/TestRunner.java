import Core.*;
import Network.Peer;
import Network.Server;
import Util.StringUtil;

import java.net.Socket;
import java.util.ArrayList;

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

        Server newServer = new Server(12345);



        newServer.start();

        Socket socket = new Socket("localhost", 12345);

        Peer p = new Peer(socket);
        //Block b = new Block("testSend");
        //p.sendBlock(b);

    }
}
