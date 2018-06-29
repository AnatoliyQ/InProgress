package Core;

import Util.StringUtil;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;

import static Util.StringUtil.applySha256;

public class Block implements Serializable {
    private int hight;
    private String hash;
    private String previousHash;
    private String merkleRoot;
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private long timeStamp;
    private int nonce;
    private int difficulty;

    public Block(String previousHash ) {

        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();

        this.hash = calculateHash();
    }

    private String calculateHash() {
        String calculatedhash = applySha256(
                Integer.toString(hight)+
                        previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        merkleRoot+
                        Integer.toString(difficulty)
        );
        return calculatedhash;
    }


    public void addUnconfTx (Transaction tx){
        transactions.add(tx);
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void addCoinbaseTx(long coinbase, PublicKey minerKey){
        Transaction coinbaseTx = new Transaction(minerKey, coinbase);
        transactions.add(coinbaseTx);

    }

    public boolean mineBlock() {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDificultyString(difficulty); //Create a string with difficulty * "0"
        while(!hash.substring( 0, difficulty).equals(target)) {
            this.nonce ++;
            this.hash = calculateHash();
        }


        System.out.println("Block Mined!!! : " + hash);
        return true;
    }


    public String getHash(){
        return hash;
    }

    public int getHight(){
        return hight;
    }

    public void setHight(int hight){
        this.hight = hight;
    }

    public void setDifficulty (int difficulty){
        this.difficulty = difficulty;
    }

    public int getDifficulty(){
        return difficulty;
    }

    public static Block getGenesisBlock(){
        Block genesisBlock = new Block("Starting blockchain");
        genesisBlock.timeStamp = 1530045299L;
        genesisBlock.setHight(0);
        genesisBlock.hash = "fcd4b1ff9ae2e9230ddca60af6e0ee6a3625252b39bc01e0a3ceb45b2bcd28b6";
        genesisBlock.difficulty = 1;

        return genesisBlock;
    }

}
