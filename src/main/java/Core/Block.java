package Core;

import Util.StringUtil;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;

import static Util.StringUtil.applySha256;

public class Block {
    public String hash;
    public String previousHash;
    public String merkleRoot;
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    public long timeStamp;
    public int nonce;
    private Transaction coinbaseTx;
    private int coinbase = 25;

    public Block(String previousHash ) {

        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();

        this.hash = calculateHash();
    }

    public String calculateHash() {
        String calculatedhash = applySha256(
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

    public static void main(String[] args) {

    }

}
