package com.gonali.crawlerTask.scheduler;

import com.gonali.crawlerTask.handler.model.HeartbeatMsgModel;
import com.gonali.crawlerTask.handler.model.HeartbeatStatusCode;
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
    private static int checkInterval;
    private static int maxTimeoutCount;
    private Lock myLock;


    static {
        try {

            checkInterval = Integer.parseInt(ConfigUtils.getResourceBundle("nodes").getString("NODES_CHECK_INTERVAL"));

        } catch (NumberFormatException e) {

            e.printStackTrace();
            checkInterval = 10;
        }

        try {

            maxTimeoutCount = Integer.parseInt(ConfigUtils.getResourceBundle("nodes").getString("NODES_MAX_HEARTBEAT_TIMEOUT_COUNT"));

        } catch (NumberFormatException e) {

            e.printStackTrace();
            maxTimeoutCount = 10;
        }
    }

    public HeartbeatUpdater() {

        heartbeatMsgList = new ArrayList<>();
        messageQueue = new HeartbeatMsgQueue();
        myLock = new ReentrantLock();

    }

    @Override
    public void run() {

        while (true) {

            updateHeartbeatMsg();
            heartbeatTimeoutCheck();

            try {
                Thread.sleep(checkInterval * 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private void updateHeartbeatMsg() {

        try {
            myLock.lock();
            int msgSize = heartbeatMsgList.size();
            boolean isHave;

            while ((message = messageQueue.popMessage()) != null) {
                if (msgSize == 0) {
                    heartbeatMsgList.add((HeartbeatMsgModel) message);
                    msgSize = heartbeatMsgList.size();
                    continue;
                }
                isHave = false;
                for (int i = 0; i < msgSize; ++i) {
                    if (heartbeatMsgList.get(i).getTaskId().equals(((HeartbeatMsgModel) message).getTaskId())
                            && heartbeatMsgList.get(i).getHostname().equals(
                            ((HeartbeatMsgModel) message).getHostname()) &&
                            heartbeatMsgList.get(i).getPid() == ((HeartbeatMsgModel) message).getPid()) {

                        heartbeatMsgList.set(i, (HeartbeatMsgModel) message);

                        isHave = true;

                    }
                }

                if (!isHave) {
                    heartbeatMsgList.add((HeartbeatMsgModel) message);
                    msgSize = heartbeatMsgList.size();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            myLock.unlock();
        }
    }

    public void heartbeatTimeoutCheck() {

        try {
            myLock.lock();

            long currentTime = System.currentTimeMillis();
            int size = heartbeatMsgList.size();
            HeartbeatMsgModel h;
            for (int i = 0; i < size; i++) {
                h = heartbeatMsgList.get(i);
                if (currentTime - h.getTime() > 3 * 1000 * HeartbeatUpdater.getCheckInterval() &&
                        h.getStatusCode() != HeartbeatStatusCode.FINISHED) {
                    h.setStatusCode(HeartbeatStatusCode.TIMEOUT);
                    h.setTimeoutCount(h.getTimeoutCount() + 1);
                    heartbeatMsgList.set(i, h);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            myLock.unlock();
        }
    }

    public void resetHeartbeatMsgList() {

        try {
            myLock.lock();

            this.heartbeatMsgList = new ArrayList<>();

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            myLock.unlock();
        }
    }

    public void cleanFinishedHeartbeat(String taskId, String hostname, int pid) {

        try {
            myLock.lock();

            for (HeartbeatMsgModel msg : this.heartbeatMsgList) {

                if (msg.getTaskId().equals(taskId) &&
                        msg.getHostname().equals(hostname) &&
                        msg.getPid() == pid)
                    this.heartbeatMsgList.remove(msg);
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            myLock.unlock();
        }

    }

    public List<HeartbeatMsgModel> getHeartbeatMsgList() {


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

    public HeartbeatMsgModel getHeartbeatMsg(String taskId, String hostname, int pid) {

        try {
            myLock.lock();

            for (HeartbeatMsgModel heartbeat : heartbeatMsgList)
                if (heartbeat.getTaskId().equals(taskId) &&
                        heartbeat.getHostname().equals(hostname) &&
                        heartbeat.getPid() == pid)

                    return heartbeat;

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            myLock.unlock();
        }

        return null;
    }


    public HeartbeatMsgModel setHeartbeatMsg(String taskId, String hostname, int pid, int statusCode) {

        try {
            myLock.lock();

            int size = heartbeatMsgList.size();
            for (int i = 0; i < size; i++)
                if (heartbeatMsgList.get(i).getTaskId().equals(taskId) &&
                        heartbeatMsgList.get(i).getHostname().equals(hostname) &&
                        heartbeatMsgList.get(i).getPid() == pid) {
                    HeartbeatMsgModel msg = heartbeatMsgList.get(i).setStatusCode(statusCode);
                    return heartbeatMsgList.set(i, msg);
                }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            myLock.unlock();
        }

        return null;
    }

    public static int getMaxTimeoutCount(){
        return maxTimeoutCount;
    }

    public static int getCheckInterval() {
        return checkInterval;
    }
}
