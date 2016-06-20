package com.gonali.crawlerTask.scheduler.rulers;

import com.gonali.crawlerTask.dao.TaskModelDao;
import com.gonali.crawlerTask.model.EntityModel;
import com.gonali.crawlerTask.model.TaskModel;
import com.gonali.crawlerTask.redisQueue.TaskQueue;
import com.gonali.crawlerTask.scheduler.CurrentTask;
import com.gonali.crawlerTask.utils.ConfigUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 6/7/16.
 */
public abstract class RulerBase implements Ruler {

    protected CurrentTask currentTasks;

    protected TaskModelDao taskModelDao;
    protected long maxTaskQueueLength;
    protected long currentTaskQueueLength;
    protected List<String> inQueueTaskIdList;
    protected String taskTableName = "crawlerTaskTable";
    protected List<EntityModel> writeBackEntityList;

    protected RulerBase(){

        try {
            maxTaskQueueLength = Integer.parseInt(ConfigUtils.getResourceBundle().getString("MAX_TASK_QUEUE_LENGTH"));
            taskTableName = ConfigUtils.getResourceBundle().getString("CRAWLER_TASK_TABLE");
        } catch (NumberFormatException e) {
            maxTaskQueueLength = 100;
            e.printStackTrace();
        }

        currentTaskQueueLength = TaskQueue.queueLenth();
        inQueueTaskIdList = new ArrayList<>();
        taskModelDao = new TaskModelDao();

        writeBackEntityList = new ArrayList<>();
    }

    public long getMaxTaskQueueLength(){

        return maxTaskQueueLength;
    }

    public long getCurrentTaskQueueLength(){

        return currentTaskQueueLength;
    }


    public void addToWriteBack(TaskModel model){

        writeBackEntityList.add(model);
    }

    public void cleanWriteBackEntityList(){

        writeBackEntityList = new ArrayList<>();
    }

    public boolean isListContainsEntity (TaskModel o){

        return writeBackEntityList.contains(o);
    }
}
