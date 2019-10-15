package com.athena.command;

import com.athena.log.RecordLog;
import com.athena.util.SpiLoader;

/**
 * Provider for a universal {@link CommandCenter} instance.
 *
 * @author mukong
 */
public final class CommandCenterProvider {

    private static CommandCenter commandCenter = null;

    static {
        resolveInstance();
    }

    private static void resolveInstance() {
        CommandCenter resolveCommandCenter = SpiLoader.loadHighestPriorityInstance(CommandCenter.class);

        if (resolveCommandCenter == null) {
            RecordLog.warn("[CommandCenterProvider] WARN: No existing CommandCenter found");
        } else {
            commandCenter = resolveCommandCenter;
            RecordLog.info("[CommandCenterProvider] CommandCenter resolved: " + resolveCommandCenter.getClass()
                .getCanonicalName());
        }
    }

    /**
     * Get resolved {@link CommandCenter} instance.
     *
     * @return resolved {@code CommandCenter} instance
     */
    public static CommandCenter getCommandCenter() {
        return commandCenter;
    }

    private CommandCenterProvider() {}
}
