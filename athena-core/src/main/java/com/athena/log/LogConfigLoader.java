package com.athena.log;

import com.athena.util.ConfigUtil;
import com.athena.util.StringUtil;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * <p>The loader that responsible for loading Sentinel log configurations.</p>
 *
 * @author mukong
 * @since 1.7.0
 */
public class LogConfigLoader {

    public static final String LOG_CONFIG = "csp.sentinel.config.file";

    private static final String DEFAULT_LOG_CONFIG_FILE = "classpath:sentinel.properties";

    private static final Properties properties = new Properties();

    static {
        load();
    }

    private static void load() {
        String file = System.getProperty(LOG_CONFIG);
        if (StringUtil.isBlank(file)) {
            file = DEFAULT_LOG_CONFIG_FILE;
        }

        Properties p = ConfigUtil.loadProperties(file);
        if (p != null && !p.isEmpty()) {
            properties.putAll(p);
        }

        CopyOnWriteArraySet<Map.Entry<Object, Object>> copy = new CopyOnWriteArraySet<>(System.getProperties().entrySet());
        for (Map.Entry<Object, Object> entry : copy) {
            String configKey = entry.getKey().toString();
            String newConfigValue = entry.getValue().toString();
            properties.put(configKey, newConfigValue);
        }
    }

    public static Properties getProperties() {
        return properties;
    }
}
