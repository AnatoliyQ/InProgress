package Util;

import Core.Block;
import Core.Blockchain;
import Core.Transaction;

import java.util.ArrayList;

public class BlockValitator {
    private static Blockchain blockchain;

    public static boolean isValid(Block block) {
        blockchain = Blockchain.getBlockchain();
        synchronized (blockchain.monitor) {
            System.out.println(StringUtil.getJson(block));
//            System.out.println(StringUtil.getJson(block.getTransactions()));

            if (!(block.getHight() - blockchain.getLastBlock().getHight() == 1)) {
                System.out.println("Validation process: Invalid block hight");
                return false;
            }

            if (!(block.getDifficulty() - blockchain.getLastBlock().getDifficulty() == 1)) {
                System.out.println("Validation process: Wrong block difficulte. Should be " + (blockchain.getLastBlock().getDifficulty() + 1));
                return false;
            }

            ArrayList<Transaction> transactions = new ArrayList<>(block.getTransactions());
            //transactions = block.getTransactions();
            if (!(transactions.get(0).getType().equals("Coinbase"))) {
                System.out.println("Validation process: First transaction should be Coinbase transaction");
                return false;
            }

            if (!(transactions.get(0).getInputsValue() == 25)) {
                System.out.println("Validation process: Coinbase transaction should be for 25 ");
                return false;
            }

            return true;
        }
    }
}