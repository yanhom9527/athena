package com.athena.log;

import java.io.UnsupportedEncodingException;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * This Handler publishes log records to console by using {@link StreamHandler}.
 *
 * Print log of WARNING level or above to System.err,
 * and print log of INFO level or below to System.out.
 *
 * To use this handler, add the following VM argument:
 * <pre>
 * -Dcsp.sentinel.log.output.type=console
 * </pre>
 *
 */
class ConsoleHandler extends Handler {

    /**
     * A Handler which publishes log records to System.out.
     */
    private StreamHandler stdoutHandler;

    /**
     * A Handler which publishes log records to System.err.
     */
    private StreamHandler stderrHandler;

    public ConsoleHandler() {
        this.stdoutHandler = new StreamHandler(System.out, new CspFormatter());
        this.stderrHandler = new StreamHandler(System.err, new CspFormatter());
    }

    @Override
    public synchronized void setFormatter(Formatter newFormatter) throws SecurityException {
        this.stdoutHandler.setFormatter(newFormatter);
        this.stderrHandler.setFormatter(newFormatter);
    }

    @Override
    public synchronized void setEncoding(String encoding) throws SecurityException, UnsupportedEncodingException {
        this.stdoutHandler.setEncoding(encoding);
        this.stderrHandler.setEncoding(encoding);
    }

    @Override
    public void publish(LogRecord record) {
        if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
            stderrHandler.publish(record);
            stderrHandler.flush();
        } else {
            stdoutHandler.publish(record);
            stdoutHandler.flush();
        }
    }

    @Override
    public void flush() {
        stdoutHandler.flush();
        stderrHandler.flush();
    }

    @Override
    public void close() throws SecurityException {
        stdoutHandler.close();
        stderrHandler.close();
    }
}
