package com.gonali.crawlerTask.socketTest;

import com.gonali.crawlerTask.socket.SchedulerClientSocket;
import com.gonali.crawlerTask.socket.handler.ClientConsoleHandler;

/**
 * Created by TianyuanPan on 6/4/16.
 */
public class TeskSchedulerClientSocket {

    static SchedulerClientSocket client;


    public static void main(String[] args) {

        SchedulerClientSocket.getClientSocket("127.0.0.1", 10090)
                .registerHandler(new ClientConsoleHandler())
                .doAction();

    }

}
