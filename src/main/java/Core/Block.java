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
    private Transaction coinbaseTx;
    private int coinbase = 25;

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
                        merkleRoot
        );
        return calculatedhash;
    }



    public void addUnconfTx(ArrayList<Transaction> uncTx){
        if(uncTx.size()==0) {
            transactions.addAll(uncTx);
        }
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void addCoinbaseTx(long coinbase, PublicKey minerKey){
        Transaction coinbaseTx = new Transaction(minerKey, coinbase);
        transactions.add(coinbaseTx);

    }

    public boolean mineBlock(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDificultyString(difficulty); //Create a string with difficulty * "0"
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }


        System.out.println("Block Mined!!! : " + hash);
        return true;
    }

    public void setHash(String hash){
        this.hash = hash;
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

    public static void main(String[] args) {

    }

}
