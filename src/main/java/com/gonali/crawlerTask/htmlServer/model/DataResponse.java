package com.gonali.crawlerTask.htmlServer.model;

import com.gonali.crawlerTask.scheduler.TaskScheduler;
import com.sun.net.httpserver.HttpExchange;

/**
 * Created by TianyuanPan on 6/6/16.
 */
public interface DataResponse {

    public String responseContents(HttpExchange exchange, TaskScheduler scheduler);

}
