package Core;

import Util.StringUtil;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.Date;

public class TransactionOutput implements Serializable {
    public String id;
    public PublicKey reciepient; //also known as the new owner of these coins.
    public String addressReciepient;
    private float value; //the amount of coins they own
    public String parentTransactionId; //the id of the transaction this output was created in
    private long timeStamp;

    public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
        this.reciepient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.timeStamp = new Date().getTime();
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(reciepient)+Float.toString(value)+parentTransactionId+Long.toString(timeStamp));

    }



    public TransactionOutput(String addressReciepient, float value, String parentTransactionId) {
        this.addressReciepient = addressReciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(addressReciepient+Float.toString(value)+parentTransactionId);
    }

    public boolean isMine(PublicKey publicKey) {
        return (publicKey == reciepient);
    }

    public float getValue(){
        return this.value;
    }




}
