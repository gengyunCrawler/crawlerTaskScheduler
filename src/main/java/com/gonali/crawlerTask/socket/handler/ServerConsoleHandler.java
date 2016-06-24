package com.gonali.crawlerTask.socket.handler;

import com.gonali.crawlerTask.message.HandlerMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by TianyuanPan on 6/4/16.
 */
public class ServerConsoleHandler implements SocketHandler {

    private BufferedReader in;
    private PrintWriter out;
    private String ioString = "";
    private Socket mySocket;


    @Override
    public byte[] doHandle(Socket socket) {

        mySocket = socket;
        char[] str = new char[65536];
        int readCharSize;

        try {

            in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            out = new PrintWriter(mySocket.getOutputStream(), true);

            ioString = new String(str);
            while ((readCharSize = in.read(str, 0, 65536)) > 0) {

                ioString += new String(str, 0, readCharSize);

                System.out.println("Server Received:\n=================\n" + ioString + "\n=================\n");
                out.println(HandlerMessage.getMessage("confirm", true));


            }
            out.close();
            out.flush();
            in.close();

        } catch (Exception ex) {
            System.out.println("Exception: at ServerConsoleHandler.java, method doHandle(...).");
            ex.printStackTrace();

        } finally {
            try {
                mySocket.close();
            } catch (Exception ex) {
                System.out.println("Exception: at ServerConsoleHandler.java, method doHandle(...), close socket error.");
                ex.printStackTrace();
            }
        }

        return ioString.getBytes();
    }
}
