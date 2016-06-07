package com.gonali.crawlerTask.scheduler.rulers;

import com.alibaba.fastjson.JSON;
import com.gonali.crawlerTask.model.EntityModel;
import com.gonali.crawlerTask.model.TaskModel;
import com.gonali.crawlerTask.redisQueue.TaskQueue;
import com.gonali.crawlerTask.scheduler.TaskScheduler;

import java.util.List;

/**
 * Created by TianyuanPan on 6/6/16.
 */
public class SimpleLongTimeFirstRuler extends RulerBase {

    private TaskModel currentTask;

    public SimpleLongTimeFirstRuler(){


    }

    @Override
    public void writeBack(TaskScheduler scheduler) {

    }

    @Override
    public TaskModel doSchedule(TaskScheduler scheduler) {

        currentTask = scheduler.getCurrentTask();

        if (currentTask == null)
            return getTask();

        System.out.println("HeartbeatMseage:\n\t" + JSON.toJSONString(scheduler.getHeartbeatUpdater().getHeartbeatMsgList()));

        return null;
    }


    private void updateTaskQueue(){

        List<EntityModel>  modelList = taskModelDao.selectAll(taskTableName);

    }


    private TaskModel getTask(){

        if (currentTaskQueueLenth > 0) {

            currentTask = TaskQueue.popCrawlerTaskQueue();
            if (currentTask == null){
                System.out.println("ERR: Scheduler crashed !!!!");
                return null;
            }
            currentTaskQueueLenth = TaskQueue.queueLenth();

        }

        int size = inQueueTaskIdList.size();
        for (int i = 0; i < size; i++){
            if (inQueueTaskIdList.get(i).equals(currentTask.getTaskId())) {
                inQueueTaskIdList.remove(i);
                break;
            }
        }
        return currentTask;
    }


}
