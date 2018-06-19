package DB;

import Core.Account;
import Core.TransactionOutput;
import Util.*;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.HashMap;
import java.util.List;

public class Storage {
    private static Storage instance;

    private String pathDB = FileManagment.getInternalPath("db");


    //private String pathTransactionsDB = pathDB + "/tx.db";
    private String pathAccountDB =  pathDB + "/account.db";
    private String pathTransactionOutputDB = pathDB + "/utxo.db";

    private DB accountDB;
    private DB transactionsDB;
    private DB transactionOutputDB; // UTXO

    private List<Account> accauntMap;
    private HTreeMap<String, TransactionOutput> transactionOutputMap;
    //private HashMap<String, TransactionOutput> transactionOutputMap;
    //private HTreeMap<byte[], byte[]> transactionsMap;

    private List<TransactionOutput> transactionsMap;

    protected Storage(){

        accountDB = getDB(pathAccountDB, false, true);
        //transactionsDB =  getDB(pathTransactionsDB,false,true);
        transactionOutputDB = getDB(pathTransactionOutputDB, false, true);



        //transactionsMap = transactionsDB.hashMap("map").keySerializer(Serializer.INTEGER).valueSerializer(Serializer.JAVA).createOrOpen();

        //tx = (List<TransactionOutput>) transactionsDB.indexTreeList("MyList", Serializer.JAVA).createOrOpen();

        //transactionsMap = (List<TransactionOutput>) transactionsDB.indexTreeList("test").serializer(Serializer.JAVA).createOrOpen();
        //transactionsMap = (List<TransactionOutput>) transactionsDB.indexTreeList("map", Serializer.JAVA).createOrOpen();
        accauntMap = (List<Account>) accountDB.indexTreeList("map", Serializer.JAVA).createOrOpen();
        transactionOutputMap = transactionOutputDB.hashMap("map").keySerializer(Serializer.STRING).valueSerializer(Serializer.JAVA).createOrOpen();






        //accountMap = accountDB.hashMap("map").keySerializer(Serializer.BYTE_ARRAY).valueSerializer(Serializer.BYTE_ARRAY).createOrOpen();
        //accountMap = accountDB.hashMap("map").keySerializer(SerializerJava.
        //blocksMap = blocksDB.hashMap("mapBlock").keySerializer(Serializer.INTEGER).valueSerializer(Serializer.JAVA).createOrOpen();
    }

    public static Storage getInstance(){
        if(instance == null){
            instance = new Storage();
        }
        return instance;
    }

    private DB getDB(String path, boolean safeMode, boolean autoCleanup){
        DBMaker.Maker dbConnection = DBMaker.fileDB(path).fileMmapEnable();
        if(safeMode){
            dbConnection = dbConnection.transactionEnable();
        }
        if (autoCleanup){
            dbConnection.closeOnJvmShutdown();
        }
        return dbConnection.make();
    }

    public TransactionOutput getTxIn(int i){
        return transactionsMap.get(i);
    }

    public HashMap<String, TransactionOutput> getTxOut (){
        HashMap<String, TransactionOutput> test = new HashMap<>();
        test.putAll(transactionOutputMap);
        return test;
        //return transactionOutputMap;
    }

    public void removeTXo (String transactionOutputId){
        transactionOutputMap.remove(transactionOutputId);
    }

    public boolean put (TransactionOutput tx){
        return transactionsMap.add(tx);
    }

    public boolean putToDB (StorageMaps map, Object objectKey, Object objectValue){
        if(map == StorageMaps.TRANSACTIONOUTPUT){
            String key = (String)objectKey;
            TransactionOutput value = (TransactionOutput) objectValue;
            transactionOutputMap.put(key , value);
            return true;
        } else if(map == StorageMaps.ACCOUNT){
            Account key = (Account)objectKey;
            accauntMap.add(key);
        }
        return false;
    }

    public void addAccount(Account acc){
        accauntMap.add(acc);
    }

    public Account getAccount (){
        if(accauntMap.size() == 0){
            return null;
        }
        return accauntMap.get(0);
    }

    /*
    public DB put(StorageMaps map, byte[] key, byte[] value){
        if(map == StorageMaps.ACCOUNTS){
            accountsMap.put(key, value);
            return accountsDB;
        }else if(map == StorageMaps.BLOCKS){
            blocksMap.put(key,value);
            return blocksDB;
        }else if(map == StorageMaps.TRANSACTIONS){
            transactionsMap.put(key,value);
            return transactionsDB;
        }else if(map == StorageMaps.CONTRACT_CODE){
            contractCodeMap.put(key,value);
            return contractCodeDB;
        }else if(map == StorageMaps.CONTRACT_STATE){
            contractStatesMap.put(key,value);
            return contractStatesDB;
        }
        else return null;
    }*/

    /*
    public byte[] get(StorageMaps map, byte[] key){
        if(map == StorageMaps.ACCOUNTS){
            return accountsMap.get(key);
        }else if(map == StorageMaps.BLOCKS){
            return blocksMap.get(key);
        }else if(map == StorageMaps.TRANSACTIONS){
            return transactionsMap.get(key);
        }else if(map == StorageMaps.CONTRACT_CODE){
            return contractCodeMap.get(key);
        }else if(map == StorageMaps.CONTRACT_STATE){
            return contractStatesMap.get(key);
        }
        return null;
    }*/

    public static void main(String[] args) throws AddressFormatException {

    }

}
