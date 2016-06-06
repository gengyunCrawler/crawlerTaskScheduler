package com.gonali.crawlerTask.deamon;

import com.gonali.crawlerTask.handler.HeartbeatHandler;
import com.gonali.crawlerTask.socket.SchedulerServerSocket;

import java.net.InetAddress;

/**
 * Created by TianyuanPan on 6/6/16.
 */
public class HtmlDeamon implements Runnable {

    private static volatile HtmlDeamon hearbeatDeamon;
    private HeartbeatHandler handler;
    private SchedulerServerSocket htmlServer;
    private InetAddress bindAddress;
    private int port;
    private int threadPoolSize;


    @Override
    public void run() {

    }
}
