package main.java.com.buildsim.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class EnvLoad extends HttpServlet{
    private static final long serialVersionUID = 8760402126588557090L;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init() throws ServletException {
        super.init();

        // Load configuration file
        String configFilePath = this.getServletContext().getRealPath("/WEB-INF/watchdog.config");
        WatchDogConfig.setConfigPath(configFilePath);

        LOG.info("Watch dog Config file path: "+configFilePath);
    }
}
