package com.athena.util;

import com.athena.command.CommandRequest;

/**
 * Util class for HTTP command center.
 *
 * @author mukong
 */
public final class HttpCommandUtils {

    public static final String REQUEST_TARGET = "command-target";

    public static String getTarget(CommandRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        return request.getMetadata().get(REQUEST_TARGET);
    }

    private HttpCommandUtils() {}
}
