package main.java.com.buildsim.httpClientConnect;

import main.java.com.buildsim.init.WatchDogConfig;
import main.java.com.buildsim.util.StringUtil;

public class StatusReporter {
    public static void wakeUpSimulationCluster(){
        String url = WatchDogConfig.readProperty("SimClusterURL");

        HttpClient sender = new HttpClient();
        sender.setup(url + "ReceiveSimRequest");
        sender.send(true);
    }

    public static void wakeUpBackupServer(){
        String url = WatchDogConfig.readProperty("backupSimURL");
        if(!StringUtil.isNullOrEmpty(url)){
            HttpClient sender = new HttpClient();
            sender.setup(url + "ReceiveSimRequest");
            sender.send(true);
        }
    }
}
