package main.java.com.buildsim.util;

import main.java.com.buildsim.file.SelfDestroyFile;
import main.java.com.buildsim.init.WatchDogConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileUtil {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    private static final Object CREATE_DIR_LOCK = new Object();
    private static final Object CREATE_FILE_LOCK = new Object();

    public static void checkFolderCreateIfNotExist(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            synchronized (CREATE_DIR_LOCK) {
                if (!folder.exists()) {
                    folder.mkdirs();
                }
            }
        }
    }

    public static File checkFileCreateIfNotExist(String path){
        File file = new File(path);

        if(!file.exists()){
            synchronized (CREATE_FILE_LOCK) {
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    }catch(IOException e){
                        LOG.error(e.getMessage(), e);
                    }
                }
            }
        }

        return file;
    }

    public static void appendToFile(String path, String content) {
        File file = checkFileCreateIfNotExist(path);

        try (FileOutputStream fis = new FileOutputStream(file, true);
             OutputStreamWriter osw = new OutputStreamWriter(fis, "utf-8");
             BufferedWriter bw = new BufferedWriter(osw)) {
            bw.write(content);
            bw.write(System.lineSeparator());
            bw.flush();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public static File createTempFile(String fileName) {
        File res;

        String tmpfolder = WatchDogConfig.readProperty("tmpfolder");
        String randomFolderPath = tmpfolder + Hasher.hash(System.currentTimeMillis()+"", HashMethod.MD5) + "\\";

        File randomFolder = new File(randomFolderPath);
        randomFolder.mkdir();

        res = new SelfDestroyFile(randomFolderPath + fileName, true);

        return res;
    }
}
