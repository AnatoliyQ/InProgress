
import Util.StringUtil;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static Util.StringUtil.applySha256;

public class Block {
    public String hash;
    public String previousHash;
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
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

    private static String getMerkleRoot(ArrayList<Transaction> transactions){
        int count = transactions.size();

        List<String> previousTreeLayer = new ArrayList<String>();
        for(Transaction transaction : transactions) {
            previousTreeLayer.add(transaction.transactionId);
        }
        List<String> treeLayer = previousTreeLayer;

        while(count > 1) {
            treeLayer = new ArrayList<String>();
            for(int i=1; i < previousTreeLayer.size(); i+=2) {
                treeLayer.add(applySha256(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
            }
            count = treeLayer.size();
            previousTreeLayer = treeLayer;
        }

        String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
        return merkleRoot;

    }

    private Transaction getCoinbaseTx(int coinbase, PublicKey minerKey){
        Transaction coinbaseTx = new Transaction(null, minerKey, coinbase, null );

//        coinbase
        return coinbaseTx;
    }

}
