package com.gonali.crawlerTask.handler;

import com.alibaba.fastjson.JSON;
import com.gonali.crawlerTask.handler.model.HeartbeatMsgModel;
import com.gonali.crawlerTask.redisQueue.HeartbeatMsgQueue;
import com.gonali.crawlerTask.socket.handler.SocketHandler;
import com.gonali.crawlerTask.utils.LoggerUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by TianyuanPan on 6/4/16.
 */
public class HeartbeatHandler extends LoggerUtils implements SocketHandler {

    private final int charBufferSize = 65536;
    private char[] charBuffer;
    private HeartbeatMsgModel heartbeatMsg;
    private HeartbeatMsgQueue heartbeatMsgQueue;
    private BufferedReader in;
    private PrintWriter out;
    private String jsonString = "";
    private Socket mySocket;

    public HeartbeatHandler() {

        charBuffer = new char[charBufferSize];
        heartbeatMsg = new HeartbeatMsgModel();
        heartbeatMsgQueue = new HeartbeatMsgQueue();
    }

    @Override
    public byte[] doHandle(Socket socket) {

        mySocket = socket;
        int readCharSize;
        charBuffer = new char[charBufferSize];
        jsonString = "";
        try {

            in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            //out = new PrintWriter(mySocket.getOutputStream(), true);

            try {

                while ((readCharSize = in.read(charBuffer, 0, charBufferSize)) > 0) {

                    jsonString += new String(charBuffer, 0, readCharSize);
                }


                //System.out.println("Heartbeat Received:\n=================\n" + jsonString + "\n=================\n");

                /*out.println(HandlerMessage.getMessage("confirm", true));
                out.flush();
                out.close();*/

                in.close();

            } catch (Exception e) {

                e.printStackTrace();
            }



            heartbeatMsg = JSON.parseObject(jsonString, HeartbeatMsgModel.class);

            heartbeatMsgQueue.setMessage(heartbeatMsg).pushMessage();

        } catch (Exception ex) {
            System.out.println("Exception: at HeartbeatHandler.java, method doHandle(...). Message: " +
                    ex.getMessage());
            ex.printStackTrace();

        } finally {

            try {

                mySocket.close();

            } catch (IOException ex) {

                System.out.println("Exception: at HeartbeatHandler.java, method doHandle(...), close socket error.");
                ex.printStackTrace();
            }
        }

        return jsonString.getBytes();
    }
}
