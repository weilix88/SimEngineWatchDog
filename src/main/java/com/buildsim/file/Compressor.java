package main.java.com.buildsim.file;

import main.java.com.buildsim.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class Compressor {
    private static final Logger LOG = LoggerFactory.getLogger(Compressor.class);

    public static File compressGZIP(File input, String fileName){
        File zipFile = FileUtil.createTempFile(fileName);

        byte[] buffer = new byte[1024];

        try(FileOutputStream fos = new FileOutputStream(zipFile);
            GZIPOutputStream zos = new GZIPOutputStream(fos);
            FileInputStream fis = new FileInputStream(input)){
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            zos.finish();
        }catch(IOException ex){
            LOG.error(ex.getMessage(), ex);
            return null;
        }

        return zipFile;
    }
}
