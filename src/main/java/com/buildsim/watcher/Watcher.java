package main.java.com.buildsim.watcher;

import com.google.gson.JsonArray;
import main.java.com.buildsim.email.Sender;
import main.java.com.buildsim.init.WatchDogConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Watcher implements Runnable {
    private static final long THRESHOLD = 45 * 60 * 1000;  // 45 minutes
    private static final long SLEEP = 5 * 60 * 1000;       // 5 minutes

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private Set<String> sent;

    public Watcher() {
        this.sent = new HashSet<>();
    }

    @Override
    public void run() {
        while (true) {
            try {
                String recordDir = WatchDogConfig.readProperty("storage");
                JsonArray hangSims = new JsonArray();

                File dir = new File(recordDir);
                File[] subDirs = dir.listFiles();
                for (File subDir : subDirs) {
                    if (subDir != null && subDir.exists() && subDir.isDirectory()) {
                        File[] logs = subDir.listFiles();
                        for (File log : logs) {
                            if (log != null && log.exists() && log.isFile()) {
                                if (System.currentTimeMillis() - log.lastModified() > THRESHOLD) {
                                    String fileName = log.getName();
                                    if(sent.add(fileName)) {
                                        hangSims.add(log.getName());
                                    }
                                }
                            }
                        }
                    }
                }

                if (hangSims.size() > 0) {
                    Sender sender = new Sender();
                    sender.sendEmail(
                            "Detect possible hanging simulation",
                            hangSims.toString(),
                            "haopeng.wang@buildsimhub.net",
                            null,
                            null,
                            null
                    );
                }
            } catch (Throwable e) {
                LOG.error(e.getMessage(), e);
            }

            try {
                Thread.sleep(SLEEP);
            } catch (InterruptedException e) {
            }
        }
    }
}
