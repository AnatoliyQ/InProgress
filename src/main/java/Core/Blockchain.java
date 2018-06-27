package Core;


import DB.Storage;
import DB.StorageMaps;

import java.util.ArrayList;
import java.util.HashMap;

public class Blockchain {
    private static Blockchain blockchain;

    private ArrayList<Block> chain;

    private Blockchain (){
        chain = new ArrayList<>();
        initializeChain();
        synchronizeChain();

//        chain.addAll()
    }

    public static Blockchain getBlockchain (){
        if (blockchain == null){
            blockchain = new Blockchain();
        }
        return blockchain;
    }

    public Block getLastBlock (){
        return chain.get(chain.size() - 1);
    }

    private void initializeChain(){
        ArrayList<Block> blockchain = new ArrayList<>();
        blockchain.addAll((ArrayList<Block>)Storage.getInstance().getFromDB(StorageMaps.BLOCKS));
        if(blockchain.size()>0){
            chain = blockchain;
        } else {
            blockchain.add(Block.getGenesisBlock());
            chain = blockchain;
        }
        System.out.println("Blocks loaded from DB");
    }

    private void synchronizeChain (){

    }

    public void synchronizeBloks(){}



}
