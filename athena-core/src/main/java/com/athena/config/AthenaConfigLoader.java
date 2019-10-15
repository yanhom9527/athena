package com.athena.config;

import static com.athena.util.ConfigUtil.addSeparator;

import com.athena.log.RecordLog;
import com.athena.util.AppNameUtil;
import com.athena.util.ConfigUtil;
import com.athena.util.StringUtil;
import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * <p>The loader that responsible for loading common configurations.</p>
 *
 * @author mukong
 * @since 1.7.0
 */
public final class AthenaConfigLoader {

    public static final String SENTINEL_CONFIG = "athena.config.file";

    private static final String DIR_NAME = "logs" + File.separator + "csp";
    private static final String USER_HOME = "user.home";

    private static final String DEFAULT_SENTINEL_CONFIG_FILE = "classpath:athena.properties";

    private static Properties properties = new Properties();

    static {
        load();
    }

    private static void load() {
        String fileName = System.getProperty(SENTINEL_CONFIG);
        if (StringUtil.isBlank(fileName)) {
            fileName = DEFAULT_SENTINEL_CONFIG_FILE;
        }

        Properties p = ConfigUtil.loadProperties(fileName);

        // Compatible with legacy config file path.
        if (p == null) {
            String path = addSeparator(System.getProperty(USER_HOME)) + DIR_NAME + File.separator;
            fileName = path + AppNameUtil.getAppName() + ".properties";
            File file = new File(fileName);
            if (file.exists()) {
                p = ConfigUtil.loadProperties(fileName);
            }
        }

        if (p != null && !p.isEmpty()) {
            RecordLog.info("[AthenaConfigLoader] Loading config from " + fileName);
            properties.putAll(p);
        }

        for (Map.Entry<Object, Object> entry : new CopyOnWriteArraySet<>(System.getProperties().entrySet())) {
            String configKey = entry.getKey().toString();
            String newConfigValue = entry.getValue().toString();
            String oldConfigValue = properties.getProperty(configKey);
            properties.put(configKey, newConfigValue);
            if (oldConfigValue != null) {
                RecordLog.info("[AthenaConfigLoader] JVM parameter overrides {0}: {1} -> {2}",
                        configKey, oldConfigValue, newConfigValue);
            }
        }
    }


    public static Properties getProperties() {
        return properties;
    }


}
