package com.gonali.crawlerTask.scheduler.rulers;

import com.gonali.crawlerTask.dao.TaskModelDao;
import com.gonali.crawlerTask.redisQueue.TaskQueue;
import com.gonali.crawlerTask.utils.ConfigUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 6/7/16.
 */
public abstract class RulerBase implements Ruler {


    protected TaskModelDao taskModelDao;
    protected long maxTaskQueueLenth;
    protected long currentTaskQueueLenth;
    protected List<String> inQueueTaskIdList;
    protected String taskTableName = "crawlerTaskTable";

    protected RulerBase(){

        try {
            maxTaskQueueLenth = Integer.parseInt(ConfigUtils.getResourceBundle().getString("MAX_TASK_QUEUE_LENTH"));
            taskTableName = ConfigUtils.getResourceBundle().getString("CRAWLER_TASK_TABLE");
        } catch (NumberFormatException e) {
            maxTaskQueueLenth = 100;
            e.printStackTrace();
        }

        currentTaskQueueLenth = TaskQueue.queueLenth();
        inQueueTaskIdList = new ArrayList<>();
        taskModelDao = new TaskModelDao();
    }

    public long getMaxTaskQueueLenth(){

        return maxTaskQueueLenth;
    }

    public long getCurrentTaskQueueLenth(){

        return currentTaskQueueLenth;
    }
}
