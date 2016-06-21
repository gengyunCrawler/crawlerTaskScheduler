package com.gonali.crawlerTask.htmlServer.model;

import com.alibaba.fastjson.JSON;
import com.gonali.crawlerTask.handler.model.HeartbeatMsgModel;
import com.gonali.crawlerTask.redisQueue.TaskQueue;
import com.gonali.crawlerTask.scheduler.TaskScheduler;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;

/**
 * Created by TianyuanPan on 6/14/16.
 */
public class SimpleResponse extends ResposeBase {


    @Override
    public String responseContents(HttpExchange exchange, TaskScheduler scheduler) {
     /*   String currentTaskId = scheduler.getCurrentTask().getTaskId();
        long currentTaskQueueSize = TaskQueue.queueLenth();
        List<HeartbeatMsgModel> heartbeatMsgModelList = scheduler.getHeartbeatUpdater().getHeartbeatMsgList();
        String s;
        s = "current crawling task id: " + currentTaskId + "<br/><br/>";
        s += "currentTaskQueueSize: " + currentTaskQueueSize + "<br/><br/>";
        for (HeartbeatMsgModel model : heartbeatMsgModelList)
            s += "Heartbeat: " + JSON.toJSONString(model) + "<br/>";*/

        return "<center><h1>Crawler Status:</h1><br/> " + "Testing" + " </center>";
    }


}
