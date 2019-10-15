package com.athena.config;

import com.athena.log.RecordLog;
import com.athena.util.HostNameUtil;
import com.athena.util.StringUtil;

/**
 * @author mukong
 */
public class TransportConfig {

    public static final String CONSOLE_SERVER = "athena.dashboard.server";
    public static final String SERVER_PORT = "athena.api.port";
    public static final String HEARTBEAT_INTERVAL_MS = "athena.heartbeat.interval.ms";
    public static final String HEARTBEAT_CLIENT_IP = "athena.heartbeat.client.ip";

    private static int runtimePort = -1;

    /**
     * Get heartbeat interval in milliseconds.
     *
     * @return heartbeat interval in milliseconds if exists, or null if not configured or invalid config
     */
    public static Long getHeartbeatIntervalMs() {
        String interval = AthenaConfig.getConfig(HEARTBEAT_INTERVAL_MS);
        try {
            return interval == null ? null : Long.parseLong(interval);
        } catch (Exception ex) {
            RecordLog.warn("[TransportConfig] Failed to parse heartbeat interval: " + interval);
            return null;
        }
    }

    /**
     * Get ip:port of Dashboard.
     *
     * @return console server ip:port, maybe null if not configured
     */
    public static String getConsoleServer() {
        return AthenaConfig.getConfig(CONSOLE_SERVER);
    }

    public static int getRuntimePort() {
        return runtimePort;
    }

    /**
     * Get Server port of this HTTP server.
     *
     * @return the port, maybe null if not configured.
     */
    public static String getPort() {
        if (runtimePort > 0) {
            return String.valueOf(runtimePort);
        }
        return AthenaConfig.getConfig(SERVER_PORT);
    }

    /**
     * Set real port this HTTP server uses.
     *
     * @param port real port.
     */
    public static void setRuntimePort(int port) {
        runtimePort = port;
    }

    /**
     * Get heartbeat client local ip.
     * If the client ip not configured,it will be the address of local host
     *
     * @return the local ip.
     */
    public static String getHeartbeatClientIp() {
        String ip = AthenaConfig.getConfig(HEARTBEAT_CLIENT_IP);
        if (StringUtil.isBlank(ip)) {
            ip = HostNameUtil.getIp();
        }
        return ip;
    }
}
