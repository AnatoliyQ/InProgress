package Core;

import DB.Storage;
import Util.Address;


import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;


import static DB.StorageMaps.*;

public class  Wallet {
    public PrivateKey privateKey;
    public PublicKey publicKey;

    public HashMap<String,TransactionOutput> UTXO = new HashMap<>(); // key - transactionOutputId; Value - TransactionOutput


    public Wallet() {
        setKeys();
        this.UTXO = Storage.getInstance().getTxOut();

    }

    public float getBalance() {
        float total = 0;

        for (TransactionOutput tx : UTXO.values()){
            total += tx.getValue();
        }

        return total;
    }

    public Transaction sendFunds(PublicKey _recipient, float value ) {
        if(getBalance() < value) {
            System.out.println("Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }
        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

        float total = 0;

        for (TransactionOutput tx : UTXO.values()){

            total += tx.getValue();
            inputs.add(new TransactionInput(tx.id));
            if(total > value) break;
        }

        Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
        newTransaction.generateSignature(privateKey);

        for(TransactionInput input: inputs){
            UTXO.remove(input.transactionOutputId);
            Storage.getInstance().removeTXo(input.transactionOutputId);

        }



        return newTransaction;
    }


    public String getOwnAddress (){
        return Address.getAddress(this.publicKey);
    }

    private void setKeys(){
        if(Storage.getInstance().getAccount() == null){
            Account myAccount = new Account();
            Storage.getInstance().putToDB(ACCOUNT, myAccount, null);
            Storage.getInstance().addAccount(myAccount);
            this.publicKey = myAccount.getPublicKey();
            this.privateKey = myAccount.getPrivateKey();
        } else {
            Account myAccount = Storage.getInstance().getAccount();
            this.publicKey = myAccount.getPublicKey();
            this.privateKey = myAccount.getPrivateKey();
        }


    }


    public static void main(String[] args) throws Exception {

    }
}
