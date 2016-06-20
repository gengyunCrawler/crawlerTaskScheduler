package com.gonali.crawlerTask.scheduler.rulers;

import com.gonali.crawlerTask.handler.model.HeartbeatMsgModel;
import com.gonali.crawlerTask.handler.model.HeartbeatStatusCode;
import com.gonali.crawlerTask.model.EntityModel;
import com.gonali.crawlerTask.model.TaskModel;
import com.gonali.crawlerTask.model.TaskStatus;
import com.gonali.crawlerTask.nodes.NodeInfo;
import com.gonali.crawlerTask.redisQueue.TaskQueue;
import com.gonali.crawlerTask.scheduler.CurrentTask;
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

//        if (scheduler.isCurrentFinish())
//            cleanWriteBackEntityList();

    }

    @Override
    public CurrentTask doSchedule(TaskScheduler scheduler) {

        currentTasks = scheduler.getCurrentTasks();
        TaskModel task;
        if (currentTaskQueueLength < 2 * scheduler.getCurrentTasks().getTaskNumber())
            updateTaskQueue();

        while (currentTasks.isHaveFinishedTask()) {

            task = getTask();

            if (task == null)
                break;

            task.setTaskStartTime(System.currentTimeMillis());
            task.setTaskStatus(TaskStatus.UNCRAWL);
            currentTasks.addTask(task);
        }


        List<HeartbeatMsgModel> heartbeatList = scheduler.getHeartbeatUpdater().getHeartbeatMsgList();
        long currentTime = System.currentTimeMillis();

        // heartbeat timeout check
        for (HeartbeatMsgModel h : heartbeatList) {

            if (currentTime - h.getTime() > 3 * 1000 * HeartbeatUpdater.getCheckInterval())
                h.setStatusCode(HeartbeatStatusCode.TIMEOUT);

        }

        currentTasks.setHeartbeatList(heartbeatList);
        return currentTasks;
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
        TaskModel task;
        if (currentTaskQueueLength > 0) {

            task = TaskQueue.popCrawlerTaskQueue();
            if (task == null) {
                System.out.println("ERR: Scheduler crashed !!!!");
                return null;
            }
            currentTaskQueueLength = TaskQueue.queueLenth();

        } else {

            return null;
        }

        return task;
    }


}
