package com.gonali.crawlerTask.socket.handler;

import java.net.Socket;

/**
 * Created by TianyuanPan on 6/4/16.
 */
public interface SocketHandler {

    public byte[] doHandle(Socket socket);

}
