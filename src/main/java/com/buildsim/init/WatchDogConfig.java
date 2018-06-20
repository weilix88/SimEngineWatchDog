package main.java.com.buildsim.init;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WatchDogConfig {
    private static final Logger LOG = LoggerFactory.getLogger(WatchDogConfig.class);

    private static String ConfigPath = "";

    private static CompositeConfiguration properties = null;

    /**
     * USED ONLY on init phase, if used for testing/development purpose, make sure to
     * remove this function call before publish
     *
     * @param path
     */
    public static void setConfigPath(String path) {
        WatchDogConfig.ConfigPath = path;
        readConfig();
    }

    private static void readConfig() {
        properties = new CompositeConfiguration();
        properties.addConfiguration(new SystemConfiguration());
        try {
            properties.addConfiguration(new PropertiesConfiguration(ConfigPath));
        } catch (ConfigurationException e) {
            LOG.error("Read configuration file failed, file path: " + ConfigPath, e);
        }
    }

    public static String readProperty(String key) {
        if (properties == null) {
            readConfig();
        }

        return properties.getString(key);
    }
}
