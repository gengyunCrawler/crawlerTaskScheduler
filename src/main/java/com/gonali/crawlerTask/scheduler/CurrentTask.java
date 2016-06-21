package com.gonali.crawlerTask.scheduler;

import com.gonali.crawlerTask.handler.model.HeartbeatMsgModel;
import com.gonali.crawlerTask.handler.model.HeartbeatStatusCode;
import com.gonali.crawlerTask.model.TaskModel;
import com.gonali.crawlerTask.model.TaskStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 6/20/16.
 */
public class CurrentTask {

    private class CurrentTaskModel {

        private TaskModel task;
        private TaskStatus status;
        private int heartbeatStatusCode;
        private boolean isFinish;
        private List<HeartbeatMsgModel> heartbeatList;

        public CurrentTaskModel(TaskModel task) {

            this.task = task;
            this.status = TaskStatus.UNCRAWL;
            this.heartbeatStatusCode = HeartbeatStatusCode.STARTING;
            this.isFinish = false;
            this.heartbeatList = new ArrayList<>();
        }

        public String getTaskId() {
            return task.getTaskId();
        }

        public TaskModel getTask() {
            return task;
        }

        public TaskStatus getTaskStatus() {
            return status;
        }

        public void setTaskStatus(TaskStatus status) {
            this.status = status;
        }

        public int getHeartbeatStatusCode() {
            return heartbeatStatusCode;
        }

        public void setHeartbeatStatusCode(int heartbeatStatusCode) {
            this.heartbeatStatusCode = heartbeatStatusCode;
        }

        public boolean isFinish() {

            return isFinish;
        }

        public void setIsFinish(boolean isFinish) {
            this.isFinish = isFinish;
        }


        public List<HeartbeatMsgModel> getHeartbeatList() {
            return heartbeatList;
        }

        public void setHeartbeatList(List<HeartbeatMsgModel> heartbeatList) {
            this.heartbeatList = heartbeatList;
            int code = HeartbeatStatusCode.FINISHED;
            for (HeartbeatMsgModel h : this.heartbeatList)
                code += h.getStatusCode();
            this.heartbeatStatusCode = code;
        }

    }


    private CurrentTaskModel[] currentTaskArray;
    private int taskNumber;
    private int nodes;


    public CurrentTask(int taskNumber) {

        this.taskNumber = taskNumber;
        currentTaskArray = new CurrentTaskModel[this.taskNumber];
    }


    public void cleanFinishedHeartbeat(HeartbeatUpdater heartbeatUpdater) {


        for (CurrentTaskModel task : currentTaskArray) {

            if (task == null)
                continue;
            if (task.isFinish) {

                List<HeartbeatMsgModel> heartbeat = task.getHeartbeatList();
                for (HeartbeatMsgModel h : heartbeat)
                    heartbeatUpdater.cleanFinishedHeartbeat(h.getTaskId(), h.getHostname(), h.getPid());
            }
        }

    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public void setNodes(int nodes) {
        this.nodes = nodes;
    }

    public void addTask(TaskModel task) {

        for (int i = 0; i < this.taskNumber; i++) {
            if (this.currentTaskArray[i] == null) {
                this.currentTaskArray[i] = new CurrentTaskModel(task);
                this.currentTaskArray[i].setIsFinish(false);
                break;
            }
            if (this.currentTaskArray[i].isFinish()) {
                this.currentTaskArray[i] = new CurrentTaskModel(task);
                this.currentTaskArray[i].setIsFinish(false);
                break;
            }
        }

    }


    public boolean isHaveFinishedTask() {

        for (CurrentTaskModel task : currentTaskArray) {

            if (task == null)
                return true;

            if (task.isFinish())
                return true;
        }

        return false;
    }

    public void setTaskStatus(String taskId, TaskStatus status) {

        for (int i = 0; i < taskNumber; i++) {

            if (currentTaskArray[i] == null)
                continue;
            if (currentTaskArray[i].getTaskId().equals(taskId)) {
                currentTaskArray[i].setTaskStatus(status);
            }
        }

    }


    public List<HeartbeatMsgModel> getHeartbeatList(String taskId) {
        for (int i = 0; i < this.taskNumber; i++)
            if (currentTaskArray[i].task.getTaskId().equals(taskId))
                return currentTaskArray[i].getHeartbeatList();
        return null;
    }

    public void setHeartbeatList(List<HeartbeatMsgModel> heartbeatList) {

        for (int i = 0; i < this.taskNumber; i++) {
            String taskId;
            List<HeartbeatMsgModel> list = new ArrayList<>();
            for (HeartbeatMsgModel h : heartbeatList) {
                taskId = h.getTaskId();
                if (currentTaskArray[i] == null)
                    break;
                if (currentTaskArray[i].task.getTaskId().equals(taskId))
                    list.add(h);
            }
            if (currentTaskArray[i] != null) {
                currentTaskArray[i].setHeartbeatList(list);
                //System.out.println("taskId::" + currentTaskArray[i].getTask().getTaskId() + "::Heartbeat::" + JSON.toJSONString(currentTaskArray[i].getHeartbeatList()));
            }
        }
    }

    public List<TaskModel> getUncrawlTask() {

        List<TaskModel> taskModelList = new ArrayList<>();

        for (int i = 0; i < taskNumber; i++) {
            if (currentTaskArray[i] == null)
                continue;
            if (currentTaskArray[i].getTaskStatus() == TaskStatus.UNCRAWL)
                taskModelList.add(currentTaskArray[i].getTask());
        }

        return taskModelList;
    }

    public List<TaskModel> getCrawledTask() {

        List<TaskModel> taskModelList = new ArrayList<>();

        for (int i = 0; i < taskNumber; i++) {

            if (currentTaskArray[i] == null)
                continue;
            if (currentTaskArray[i].isFinish())
                taskModelList.add(currentTaskArray[i].getTask());
        }

        return taskModelList;
    }

    public List<TaskModel> getCurrentTaskElements() {

        List<TaskModel> taskModelList = new ArrayList<>();

        for (int i = 0; i < taskNumber; i++) {

            if (currentTaskArray[i] == null)
                continue;
            taskModelList.add(currentTaskArray[i].getTask());
        }

        return taskModelList;

    }

    public void taskStatusChecking() {

        for (int i = 0; i < taskNumber; i++) {
            if (currentTaskArray[i] == null)
                continue;
            if (currentTaskArray[i].isFinish) {
                currentTaskArray[i].setTaskStatus(TaskStatus.CRAWLED);
            }
        }
    }


    public void taskFinishChecking() {

        int heartbeatCode;

        for (int i = 0; i < taskNumber; i++) {

            if (currentTaskArray[i] == null)
                continue;
            int size = currentTaskArray[i].getHeartbeatList().size();
            if (size != nodes)
                continue;
            heartbeatCode = currentTaskArray[i].getHeartbeatStatusCode();

            if (heartbeatCode == HeartbeatStatusCode.FINISHED) {
                currentTaskArray[i].setHeartbeatStatusCode(HeartbeatStatusCode.FINISHED);
                currentTaskArray[i].setTaskStatus(TaskStatus.CRAWLED);
                currentTaskArray[i].setIsFinish(true);
                System.out.println("FINISHED TASK: taskId = [ " + currentTaskArray[i].getTaskId() + " ]");
            }
        }
    }

    public void taskNodeTimeoutChecking(){

        List<HeartbeatMsgModel> heartbeatList;

        for (int i = 0; i < taskNumber; i++) {
            if (currentTaskArray[i] == null)
                continue;
            if (!currentTaskArray[i].isFinish) {
                heartbeatList = currentTaskArray[i].getHeartbeatList();
                HeartbeatMsgModel h;
                int size = heartbeatList.size();
                for (int j = 0; j < size; j++ ){
                    h = heartbeatList.get(j);
                    if (h.getStatusCode() != HeartbeatStatusCode.FINISHED &&
                            h.getTimeoutCount() > HeartbeatUpdater.getMaxTimeoutCount()) {
                        h.setStatusCode(HeartbeatStatusCode.FINISHED);
                        heartbeatList.set(j, h);
                    }
                }
                currentTaskArray[i].setHeartbeatList(heartbeatList);
            }
        }
    }
}
