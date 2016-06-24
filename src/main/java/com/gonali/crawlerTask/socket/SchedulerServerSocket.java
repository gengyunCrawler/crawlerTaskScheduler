package com.gonali.crawlerTask.socket;

import com.gonali.crawlerTask.socket.handler.ServerConsoleHandler;
import com.gonali.crawlerTask.socket.handler.SocketHandler;
import com.gonali.crawlerTask.utils.LoggerUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by TianyuanPan on 6/4/16.
 */
public class SchedulerServerSocket extends LoggerUtils {


    private ServerSocket serverSocket;
    private Socket socket;
    private InetAddress address;
    private int port;
    private SocketHandler handler;
    private ExecutorService executeService;

    private SchedulerServerSocket(InetAddress address, int port, int poolSize) {

        this.address = address;
        this.port = port;
        executeService = Executors.newFixedThreadPool(poolSize);
        handler = new ServerConsoleHandler();
    }


    public static SchedulerServerSocket getSchedulerServerSocket(InetAddress address, int port, int poolSize) {

        return new SchedulerServerSocket(address, port, poolSize);
    }

    public SchedulerServerSocket registerHandler(SocketHandler handler) {

        this.handler = handler;
        return this;
    }

    public void serverStart() {

        try {

            serverSocket = new ServerSocket(port, 10, address);
            System.out.println("SocketServer have been started." + serverSocket);


            while (true) {

                try {
                    socket = serverSocket.accept(); // this accept is blocking.

                    executeService.submit(new Runnable() {
                        public void run() {

                            handler.doHandle(socket);
                        }
                    });

                } catch (IOException e) {
                    System.out.println("Scheduler Exception !!!!");
                    e.printStackTrace();
                }
            }


        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {

            try {
                if (null != serverSocket) {
                    serverSocket.close();
                    System.out.println("serverSocket close");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

}
