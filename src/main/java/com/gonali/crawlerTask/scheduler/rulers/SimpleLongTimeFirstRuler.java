package com.gonali.crawlerTask.scheduler.rulers;

import com.alibaba.fastjson.JSON;
import com.gonali.crawlerTask.handler.model.HeartbeatMsgModel;
import com.gonali.crawlerTask.handler.model.HeartbeatStatusCode;
import com.gonali.crawlerTask.model.EntityModel;
import com.gonali.crawlerTask.model.TaskModel;
import com.gonali.crawlerTask.model.TaskStatus;
import com.gonali.crawlerTask.nodes.NodeInfo;
import com.gonali.crawlerTask.redisQueue.TaskQueue;
import com.gonali.crawlerTask.scheduler.HeartbeatUpdater;
import com.gonali.crawlerTask.scheduler.TaskScheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 6/6/16.
 */
public class SimpleLongTimeFirstRuler extends RulerBase {


    public SimpleLongTimeFirstRuler() {


    }

    @Override
    public void writeBack(TaskScheduler scheduler) {

 /*       TaskModel current = scheduler.getCurrentTask();

        if (current != null &&
                !isListContainsEntity(current) &&
                current.getTaskStatus() == TaskStatus.CRAWLED) {

            addToWriteBack(current);
        }*/

        for (EntityModel m : writeBackEntityList) {
            taskModelDao.update(taskTableName, m);
            writeBackEntityList.remove(m);
        }

        if (scheduler.isCurrentFinish())
            cleanWriteBackEntityList();

    }

    @Override
    public TaskModel doSchedule(TaskScheduler scheduler) {

        currentTask = scheduler.getCurrentTask();

        if (currentTaskQueueLength < 3)
            updateTaskQueue();

        if (currentTask == null) {

            currentTask = getTask();
            if (currentTask == null) {
                scheduler.setIsCurrentFinish(true);
                return null;
            }
            scheduler.setIsCurrentFinish(false);
            currentTask.setTaskStartTime(System.currentTimeMillis());
            currentTask.setTaskStatus(TaskStatus.UNCRAWL);
            return currentTask;
        }

        List<NodeInfo> nodeInfoList = scheduler.getNodeInfoList();
        int node = nodeInfoList.size();
        List<HeartbeatMsgModel> heartbeatList = scheduler.getHeartbeatUpdater().getHeartbeatMsgList();


        if (heartbeatList.size() < node)
            return currentTask;

        long currentTime = System.currentTimeMillis();

        // heartbeat timeout check
        for (int i = 0; i < node; i++) {

            for (HeartbeatMsgModel heartbeat : heartbeatList) {

                if (heartbeat.getHostname().equals(nodeInfoList.get(i).getHostname()))
                    if (currentTime - heartbeat.getTime() > 3 * 1000 * HeartbeatUpdater.getCheckInterval())
                        scheduler.getHeartbeatUpdater().setHeartbeatMsg(heartbeat.getHostname(), heartbeat.getPid(), HeartbeatStatusCode.TIMEOUT);
            }
        }

        // current task finished check
        int finished = HeartbeatStatusCode.CRAWLING;

        for (HeartbeatMsgModel heartbeat : heartbeatList) {
            int code = heartbeat.getStatusCode();
            finished = finished & code;
        }

        if (finished == HeartbeatStatusCode.FINISHED) {
            int size = inQueueTaskIdList.size();
            for (int i = 0; i < size; i++) {
                if (inQueueTaskIdList.get(i).equals(currentTask.getTaskId())) {
                    inQueueTaskIdList.remove(i);
                    break;
                }
            }
            scheduler.setIsCurrentFinish(true);
            scheduler.getCurrentTask().setTaskStopTime(System.currentTimeMillis());
            scheduler.getCurrentTask().setTaskStatus(TaskStatus.CRAWLED);
            addToWriteBack(scheduler.getCurrentTask());
            scheduler.getHeartbeatUpdater().resetHeartbeatMsgList();

            return null;
        }

       // System.out.println("HeartbeatMseage:\n\t" + JSON.toJSONString(scheduler.getHeartbeatUpdater().getHeartbeatMsgList()));
       // currentTask.setTaskStatus(TaskStatus.CRAWLING);
        return currentTask;
    }


    private void updateTaskQueue() {

        List<EntityModel> modelList = taskModelDao.selectAll(taskTableName);
        int size = modelList.size();
        for (int i = 0; i < size; ) {

            if (inQueueTaskIdList.contains(((TaskModel) modelList.get(i)).getTaskId())) {

                modelList.remove(i);
                size = modelList.size();
                i = 0;
            } else {

                i++;
            }
        }

        TaskModel[] sortedModels = sortByTimeAesc(modelList);

        int length = sortedModels.length;
        for (int i = 0; i < length; i++) {

            if (currentTaskQueueLength < maxTaskQueueLength) {

                sortedModels[i].setTaskStatus(TaskStatus.INQUEUE);
                inQueueTaskIdList.add(sortedModels[i].getTaskId());
                TaskQueue.pushCrawlerTaskQueue(sortedModels[i]);
                addToWriteBack(sortedModels[i]);
                currentTaskQueueLength = TaskQueue.queueLenth();
            } else break;
        }

        currentTaskQueueLength = TaskQueue.queueLenth();
    }


    private TaskModel[] sortByTimeAesc(List<EntityModel> modelList) {

        int size = modelList.size();
        TaskModel[] models = new TaskModel[size];
        TaskModel temp;

        for (int i = 0; i < size; i++)
            models[i] = (TaskModel) modelList.get(i);

        int k = 0, l = size - 1;

        while (k < size) {
            while (k != l) {
                if (models[k].getTaskStopTime() > models[l].getTaskStopTime()) {

                    temp = models[k];
                    models[k] = models[l];
                    models[l] = temp;
                }
                l--;
            }
            l = size - 1;
            k++;
        }
        return models;
    }


    private TaskModel getTask() {

        currentTaskQueueLength = TaskQueue.queueLenth();

        if (currentTaskQueueLength > 0) {

            currentTask = TaskQueue.popCrawlerTaskQueue();
            if (currentTask == null) {
                System.out.println("ERR: Scheduler crashed !!!!");
                return null;
            }
            currentTaskQueueLength = TaskQueue.queueLenth();

        } else {

            return null;
        }

        return currentTask;
    }


}
