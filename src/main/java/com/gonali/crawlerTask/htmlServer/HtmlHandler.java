package com.gonali.crawlerTask.htmlServer;

import com.alibaba.fastjson.JSON;
import com.gonali.crawlerTask.htmlServer.model.DataResponse;
import com.gonali.crawlerTask.htmlServer.model.SimpleResponse;
import com.gonali.crawlerTask.scheduler.TaskScheduler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by TianyuanPan on 6/6/16.
 */
public class HtmlHandler implements HttpHandler {

    private TaskScheduler scheduler;
    private DataResponse dataResponse;

    public HtmlHandler(){

        scheduler = TaskScheduler.createTaskScheduler();
        dataResponse = new SimpleResponse();
    }

    public void setDataResponse(DataResponse response){

        if (response != null)
            this.dataResponse = response;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String response = this.dataResponse.responseContents(exchange, this.scheduler);
        exchange.sendResponseHeaders(200, response.length());
        exchange.setAttribute("Content-Length", response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
