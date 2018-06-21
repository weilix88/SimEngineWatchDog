package main.java.com.buildsim.cloud;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.file.CloudFile;
import com.microsoft.azure.storage.file.CloudFileClient;
import com.microsoft.azure.storage.file.CloudFileDirectory;
import com.microsoft.azure.storage.file.CloudFileShare;
import main.java.com.buildsim.init.WatchDogConfig;
import main.java.com.buildsim.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

public class AzureFileUtil implements CloudFileUtil {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private CloudFileClient getFileClient() {
        String storageConnectionString =
                "DefaultEndpointsProtocol=https;"
                        + "AccountName=" + WatchDogConfig.readProperty("AzureFileStorageAccountName") + ";"
                        + "AccountKey=" + WatchDogConfig.readProperty("AzureFileStorageAccountKey") + ";"
                        + "EndpointSuffix=core.windows.net";

        try {
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            return storageAccount.createCloudFileClient();
        } catch (InvalidKeyException | URISyntaxException e) {
            LOG.error("Cannot get Azure file client, " + e.getMessage(), e);
        }

        return null;
    }

    public CloudFileDirectory getDirOnly(String share, String path) {
        CloudFileClient fileClient = getFileClient();
        if (fileClient == null) {
            return null;
        }

        CloudFileShare fileShare = null;
        try {
            fileShare = fileClient.getShareReference(share);
        } catch (URISyntaxException | StorageException e) {
            LOG.error("Cannot get file share " + share + ", " + e.getMessage(), e);
            return null;
        }

        try {
            fileShare.createIfNotExists();
        } catch (StorageException e) {
            LOG.error("Share create if not exist failed: " + share + ", " + e.getMessage(), e);
            return null;
        }

        CloudFileDirectory rootDir = null;
        try {
            rootDir = fileShare.getRootDirectoryReference();
        } catch (StorageException | URISyntaxException e) {
            LOG.error("Get root directory failed: " + share + ", " + e.getMessage(), e);
            return null;
        }

        if (StringUtil.isNullOrEmpty(path)) {
            return rootDir;
        }

        CloudFileDirectory dir;
        try {
            dir = rootDir.getDirectoryReference(path);
        } catch (URISyntaxException | StorageException e) {
            LOG.error("Get directory failed: " + share + ", " + path + ", " + e.getMessage(), e);
            return null;
        }

        return dir;
    }

    public CloudFileDirectory getDirCreateIfNotExist(String share, String path) {
        CloudFileDirectory dir = getDirOnly(share, path);

        if (dir != null) {
            try {
                dir.createIfNotExists();
            } catch (StorageException | URISyntaxException e) {
                LOG.error("Dir create if not exist failed: " + share + ", " + path + ", " + e.getMessage(), e);
                return null;
            }
        }

        return dir;
    }

    public CloudFile getCloudFile(String share, String path, String fileName) {
        CloudFileDirectory dir = getDirCreateIfNotExist(share, path);

        CloudFile cloudFile = null;
        try {
            cloudFile = dir.getFileReference(fileName);
        } catch (URISyntaxException | StorageException e) {
            LOG.error("Get cloud file failed: " + share + ", " + path + ", " + fileName + ", " + e.getMessage(), e);
        }

        return cloudFile;
    }

    public boolean uploadFile(String share, String path, File file, String fileName) {
        if (StringUtil.isNullOrEmpty(fileName)) {
            fileName = file.getName();
        }

        CloudFile cloudFile = getCloudFile(share, path, fileName);
        if (cloudFile == null) {
            return false;
        }

        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            cloudFile.upload(bis, file.length());
        } catch (URISyntaxException | StorageException | IOException e) {
            LOG.error("Upload failed: " + share + ", " + path + ", " + file.getName() + ", " + e.getMessage(), e);
            return false;
        }

        return true;
    }
}
