package com.athena.command;

import com.athena.annotation.CommandMapping;
import com.athena.util.StringUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Provides and filters command handlers registered via SPI.
 *
 * @author mukong
 */
public class CommandHandlerProvider implements Iterable<CommandHandler> {

    private final ServiceLoader<CommandHandler> serviceLoader = ServiceLoader.load(CommandHandler.class);

    /**
     * Get all command handlers annotated with {@link CommandMapping} with command name.
     *
     * @return list of all named command handlers
     */
    public Map<String, CommandHandler> namedHandlers() {
        Map<String, CommandHandler> map = new HashMap<String, CommandHandler>();
        for (CommandHandler handler : serviceLoader) {
            String name = parseCommandName(handler);
            if (!StringUtil.isEmpty(name)) {
                map.put(name, handler);
            }
        }
        return map;
    }

    private String parseCommandName(CommandHandler handler) {
        CommandMapping commandMapping = handler.getClass().getAnnotation(CommandMapping.class);
        if (commandMapping != null) {
            return commandMapping.name();
        } else {
            return null;
        }
    }

    @Override
    public Iterator<CommandHandler> iterator() {
        return serviceLoader.iterator();
    }

    private static final CommandHandlerProvider INSTANCE = new CommandHandlerProvider();

    public static CommandHandlerProvider getInstance() {
        return INSTANCE;
    }
}
