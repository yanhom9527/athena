package com.athena.log;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/***
 * The basic logger for vital events.
 *
 * @author muknog
 */
public class RecordLog extends LogBase {
    private static final Logger heliumRecordLog = Logger.getLogger("cspSentinelRecordLog");
    private static final String FILE_NAME = "sentinel-record.log";
    private static Handler logHandler = null;

    static {
        logHandler = makeLogger(FILE_NAME, heliumRecordLog);
    }

    public static void info(String detail, Object... params) {
        log(heliumRecordLog, logHandler, Level.INFO, detail, params);
    }

    public static void info(String detail, Throwable e) {
        log(heliumRecordLog, logHandler, Level.INFO, detail, e);
    }

    public static void warn(String detail, Object... params) {
        log(heliumRecordLog, logHandler, Level.WARNING, detail, params);
    }

    public static void warn(String detail, Throwable e) {
        log(heliumRecordLog, logHandler, Level.WARNING, detail, e);
    }
}
