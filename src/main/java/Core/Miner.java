package Core;

import java.security.PublicKey;
import java.util.ArrayList;

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

    public Block startMiner() {
        minerThread = new Thread(this);
        minerThread.start();
        return block;
    }


    @Override
    public void run() {
//        try{
        block.addCoinbaseTx(25L, minerKey);
        block.addUnconfTx(uncTx);
            while (!shouldMine && !minerThread.isInterrupted()) {

                if (block.mineBlock(difficulty)) {
                    minerThread.interrupt();
                    shouldMine = false;
                }
            }

//        } catch (InterruptedException e){
//            System.out.println("Thread is interrupted");
//        }


    }

    public void stopMining(){
        minerThread.interrupt();
        shouldMine = false;
    }
}
