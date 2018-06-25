package main.java.com.buildsim.httpClientConnect;

import main.java.com.buildsim.init.WatchDogConfig;

public class StatusReporter {
    public static void wakeUpSimulationCluster(){
        String url = WatchDogConfig.readProperty("SimClusterURL");

        HttpClient sender = new HttpClient();
        sender.setup(url + "ReceiveSimRequest");
        sender.send(true);
    }
}
