package com.gonali.crawlerTask.scheduler;

import com.gonali.crawlerTask.handler.model.HeartbeatMsgModel;
import com.gonali.crawlerTask.message.Message;
import com.gonali.crawlerTask.redisQueue.HeartbeatMsgQueue;
import com.gonali.crawlerTask.utils.ConfigUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by TianyuanPan on 6/5/16.
 */
public class HeartbeatUpdater implements Runnable {

    private volatile List<HeartbeatMsgModel> heartbeatMsgList;
    private HeartbeatMsgQueue messageQueue;
    private Message message;
    private int checkInterval;
    private Lock myLock;


    public HeartbeatUpdater() {

        heartbeatMsgList = new ArrayList<>();
        messageQueue = new HeartbeatMsgQueue();
        myLock = new ReentrantLock();

        try {

            this.checkInterval = Integer.parseInt(ConfigUtils.getResourceBundle("nodes").getString("NODES_CHECK_INTERVAL"));

        } catch (NumberFormatException e) {

            e.printStackTrace();
            this.checkInterval = 10;
        }
    }

    @Override
    public void run() {

        while (true) {

            try {

                myLock.lock();
                updateMsg();

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

                myLock.unlock();
            }

            try {
                Thread.sleep(checkInterval * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private void updateMsg() {

        int msgSize = heartbeatMsgList.size();

        while ((message = messageQueue.popMessage()) != null) {
            if (msgSize == 0) {
                heartbeatMsgList.add((HeartbeatMsgModel) message);
                continue;
            }
            for (int i = 0; i < msgSize; ++i)
                if (heartbeatMsgList.get(i).getHostname().equals(
                        ((HeartbeatMsgModel) message).getHostname()) &&
                        heartbeatMsgList.get(i).getPid() == ((HeartbeatMsgModel) message).getPid()) {

                    heartbeatMsgList.set(i, (HeartbeatMsgModel) message);

                } else {
                    heartbeatMsgList.add((HeartbeatMsgModel) message);
                }
        }
    }


    public  List<HeartbeatMsgModel> getHeartbeatMsgList() {


        try {
            myLock.lock();
            List<HeartbeatMsgModel> newList = new ArrayList<>();

            for (HeartbeatMsgModel msg : this.heartbeatMsgList)
                newList.add(msg);

            return newList;

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            myLock.unlock();
        }
        return null;
    }

    public HeartbeatMsgModel getHeartbeatMsg(String hostname, int pid) {

        try {
            myLock.lock();

            for (HeartbeatMsgModel heartbeat : heartbeatMsgList)
                if (heartbeat.getHostname().equals(hostname) && heartbeat.getPid() == pid)
                    return heartbeat;

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            myLock.unlock();
        }

        return null;
    }

}
