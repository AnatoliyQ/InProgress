package Core;

import Network.Node;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

public class Miner implements Runnable {

    private Block block;
    private Thread minerThread;
    private PublicKey minerKey;
    private ArrayList<Transaction> uncTx;
    private int difficulty;

    private boolean shouldMine = false;

    private static Miner instance;

    public Miner(Block block, PublicKey minerKey, ArrayList<Transaction> uncTx, int difficulty){
        this.block = block;
        this.minerKey = minerKey;
        this.uncTx = uncTx;
        this.difficulty = difficulty;
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
        uncTx = Node.getInstance().getAllTransactions();


        minerThread = new Thread(this);
        minerThread.start();
    }


    @Override
    public void run() {

        block.addCoinbaseTx(25L, minerKey);
        block.addUnconfTx(uncTx);
            while (!shouldMine && !minerThread.isInterrupted()) {

                if (block.mineBlock(difficulty)) {
                    minerThread.interrupt();
                    shouldMine = false;
                }
            }




    }

    public void stopMining(){
        minerThread.interrupt();
        shouldMine = false;
    }
}
