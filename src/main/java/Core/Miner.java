package Core;

import Network.Node;

import java.security.PublicKey;
import java.util.ArrayList;

public class Miner implements Runnable {

    private Block block;
    private Thread minerThread;
    private PublicKey minerKey;
    private ArrayList<Transaction> uncTx;
    Blockchain blockchain;


    private boolean shouldMine = false;

    private static Miner instance;


    private Miner(){
        blockchain = Blockchain.getBlockchain();
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


    public void startMiner (){
        shouldMine = true;
        minerThread = new Thread(this, "Mining Thread");
        minerThread.start();
    }


    @Override
    public void run() {
        while (shouldMine && !minerThread.isInterrupted()) {
            Block lastBlock;
            synchronized (blockchain.monitor) {
                lastBlock = blockchain.getLastBlock();
            }
            block = new Block(lastBlock.getHash());
            block.addCoinbaseTx(25L, minerKey);
            uncTx = Node.getInstance().getAllTransactions();
            block.setDifficulty(lastBlock.getDifficulty()+1);
            block.setHight(lastBlock.getHight()+1);
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
                synchronized (blockchain.monitor) {
                    Blockchain.getBlockchain().addBlock(block);
                    System.out.println("Block " + block.getHight() + " was added to chain");
                }
            }


        }

    }

    public void stopMining(){
        minerThread.interrupt();
        shouldMine = false;
        System.out.println("Mining was stopped.");
    }

}
