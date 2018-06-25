package main.java.com.buildsim.init;

import main.java.com.buildsim.watcher.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EnvLoad extends HttpServlet {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private static volatile Watcher watcher = null;
    private static ExecutorService singleExecutor = null;

    @Override
    public void init() throws ServletException {
        super.init();

        // Load configuration file
        String configFilePath = this.getServletContext().getRealPath("WEB-INF\\classes\\main\\resources\\watchdog.config");
        WatchDogConfig.setConfigPath(configFilePath);

        LOG.info("Watch dog Config file path: " + configFilePath);

        try {
            watcher = new Watcher();
            singleExecutor = Executors.newSingleThreadExecutor();
            singleExecutor.execute(watcher);
        }catch (Throwable e){
            LOG.error("Start watcher failed: "+e.getMessage(), e);
        }
    }
}
