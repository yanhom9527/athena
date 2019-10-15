package com.athena.netty;

import com.athena.command.CommandCenter;
import com.athena.command.CommandHandler;
import com.athena.command.CommandHandlerProvider;
import com.athena.annotation.SpiOrder;
import com.athena.concurrent.NamedThreadFactory;
import com.athena.log.RecordLog;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Implementation of {@link CommandCenter} based on Netty HTTP library.
 * @author mukong
 */
@SpiOrder(SpiOrder.LOWEST_PRECEDENCE - 100)
public class NettyHttpCommandCenter implements CommandCenter {

    private final HttpServer server = new HttpServer();

    @SuppressWarnings("PMD.ThreadPoolCreationRule")
    private final ExecutorService pool = Executors.newSingleThreadExecutor(
        new NamedThreadFactory("sentinel-netty-command-center-executor"));

    @Override
    public void start() throws Exception {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    server.start();
                } catch (Exception ex) {
                    RecordLog.info("Start netty server error", ex);
                    ex.printStackTrace();
                    System.exit(-1);
                }
            }
        });
    }

    @Override
    public void stop() throws Exception {
        server.close();
        pool.shutdownNow();
    }

    @Override
    public void beforeStart() throws Exception {
        // Register handlers
        Map<String, CommandHandler> handlers = CommandHandlerProvider.getInstance().namedHandlers();
        server.registerCommands(handlers);
    }
}
