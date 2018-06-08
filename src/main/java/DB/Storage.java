package DB;

import Util.FileManagment;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerJava;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Storage {
    private static Storage instance;

    private String pathDB = FileManagment.getInternalPath("db");

    private String pathKeys = pathDB + "/keys.db";

    private DB accountDB;

    private HTreeMap<PublicKey, PrivateKey> accountMap;

    protected Storage(){
        accountDB = getDB("pathBlocksDB", false, true);


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

}
