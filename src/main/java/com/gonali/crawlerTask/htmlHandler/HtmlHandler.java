package com.gonali.crawlerTask.htmlHandler;

import com.gonali.crawlerTask.socket.handler.SocketHandler;

import java.net.Socket;

/**
 * Created by TianyuanPan on 6/6/16.
 */
public class HtmlHandler implements SocketHandler{


    @Override
    public byte[] doHandle(Socket socket) {
        return new byte[0];
    }
}
