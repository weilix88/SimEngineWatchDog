package main.java.com.buildsim.cloud;

import java.io.File;

public interface CloudFileUtil {
    boolean uploadFile(String share, String path, File file, String fileName);
}
