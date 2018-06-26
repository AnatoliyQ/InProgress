package Core;

import java.util.ArrayList;

public class Blockchain {
    private static Blockchain blockchain;

    private ArrayList<Block> chain;

    private Blockchain (){
        chain = new ArrayList<>();

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



}
