package DB;

import Core.Account;
import Core.Block;
import Core.TransactionOutput;
import Util.*;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static DB.StorageMaps.ACCOUNT;
import static DB.StorageMaps.BLOCKS;
import static DB.StorageMaps.TRANSACTIONOUTPUT;

public class Storage {
    private static Storage instance;

    private String pathDB = FileManagment.getInternalPath("db");


    //path names for the database files
    private String pathAccountDB =  pathDB + "/account.db";
    private String pathTransactionOutputDB = pathDB + "/utxo.db";
    private String pathBlockDB = pathDB + "/block.db";

    // db objects
    private DB accountDB;
    private DB transactionOutputDB; // UTXO
    private DB blockDB;

    //db  stores
    private List<Account> accountMap;
    private HTreeMap<String, TransactionOutput> transactionOutputMap;
    private List<Block> blockMap;



    private Storage(){

        accountDB = getDB(pathAccountDB, false, true);
        transactionOutputDB = getDB(pathTransactionOutputDB, false, true);
        blockDB = getDB(pathBlockDB, false, true);

        accountMap = (List<Account>) accountDB.indexTreeList("map", Serializer.JAVA).createOrOpen();
        transactionOutputMap = transactionOutputDB.hashMap("map").keySerializer(Serializer.STRING).valueSerializer(Serializer.JAVA).createOrOpen();
        blockMap = (List<Block>) blockDB.indexTreeList("map", Serializer.JAVA).createOrOpen();



    }

    public static Storage getInstance(){
        if(instance == null){
            instance = new Storage();
        }
        return instance;
    }

    //Create db connection
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


    public HashMap<String, TransactionOutput> getAllTxOut(){
        HashMap<String, TransactionOutput> tempTXOU = new HashMap<>();
        tempTXOU.putAll(transactionOutputMap);

        return tempTXOU;
    }

    public void removeTXo (String transactionOutputId){
        transactionOutputMap.remove(transactionOutputId);
    }


    public void putToDB (StorageMaps map, Object objectKey, Object objectValue){
        if(map == StorageMaps.TRANSACTIONOUTPUT){
            String key = (String)objectKey;
            TransactionOutput value = (TransactionOutput) objectValue;
            transactionOutputMap.put(key , value);
        } else if(map == ACCOUNT){
            Account key = (Account)objectKey;
            accountMap.add(key);
        } else if (map == StorageMaps.BLOCKS){
            Block block = (Block) objectKey;
            blockMap.add(block);
        }
    }


    public Object getFromDB(StorageMaps map){
        if(map == ACCOUNT) {
            if (accountMap.size() == 0) {
                return null;
            }
            return accountMap.get(0);
        }else if( map == TRANSACTIONOUTPUT){
            HashMap<String, TransactionOutput> tempTXOU = new HashMap<>();
            tempTXOU.putAll(transactionOutputMap);
            return tempTXOU;
        } if (map == BLOCKS){
            ArrayList<Block> temp = new ArrayList<>(blockMap.size());
            temp.addAll(blockMap);
            return temp;
        }

        return null ;
    }

    public void commitAll(){
        accountDB.commit();
        transactionOutputDB.commit();
        blockDB.commit();

    }

    public void closeAll(){
        accountDB.close();
        transactionOutputDB.close();
        blockDB.close();

    }






    public static void main(String[] args) throws AddressFormatException {

    }

}
