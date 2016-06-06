package com.gonali.crawlerTask.deamon;

import com.gonali.crawlerTask.handler.HeartbeatHandler;
import com.gonali.crawlerTask.socket.SchedulerServerSocket;
import com.gonali.crawlerTask.utils.ConfigUtils;

import java.net.InetAddress;

/**
 * Created by TianyuanPan on 6/5/16.
 */
public class HearbeatDeamon implements Runnable{

    private static volatile HearbeatDeamon hearbeatDeamon;
    private HeartbeatHandler handler;
    private SchedulerServerSocket heartbeatServer;
    private InetAddress bindAddress;
    private int port;
    private int threadPoolSize;

    private HearbeatDeamon() {

        try {

            bindAddress = InetAddress.getByName(ConfigUtils.getResourceBundle().getString("HEARTBEAT_SERVER_BINDING_ADDRESS"));
            port = Integer.parseInt(ConfigUtils.getResourceBundle().getString("HEARTBEAT_SERVER_LISTENING_PORT"));
            threadPoolSize = Integer.parseInt(ConfigUtils.getResourceBundle().getString("HEARTBEAT_SERVER_THREAD_POOL_SIZE"));

            handler = new HeartbeatHandler();
            heartbeatServer = SchedulerServerSocket.getSchedulerServerSocket(bindAddress, port, threadPoolSize);
            heartbeatServer.registerHandler(handler);

        } catch (Exception ex) {

            ex.printStackTrace();
        }


    }


    public static HearbeatDeamon createDeamon() {

        if (hearbeatDeamon == null)
            hearbeatDeamon = new HearbeatDeamon();
        return hearbeatDeamon;
    }


    @Override
    public void run() {

        this.heartbeatServer.serverStart();
        System.out.println("Heartbeat Deamon OK <<====");
    }


}
