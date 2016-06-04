package com.gonali.crawlerTask.socketTest;

import com.gonali.crawlerTask.handler.HeartbeatHandler;
import com.gonali.crawlerTask.socket.SchedulerServerSocket;
import com.gonali.crawlerTask.socket.handler.ServerConsoleHander;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;

/**
 * Created by TianyuanPan on 6/4/16.
 */
public class TestSchedulerServerSocket {


    public static void main(String[] args) {
        try {
            // get name representing the running Java virtual machine.
            String name = ManagementFactory.getRuntimeMXBean().getName();
            System.out.println(name);
            // get pid
            String pid = name.split("@")[0];
            System.out.println("Pid is:" + pid);

            SchedulerServerSocket.getSchedulerServerSocket(InetAddress.getByName("0.0.0.0"), 10080, 10)
                    .registerHandler(new ServerConsoleHander()).serverStart();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
