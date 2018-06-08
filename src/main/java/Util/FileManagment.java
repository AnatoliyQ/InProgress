package Util;

import java.io.File;

public class FileManagment {
    private static String internalParentDirectory = "BC-files/";

    public static String getInternalPath(String path){
        File file = new File(getInternalPathRaw(path));
        boolean success = file.mkdirs();
        return file.getAbsolutePath();
    }

    public static String getInternalPathRaw(String path){
        return internalParentDirectory + path;
    }
}
