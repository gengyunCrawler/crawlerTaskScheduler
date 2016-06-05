package com.gonali.crawlerTask.scheduler;

import com.gonali.crawlerTask.handler.model.HeartbeatMsgModel;
import com.gonali.crawlerTask.redisQueue.HeartbeatMsgQueue;

import java.util.List;

/**
 * Created by TianyuanPan on 6/5/16.
 */
public class HeartbeatUpdate implements Runnable{

    private List<HeartbeatMsgModel> heartbeatMsgList;
    private HeartbeatMsgQueue messageQueue;

    @Override
    public void run() {


    }

}
