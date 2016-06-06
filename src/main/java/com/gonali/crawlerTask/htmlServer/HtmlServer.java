package com.gonali.crawlerTask.htmlServer;

import com.gonali.crawlerTask.utils.ConfigUtils;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by TianyuanPan on 6/6/16.
 */
public class HtmlServer {

    private static HtmlServer htmlServer;
    private InetSocketAddress address;
    private int port;
    private HttpServer httpServer;
    private HtmlHandler htmlHandler;
    private String context;

    private HtmlServer(int port, String context) {

        this.address = new InetSocketAddress(port);
        this.port = port;
        this.context = context;
        this.htmlHandler = new HtmlHandler();

        try {

            httpServer = HttpServer.create(this.address, this.port);
            httpServer.createContext(this.context, this.htmlHandler);
            httpServer.setExecutor(null);

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public static HtmlServer createHtmlServer(){

        if (htmlServer == null){

            int port = Integer.parseInt(ConfigUtils.getResourceBundle().getString("HTML_SERVER_LISTENING_PORT"));
            String context = ConfigUtils.getResourceBundle().getString("HTML_CONTEXT");

            htmlServer = new HtmlServer(port, context);
        }
        return htmlServer;
    }

    public void serverStart() {

        httpServer.start();
    }
}
