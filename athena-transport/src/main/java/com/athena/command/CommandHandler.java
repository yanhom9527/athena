package com.athena.command;

/**
 * Represent a handler that handles a {@link CommandRequest}.
 *
 * @author mukong
 */
public interface CommandHandler<R> {

    /**
     * Handle the given Courier command request.
     *
     * @param request the request to handle
     * @return the response
     */
    CommandResponse<R> handle(CommandRequest request);
}
