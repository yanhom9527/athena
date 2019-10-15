package com.athena.netty;

import com.athena.command.CommandHandler;
import com.athena.config.TransportConfig;
import com.athena.log.CommandCenterLog;
import com.athena.log.RecordLog;
import com.athena.util.StringUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author mukong
 */
public final class HttpServer {

    private static final int DEFAULT_PORT = 8719;

    private Channel channel;

    final static Map<String, CommandHandler> handlerMap = new ConcurrentHashMap<String, CommandHandler>();

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new HttpServerInitializer());
            int port;
            try {
                if (StringUtil.isEmpty(TransportConfig.getPort())) {
                    CommandCenterLog.info("Port not configured, using default port: " + DEFAULT_PORT);
                    port = DEFAULT_PORT;
                } else {
                    port = Integer.parseInt(TransportConfig.getPort());
                }
            } catch (Exception e) {
                // Will cause the application exit.
                throw new IllegalArgumentException("Illegal port: " + TransportConfig.getPort());
            }
            
            int retryCount = 0;
            ChannelFuture channelFuture = null;
            // loop for an successful binding
            while (true) {
                int newPort = getNewPort(port, retryCount);
                try {
                    channelFuture = b.bind(newPort).sync();
                    TransportConfig.setRuntimePort(newPort);
                    CommandCenterLog.info("[NettyHttpCommandCenter] Begin listening at port " + newPort);
                    break;
                } catch (Exception e) {
                    TimeUnit.MILLISECONDS.sleep(30);
                    RecordLog
                        .warn("[HttpServer] Netty server bind error, port={0}, retry={1}", newPort, retryCount);
                    retryCount ++;
                }
            }
            channel = channelFuture.channel();
            channel.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    
    /**
     * Increase port number every 3 tries.
     * 
     * @param basePort base port to start
     * @param retryCount retry count
     * @return next calculated port
     */
    private int getNewPort(int basePort, int retryCount) {
        return basePort + retryCount / 3;
    }

    public void close() {
        channel.close();
    }

    public void registerCommand(String commandName, CommandHandler handler) {
        if (StringUtil.isEmpty(commandName) || handler == null) {
            return;
        }

        if (handlerMap.containsKey(commandName)) {
            CommandCenterLog.warn("[NettyHttpCommandCenter] Register failed (duplicate command): " + commandName);
            return;
        }

        handlerMap.put(commandName, handler);
    }

    public void registerCommands(Map<String, CommandHandler> handlerMap) {
        if (handlerMap != null) {
            for (Entry<String, CommandHandler> e : handlerMap.entrySet()) {
                registerCommand(e.getKey(), e.getValue());
            }
        }
    }
}
