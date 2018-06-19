package Network;

import Core.Transaction;

import java.security.PublicKey;
import java.util.PriorityQueue;
import java.util.Queue;

public class Node implements Runnable{
    private Queue<Transaction> transactionPool;
    PublicKey minerKey;


    public Node(PublicKey minerKey){
        this.minerKey = minerKey;
        transactionPool = new PriorityQueue<Transaction>();
    }

    public void addTransactionToPool(Transaction tx){
        transactionPool.add(tx);
    }






    @Override
    public void run() {

    }
}
