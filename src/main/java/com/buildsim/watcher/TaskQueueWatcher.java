package main.java.com.buildsim.watcher;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.java.com.buildsim.cloud.RedisAccess;
import main.java.com.buildsim.cloud.RedisAccessFactory;
import main.java.com.buildsim.email.Sender;
import main.java.com.buildsim.httpClientConnect.StatusReporter;
import main.java.com.buildsim.init.WatchDogConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class TaskQueueWatcher implements Runnable {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private static final long SLEEP = 1 * 60 * 1000;       // 1 minutes

    public TaskQueueWatcher() {
    }

    @Override
    public void run() {
        int queueNotEmpty = 0;
        while (true) {
            try {
                RedisAccess ra = RedisAccessFactory.getAccess();

                if(ra.lindx("TaskQueue", 0) != null){
                    StatusReporter.wakeUpSimulationCluster();
                    queueNotEmpty++;
                    if(queueNotEmpty==5){
                        StatusReporter.wakeUpBackupServer();
                        queueNotEmpty = 0;
                    }
                }else {
                    queueNotEmpty = 0;
                }

            try {
                Thread.sleep(SLEEP);
            } catch (InterruptedException e) {
            }
        }catch (Exception e){
                LOG.error(e.getMessage(), e);
            }
        }
    }
}
