package com.gonali.crawlerTask.handler;

import com.alibaba.fastjson.JSON;
import com.gonali.crawlerTask.handler.model.HeartbeatMsgModel;
import com.gonali.crawlerTask.message.HandlerMessage;
import com.gonali.crawlerTask.redisQueue.HeartbeatMsgQueue;
import com.gonali.crawlerTask.socket.handler.SocketHandler;
import com.gonali.crawlerTask.utils.LoggerUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by TianyuanPan on 6/4/16.
 */
public class HeartbeatHandler extends LoggerUtil implements SocketHandler {

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
            out = new PrintWriter(mySocket.getOutputStream(), true);

            while ((readCharSize = in.read(charBuffer, 0, charBufferSize)) > 0) {

                jsonString += new String(charBuffer, 0, readCharSize);

                System.out.println("Server Received:\n=================\n" + jsonString + "\n=================\n");
                out.println(HandlerMessage.getMessage("confirm", true));


            }
            out.flush();
            out.close();
            in.close();

            heartbeatMsg = JSON.parseObject(jsonString, HeartbeatMsgModel.class);

            heartbeatMsgQueue.setMessage(heartbeatMsg).pushMessage();

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {

            try {

                mySocket.close();

            } catch (IOException ex) {

                System.out.println("close socket error.");
                ex.printStackTrace();
            }
        }

//        String s = JSON.toJSONString(heartbeatMsgQueue.popMessage());
//
//        System.out.printf("POP: " + s);


        return jsonString.getBytes();
    }
}
