package Core;

import Network.Node;
import Util.StringUtil;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

public class Miner implements Runnable {

    private Block block;
    private Thread minerThread;
    private PublicKey minerKey;
    private ArrayList<Transaction> uncTx;


    private boolean shouldMine = false;

    private static Miner instance;

    public Miner(Block block, PublicKey minerKey, ArrayList<Transaction> uncTx, int difficulty){
        this.block = block;
        this.minerKey = minerKey;
        this.uncTx = uncTx;

    }

    private Miner(){

    }

    public static Miner getInstance(){
        if(instance == null){
            instance = new Miner();
        }

        return instance;
    }

    public void setMiner(PublicKey minerKey){
        this.minerKey = minerKey;

    }

//    public Block startMiner() {
//        minerThread = new Thread(this);
//        minerThread.start();
//        return block;
//    }

    public void startMiner (){
//        uncTx = Node.getInstance().getAllTransactions();


        minerThread = new Thread(this, "Mining Thread");
        minerThread.start();
    }


    @Override
    public void run() {
        while (!shouldMine && !minerThread.isInterrupted()) {
            block = new Block(Blockchain.getBlockchain().getLastBlock().getHash());
            block.addCoinbaseTx(25L, minerKey);
            uncTx = Node.getInstance().getAllTransactions();
            block.setDifficulty(Blockchain.getBlockchain().getLastBlock().getDifficulty()+1);
            block.setHight(Blockchain.getBlockchain().getLastBlock().getHight()+1);
            if (!(uncTx.size() == 0)) {
                for (Transaction tx : uncTx) {
                    if (tx.processTransaction()) {
                        block.addUnconfTx(tx);
                        uncTx.remove(tx);
                    } else {
                        uncTx.remove(tx);
                        System.out.println("Transaction is not valid !");
                    }

                }
                System.out.println("Start mining block " + block.getHight());
            } else {
                System.out.println("Start mining block " + block.getHight() + " with only coinbase transaction");
            }



            Boolean isMined = block.mineBlock();

            if (isMined){
                Blockchain.getBlockchain().addBlock(block);
                System.out.println("Block " + block.getHight() + " was added to chain");
            }


        }

    }



    public void stopMining(){
        minerThread.interrupt();
        shouldMine = false;
        System.out.println("Mining was stopped.");
    }

    public static void main(String[] args) {


    }
}
