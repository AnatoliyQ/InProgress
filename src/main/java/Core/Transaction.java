package Core;

import Util.StringUtil;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction implements Serializable{
    private String type;
    private   String transactionId; //Contains a hash of transaction*
    private PublicKey sender; //Senders address/public key.
    private PublicKey reciepient; //Recipients address/public key.
    private float value; //Contains the amount we wish to send to the recipient.
    private byte[] signature; //This is to prevent anybody else from spending funds in our wallet.


    public ArrayList<TransactionInput> inputs = new ArrayList<>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();

    private static int sequence = 0; //A rough count of how many transactions have been generated

    public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
        this.type = "Regular";
    }


    public Transaction (PublicKey to, float value){
        this.reciepient = to;
        this.value = value;
        this.inputs = null;
        TransactionOutput txOut = new TransactionOutput(reciepient, value, "CoinBaseTransaction");
        this.outputs.add(txOut);
        this.signature = StringUtil.stringToByteArray("CoinBase");
        this.transactionId = StringUtil.applySha256(reciepient + Float.toString(value) + StringUtil.getJson(txOut));
        this.type = "Coinbase";
    }

    public boolean processTransaction() {
        if(!verifySignature()) {
            System.out.println("Transaction Signature failed to verify");
            return false;
        }

        //Generate transaction outputs:
        float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
        transactionId = calulateHash();
        outputs.add(new TransactionOutput( this.reciepient, value,transactionId)); //send value to recipient
        outputs.add(new TransactionOutput( this.sender, leftOver,transactionId)); //send the left over 'change' back to sender


        return true;
    }

    public float getInputsValue() {

        float total = 0;
        if(getType().equals("Coinbase")){
            total = 25;
        } else {
            for (TransactionInput i : inputs) {
                if (i.UTXO == null)
                    continue; //if Core.Transaction can't be found skip it, This behavior may not be optimal.
                total += i.UTXO.getValue();
            }
        }
        return total;
    }


    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
        signature = StringUtil.applyECDSASig(privateKey,data);
    }

    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
        return StringUtil.verifyECDSASig(sender, data, signature);
    }


    // This Calculates the transaction hash (which will be used as its Id)
    private String calulateHash() {
        sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(reciepient) +
                        Float.toString(value) + sequence
        );
    }

    public String getTransactionId (){
        return transactionId;
    }

    public String getType(){
        return type;
    }

    public PublicKey getAddressSender(){
        return sender;
    }

    public PublicKey getReciepient(){
        return reciepient;
    }

    public static void main(String[] args) {

    }


}
