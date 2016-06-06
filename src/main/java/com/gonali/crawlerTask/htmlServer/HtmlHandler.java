package com.gonali.crawlerTask.htmlServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by TianyuanPan on 6/6/16.
 */
public class HtmlHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream instream = exchange.getRequestBody();
        String con = exchange.getHttpContext().getPath();
        String response = "<font color='#ff0000'>come on baby" + con +"</font>";
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
