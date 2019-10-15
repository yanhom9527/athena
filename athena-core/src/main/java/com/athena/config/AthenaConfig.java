package com.athena.config;

import com.athena.log.RecordLog;
import com.athena.util.AppNameUtil;
import com.athena.util.AssertUtil;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mukong
 */
public class AthenaConfig {

    /**
     * The default application type.
     *
     * @since 1.6.0
     */
    public static final int APP_TYPE_COMMON = 0;

    private static final Map<String, String> props = new ConcurrentHashMap<>();
    private static int appType = APP_TYPE_COMMON;

    public static final String APP_TYPE = "athena.app.type";
    public static final String CHARSET = "athena.charset";
    public static final String SINGLE_METRIC_FILE_SIZE = "athena.metric.file.single.size";
    public static final String TOTAL_METRIC_FILE_COUNT = "athena.metric.file.total.count";
    public static final String COLD_FACTOR = "athena.flow.cold.factor";
    public static final String STATISTIC_MAX_RT = "athena.statistic.max.rt";

    static final String DEFAULT_CHARSET = "UTF-8";
    static final long DEFAULT_SINGLE_METRIC_FILE_SIZE = 1024 * 1024 * 50;
    static final int DEFAULT_TOTAL_METRIC_FILE_COUNT = 6;
    static final int DEFAULT_COLD_FACTOR = 3;
    static final int DEFAULT_STATISTIC_MAX_RT = 4900;

    static {
        try {
            initialize();
            loadProps();
            resolveAppType();
            RecordLog.info("[AthenaConfig] Application type resolved: " + appType);
        } catch (Throwable ex) {
            RecordLog.warn("[AthenaConfig] Failed to initialize", ex);
            ex.printStackTrace();
        }
    }

    private static void resolveAppType() {
        try {
            String type = getConfig(APP_TYPE);
            if (type == null) {
                appType = APP_TYPE_COMMON;
                return;
            }
            appType = Integer.parseInt(type);
            if (appType < 0) {
                appType = APP_TYPE_COMMON;
            }
        } catch (Exception ex) {
            appType = APP_TYPE_COMMON;
        }
    }

    private static void initialize() {
        // Init default properties.
        setConfig(CHARSET, DEFAULT_CHARSET);
        setConfig(SINGLE_METRIC_FILE_SIZE, String.valueOf(DEFAULT_SINGLE_METRIC_FILE_SIZE));
        setConfig(TOTAL_METRIC_FILE_COUNT, String.valueOf(DEFAULT_TOTAL_METRIC_FILE_COUNT));
        setConfig(COLD_FACTOR, String.valueOf(DEFAULT_COLD_FACTOR));
        setConfig(STATISTIC_MAX_RT, String.valueOf(DEFAULT_STATISTIC_MAX_RT));
    }

    private static void loadProps() {
        Properties properties = AthenaConfigLoader.getProperties();
        for (Object key : properties.keySet()) {
            setConfig((String) key, (String) properties.get(key));
        }
    }

    /**
     * Get config value of the specific key.
     *
     * @param key config key
     * @return the config value.
     */
    public static String getConfig(String key) {
        AssertUtil.notNull(key, "key cannot be null");
        return props.get(key);
    }

    public static void setConfig(String key, String value) {
        AssertUtil.notNull(key, "key cannot be null");
        AssertUtil.notNull(value, "value cannot be null");
        props.put(key, value);
    }

    public static String removeConfig(String key) {
        AssertUtil.notNull(key, "key cannot be null");
        return props.remove(key);
    }

    public static void setConfigIfAbsent(String key, String value) {
        AssertUtil.notNull(key, "key cannot be null");
        AssertUtil.notNull(value, "value cannot be null");
        String v = props.get(key);
        if (v == null) {
            props.put(key, value);
        }
    }

    public static String getAppName() {
        return AppNameUtil.getAppName();
    }

    /**
     * Get application type.
     *
     * @return application type, common (0) by default
     * @since 1.6.0
     */
    public static int getAppType() {
        return appType;
    }

    public static String charset() {
        return props.get(CHARSET);
    }

    public static long singleMetricFileSize() {
        try {
            return Long.parseLong(props.get(SINGLE_METRIC_FILE_SIZE));
        } catch (Throwable throwable) {
            RecordLog.warn("[AthenaConfig] Parse singleMetricFileSize fail, use default value: "
                    + DEFAULT_SINGLE_METRIC_FILE_SIZE, throwable);
            return DEFAULT_SINGLE_METRIC_FILE_SIZE;
        }
    }

    public static int totalMetricFileCount() {
        try {
            return Integer.parseInt(props.get(TOTAL_METRIC_FILE_COUNT));
        } catch (Throwable throwable) {
            RecordLog.warn("[AthenaConfig] Parse totalMetricFileCount fail, use default value: "
                    + DEFAULT_TOTAL_METRIC_FILE_COUNT, throwable);
            return DEFAULT_TOTAL_METRIC_FILE_COUNT;
        }
    }

    public static int coldFactor() {
        try {
            int coldFactor = Integer.parseInt(props.get(COLD_FACTOR));
            // check the cold factor larger than 1
            if (coldFactor <= 1) {
                coldFactor = DEFAULT_COLD_FACTOR;
                RecordLog.warn("cold factor=" + coldFactor + ", should be larger than 1, use default value: "
                        + DEFAULT_COLD_FACTOR);
            }
            return coldFactor;
        } catch (Throwable throwable) {
            RecordLog.warn("[AthenaConfig] Parse coldFactor fail, use default value: "
                    + DEFAULT_COLD_FACTOR, throwable);
            return DEFAULT_COLD_FACTOR;
        }
    }

    public static int statisticMaxRt() {
        try {
            return Integer.parseInt(props.get(STATISTIC_MAX_RT));
        } catch (Throwable throwable) {
            RecordLog.warn("[AthenaConfig] Parse statisticMaxRt fail, use default value: "
                    + DEFAULT_STATISTIC_MAX_RT, throwable);
            return DEFAULT_STATISTIC_MAX_RT;
        }
    }
}
