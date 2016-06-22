package com.gonali.crawlerTask.scheduler.rulers;

import com.gonali.crawlerTask.dao.TaskModelDao;
import com.gonali.crawlerTask.model.EntityModel;
import com.gonali.crawlerTask.model.TaskModel;
import com.gonali.crawlerTask.redisQueue.TaskQueue;
import com.gonali.crawlerTask.scheduler.CurrentTask;
import com.gonali.crawlerTask.utils.ConfigUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    private Lock myLock;

    protected RulerBase() {

        try {
            maxTaskQueueLength = Integer.parseInt(ConfigUtils.getResourceBundle().getString("MAX_TASK_QUEUE_LENGTH"));
            taskTableName = ConfigUtils.getResourceBundle().getString("CRAWLER_TASK_TABLE");
        } catch (NumberFormatException e) {
            maxTaskQueueLength = 100;
            e.printStackTrace();
        }

        //currentTaskQueueLength = TaskQueue.queueLenth();
        inQueueTaskIdList = new ArrayList<>();
        taskModelDao = new TaskModelDao();

        writeBackEntityList = new ArrayList<>();

        myLock = new ReentrantLock();
    }

    public long getMaxTaskQueueLength() {

        return maxTaskQueueLength;
    }

    public long getCurrentTaskQueueLength() {
        currentTaskQueueLength = TaskQueue.queueLenth();
        return currentTaskQueueLength;
    }


    public void addToWriteBack(TaskModel model) {

        writeBackEntityList.add(model);
    }

    public void cleanWriteBackEntityList() {

        writeBackEntityList = new ArrayList<>();
    }

    public boolean isListContainsEntity(TaskModel o) {

        return writeBackEntityList.contains(o);
    }

    public List<String> getInQueueTaskIdList() {
        List<String> inQueue = new ArrayList<>();
        String new_s;

        try {
            myLock.lock();
            for (String s : inQueueTaskIdList) {

                new_s = new String(s);

                inQueue.add(new_s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            myLock.unlock();
        }

        return inQueue;

    }

    public void removeInQueueTaskId(String taskId) {

        try {
            myLock.lock();
            int size = inQueueTaskIdList.size();
            int i;
            for (i = 0; i < size; i++) {

                if (inQueueTaskIdList.get(i).equals(taskId)) {
                    inQueueTaskIdList.remove(i);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myLock.unlock();
        }
    }

    public boolean isInQueueTaskIdHaveThis(String taskId) {

        try {
            myLock.lock();

            for (String s : inQueueTaskIdList)
                if (s.equals(taskId))
                    return true;

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            myLock.unlock();
        }

        return false;
    }

    public void addTaskIdToInQueue(String taskId) {

        if (!isInQueueTaskIdHaveThis(taskId))
            inQueueTaskIdList.add(taskId);
    }
}
