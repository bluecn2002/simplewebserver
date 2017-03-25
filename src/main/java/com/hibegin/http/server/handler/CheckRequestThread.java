package com.hibegin.http.server.handler;

import com.hibegin.common.util.LoggerUtil;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckRequestThread extends Thread {

    private static final Logger LOGGER = LoggerUtil.getLogger(CheckRequestThread.class);

    private Map<SocketChannel, HttpRequestHandlerThread> channelHttpRequestHandlerThreadMap = new ConcurrentHashMap<>();

    private int requestTimeout = 0;

    public CheckRequestThread(String name, int requestTimeout) {
        super(name);
        this.requestTimeout = requestTimeout;
    }

    @Override
    public void run() {
        try {
            while (true) {
                clearRequestListener();
                //LOGGER.log(Level.INFO, "Running... " + channelHttpRequestHandlerThreadMap.size());
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
    }

    private void clearRequestListener() {
        Map<SocketChannel, HttpRequestHandlerThread> removeHttpRequestList = new HashMap<>();
        for (Map.Entry<SocketChannel, HttpRequestHandlerThread> entry : channelHttpRequestHandlerThreadMap.entrySet()) {
            if (entry.getKey().socket().isClosed() || !entry.getKey().isConnected()) {
                removeHttpRequestList.put(entry.getKey(), entry.getValue());
            }
            if (requestTimeout > 0) {
                if (System.currentTimeMillis() - entry.getValue().getRequest().getCreateTime() > requestTimeout * 1000) {
                    entry.getValue().getResponse().renderCode(504);
                    removeHttpRequestList.put(entry.getKey(), entry.getValue());
                }
            }
        }
        for (Map.Entry<SocketChannel, HttpRequestHandlerThread> entry : removeHttpRequestList.entrySet()) {
            entry.getValue().interrupt();
            channelHttpRequestHandlerThreadMap.remove(entry.getKey());
        }
    }

    public Map<SocketChannel, HttpRequestHandlerThread> getChannelHttpRequestHandlerThreadMap() {
        return channelHttpRequestHandlerThreadMap;
    }
}