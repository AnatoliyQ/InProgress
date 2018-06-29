package Core;


import DB.Storage;
import Network.Node;
import Util.BlockValitator;

import java.util.ArrayList;

import static DB.StorageMaps.BLOCKS;


public class Blockchain {
    private static Blockchain blockchain;
    public final Object monitor;

    private ArrayList<Block> chain;

    private Blockchain (){
        monitor = new Object();
        chain = new ArrayList<Block>();
        initializeChain();
        synchronizeChain();

    }

    public static Blockchain getBlockchain (){
        if (blockchain == null){
            blockchain = new Blockchain();
        }
        return blockchain;
    }

    public Block getLastBlock (){
        synchronized (monitor) {
            return chain.get(chain.size() - 1);
        }
    }

    private void initializeChain(){
        ArrayList<Block> blockchain = new ArrayList<>();
        blockchain.addAll((ArrayList<Block>) Storage.getInstance().getFromDB(BLOCKS));
        if (blockchain.size() > 0) {
            chain.addAll(blockchain);
        } else {
            blockchain.add(Block.getGenesisBlock());
            chain.addAll(blockchain);
        }
        System.out.println("Blocks loaded from DB");
    }

    private void synchronizeChain (){

    }


    public void addBlock(Block block){
        synchronized (monitor) {
            if (BlockValitator.isValid(block)) {
                if (!chain.contains(block)) {
                    checkIsTXforMe(block);
                    chain.add(block);
                    Storage.getInstance().putToDB(BLOCKS, block, null);
                    Node.getInstance().myServer.psBroadcast(block);
                }
            }
        }
    }

    private void checkIsTXforMe (Block block){
        ArrayList<Transaction> transactions = block.getTransactions();
        for (Transaction tx: transactions) {
            ArrayList<TransactionOutput> outputs = tx.outputs;
            for (TransactionOutput txOut:outputs) {
                if (txOut.isMine(Node.getInstance().minerKey)){
                    Node.getInstance().addMyTXout(txOut);
                    System.out.println("Tx added to miner " + txOut.getValue());
                }
            }
        }
    }


}
