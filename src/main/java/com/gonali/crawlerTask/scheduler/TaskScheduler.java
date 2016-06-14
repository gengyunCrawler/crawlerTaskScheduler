package com.gonali.crawlerTask.scheduler;

import com.gonali.crawlerTask.handler.model.HeartbeatMsgModel;
import com.gonali.crawlerTask.model.TaskModel;
import com.gonali.crawlerTask.model.TaskStatus;
import com.gonali.crawlerTask.nodes.NodeInfo;
import com.gonali.crawlerTask.redisQueue.TaskQueue;
import com.gonali.crawlerTask.scheduler.rulers.Ruler;
import com.gonali.crawlerTask.scheduler.rulers.RulerBase;
import com.gonali.crawlerTask.scheduler.rulers.SimpleLongTimeFirstRuler;
import com.gonali.crawlerTask.utils.ConfigUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 6/5/16.
 */
public class TaskScheduler {

    private static volatile TaskScheduler scheduler;

    private TaskModel currentTask;
    private TaskTimer taskTimer;
    private HeartbeatUpdater heartbeatUpdater;
    private List<NodeInfo> nodeInfoList;
    private String command = "echo \" Hello World !! \"";
    private String sh;
    private Ruler ruler;



    private boolean isCurrentFinish;

    public static TaskModel getSchedulerCurrentTask() {

        if (scheduler == null)
            return null;
        return scheduler.getCurrentTask();
    }

    public static List<HeartbeatMsgModel> getSchedulerHeartbeatMsgList() {

        if (scheduler == null)
            return null;
        return scheduler.heartbeatUpdater.getHeartbeatMsgList();
    }

    public static List<NodeInfo> getSchedulerNodeInfoList() {

        if (scheduler == null)
            return null;
        return scheduler.getNodeInfoList();
    }


    private TaskScheduler() {

        currentTask = null;
        taskTimer = new TaskTimer();
        heartbeatUpdater = new HeartbeatUpdater();
        try {

            this.sh = ConfigUtils.getResourceBundle().getString("NODE_START_SH");

        } catch (Exception e) {
            sh = "crawlerStart.sh";
            e.printStackTrace();
        }
        isCurrentFinish = false;
    }


    public TaskScheduler registerRuler(Ruler ruler) {

        if (ruler == null) {
            this.ruler = new SimpleLongTimeFirstRuler();
            return this;
        }
        this.ruler = ruler;
        return this;

    }

    /**
     *
     * $(1):  depth
     * $(2):  pass
     * $(3):  tid
     * $(4):  startTime
     * $(5):  seedPath
     * $(6):  protocolDir
     * $(7):  type
     * $(8):  recallDepth
     * $(9):  templateDir
     * $(10): clickRegexDir
     * $(11): postRegexDir
     * $(12): configPath
     *
     *
     */
    private void setTaskInfo() {

        command = sh +
                "  " + currentTask.getTaskCrawlerDepth() +
                "  " + currentTask.getTaskPass() +
                "  " + currentTask.getTaskId() +
                "  " + currentTask.getTaskStartTime() +
                "  " + currentTask.getTaskSeedPath() +
                "  " + currentTask.getTaskProtocolFilterPath() +
                "  " + currentTask.getTaskType() +
                "  " + currentTask.getTaskCrawlerDepth() +
                "  " + currentTask.getTaskTemplatePath() +
                "  " + currentTask.getTaskClickRegexPath() +
                "  " + currentTask.getTaskRegexFilterPath() +
                "  " + currentTask.getTaskConfigPath();

        int nodes = Integer.parseInt(ConfigUtils.getResourceBundle("nodes").getString("NODES"));

        NodeInfo nodeInfo;

        nodeInfoList = new ArrayList<>();

        for (int i = 1; i <= nodes; ++i) {

            String info = ConfigUtils.getResourceBundle("nodes").getString("NODE_" + i);
            String[] strings = info.split("::");
            nodeInfo = new NodeInfo(strings[0], strings[1], strings[2], command);

            nodeInfoList.add(nodeInfo);
        }

    }

    private void cleanTaskQueue(){

        while (TaskQueue.popCrawlerTaskQueue() != null);
    }

    public static TaskScheduler createTaskScheduler() {

        if (scheduler == null)
            scheduler = new TaskScheduler();
        return scheduler;
    }


    public void schedulerStart() {

        cleanTaskQueue();
        new Thread(heartbeatUpdater).start();
        while (true) {


            ruler.writeBack(this);

            currentTask = ruler.doSchedule(this);

            if (currentTask != null &&
                    !isCurrentFinish &&
                    currentTask.getTaskStatus() == TaskStatus.UNCRAWL) {

                currentTask.setTaskStatus(TaskStatus.CRAWLING);
                ((RulerBase)ruler).addToWriteBack(currentTask);
                setTaskInfo();

                for (NodeInfo node : nodeInfoList) {

                    try {

                        System.out.println(node.nodeStart());
                        Thread.sleep(1000);

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
            } else if (isCurrentFinish) {

                ruler.writeBack(this);
                currentTask = null;
            }

            try {

                Thread.sleep(300);

            } catch (InterruptedException e) {

                e.printStackTrace();
            }

        }

    }


    public TaskModel getCurrentTask() {
        return currentTask;
    }

    public List<NodeInfo> getNodeInfoList() {
        return nodeInfoList;
    }

    public String getCommand() {
        return command;
    }

    public Ruler getRuler() {
        return ruler;
    }

    public void setIsCurrentFinish(boolean isCurrentFinish) {
        this.isCurrentFinish = isCurrentFinish;
    }

    public boolean isCurrentFinish() {
        return isCurrentFinish;
    }

    public HeartbeatUpdater getHeartbeatUpdater() {
        return heartbeatUpdater;
    }

}
