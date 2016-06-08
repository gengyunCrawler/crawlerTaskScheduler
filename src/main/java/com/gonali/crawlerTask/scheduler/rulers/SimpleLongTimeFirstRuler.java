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

import java.util.List;

/**
 * Created by TianyuanPan on 6/6/16.
 */
public class SimpleLongTimeFirstRuler extends RulerBase {


    public SimpleLongTimeFirstRuler() {


    }

    @Override
    public void writeBack(TaskScheduler scheduler) {

        TaskModel current = scheduler.getCurrentTask();

        if (current != null)
            addToWriteBack(current);

        for (EntityModel m : writeBackEntityList)
            taskModelDao.update(taskTableName, m);

    }

    @Override
    public TaskModel doSchedule(TaskScheduler scheduler) {

        currentTask = scheduler.getCurrentTask();

        updateTaskQueue();

        if (currentTask == null) {

            currentTask = getTask();
            if (currentTask == null) {
                scheduler.setIsFinish(true);
                return null;
            }
            scheduler.setIsFinish(false);
            currentTask.setTaskStartTime(System.currentTimeMillis());
            currentTask.setTaskStatus(TaskStatus.UNCRAWL);
            return currentTask;
        }

        List<NodeInfo> nodeInfoList = scheduler.getNodeInfoList();
        int node = nodeInfoList.size();
        List<HeartbeatMsgModel> heartbeatList = scheduler.getHeartbeatUpdater().getHeartbeatMsgList();

        long currentTime = System.currentTimeMillis();

        // heartbeat timeout check
        for (int i = 0; i < node; i++) {

            for (HeartbeatMsgModel heartbeat : heartbeatList) {

                if (heartbeat.getHostname().equals(nodeInfoList.get(i).getHostname()))
                    if (currentTime - heartbeat.getTime() > 3 * HeartbeatUpdater.getCheckInterval())
                        scheduler.getHeartbeatUpdater().setHeartbeatMsg(heartbeat.getHostname(), heartbeat.getPid(), HeartbeatStatusCode.TIMEOUT);
            }
        }

        // current task finished check
        int finished = 4;
        for (HeartbeatMsgModel heartbeat : heartbeatList) {
            finished &= heartbeat.getStatusCode();
        }
        if (finished == HeartbeatStatusCode.FINISHED) {
            scheduler.setIsFinish(true);
            addToWriteBack(scheduler.getCurrentTask());
            return null;
        }
        System.out.println("HeartbeatMseage:\n\t" + JSON.toJSONString(null));
        currentTask.setTaskStatus(TaskStatus.CRAWLING);
        return currentTask;
    }


    private void updateTaskQueue() {

        List<EntityModel> modelList = taskModelDao.selectAll(taskTableName);
        int size = modelList.size();
        for (int i = 0; i < size; ) {

            if (inQueueTaskIdList.contains(((TaskModel) modelList.get(i)).getTaskId()) ||
                    ((TaskModel) modelList.get(i)).getTaskStatus() == TaskStatus.INQUEUE) {

                modelList.remove(i);
                size = modelList.size();
                i = 0;
            } else {

                i++;
            }
        }

        TaskModel[] sortedModels = sortByTimeAesc(modelList);

        int lenth = sortedModels.length;
        for (int i = 0; i < lenth; i++) {

            if (currentTaskQueueLenth < maxTaskQueueLenth) {

                sortedModels[i].setTaskStatus(TaskStatus.INQUEUE);
                inQueueTaskIdList.add(sortedModels[i].getTaskId());
                TaskQueue.pushCrawlerTaskQueue(sortedModels[i]);
                addToWriteBack(sortedModels[i]);
                currentTaskQueueLenth = TaskQueue.queueLenth();
            } else break;
        }

        currentTaskQueueLenth = TaskQueue.queueLenth();
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

        if (currentTaskQueueLenth > 0) {

            currentTask = TaskQueue.popCrawlerTaskQueue();
            if (currentTask == null) {
                System.out.println("ERR: Scheduler crashed !!!!");
                return null;
            }
            currentTaskQueueLenth = TaskQueue.queueLenth();

        } else {

            return null;
        }

        int size = inQueueTaskIdList.size();
        for (int i = 0; i < size; i++) {
            if (inQueueTaskIdList.get(i).equals(currentTask.getTaskId())) {
                inQueueTaskIdList.remove(i);
                break;
            }
        }
        currentTask.setTaskStatus(TaskStatus.CRAWLING);
        return currentTask;
    }


}
