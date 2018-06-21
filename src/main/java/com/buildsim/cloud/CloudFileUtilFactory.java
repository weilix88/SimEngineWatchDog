package main.java.com.buildsim.cloud;

import main.java.com.buildsim.init.WatchDogConfig;

public class CloudFileUtilFactory {
    public static CloudFileUtil getInstance(){
        String platform = WatchDogConfig.readProperty("platform");
        switch (platform){
            case "azure":
                return new AzureFileUtil();
        }

        return null;
    }
}
