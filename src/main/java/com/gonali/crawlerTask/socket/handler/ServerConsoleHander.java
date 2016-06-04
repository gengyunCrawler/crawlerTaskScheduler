package com.gonali.crawlerTask.socket.handler;

import java.io.*;
import java.net.Socket;

/**
 * Created by TianyuanPan on 6/4/16.
 */
public class ServerConsoleHander implements SocketHandler {

    private BufferedReader in;
    private PrintWriter out;
    private String ioString = "";
    private Socket mySocket;


    @Override
    public byte[] doHandle(Socket socket) {

        mySocket = socket;
        char[] str = new char[65536];
        int size;

        try {

            in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            out = new PrintWriter(mySocket.getOutputStream(), true);
            size = in.read(str, 0, 65536);
            ioString = new String(str);
            if (size > 0) {
                ioString = ioString.substring(0, size);
                System.out.println("========================================");
                System.out.println("Server Received:\n\t" + ioString);
                out.println("Server Received your Message!");
            }else {
                out.println("Server Can not Received your Message!");
            }
            out.close();
            out.flush();
            in.close();

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {
            try {
                mySocket.close();
            } catch (Exception ex) {
                System.out.println("close socket error.");
                ex.printStackTrace();
            }
        }

        return ioString.getBytes();
    }
}
