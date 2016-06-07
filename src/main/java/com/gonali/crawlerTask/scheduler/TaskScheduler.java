package com.gonali.crawlerTask.scheduler;

import com.gonali.crawlerTask.dao.TaskModelDao;
import com.gonali.crawlerTask.handler.model.HeartbeatMsgModel;
import com.gonali.crawlerTask.model.TaskModel;
import com.gonali.crawlerTask.nodes.NodeInfo;
import com.gonali.crawlerTask.scheduler.rulers.Ruler;
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
    private Ruler ruler;

    public static TaskModel getSchedulerCurrentTask(){

        if (scheduler == null)
            return null;
        return scheduler.getCurrentTask();
    }

    public static List<HeartbeatMsgModel> getSchedulerHeartbeatMsgList(){

        if (scheduler == null)
            return null;
        return scheduler.heartbeatUpdater.getHeartbeatMsgList();
    }

    public static List<NodeInfo> getSchedulerNodeInfoList(){

        if (scheduler == null)
            return null;
        return scheduler.getNodeInfoList();
    }


    private TaskScheduler() {

        currentTask = null;
        taskTimer = new TaskTimer();
        heartbeatUpdater = new HeartbeatUpdater();
    }


    public TaskScheduler registerRuler(Ruler ruler) {

        if (ruler == null) {
            this.ruler = new SimpleLongTimeFirstRuler();
            return this;
        }
        this.ruler = ruler;
        return this;

    }

    private void setTaskInfo() {

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


    public static TaskScheduler createTaskScheduler() {

        if (scheduler == null)
            scheduler = new TaskScheduler();
        return scheduler;
    }


    public void schedulerStart() {

        new Thread(heartbeatUpdater).start();

        while (true) {

            ruler.writeBack(this);
            currentTask = ruler.doSchedule(this);

            if (currentTask != null) {

                setTaskInfo();

                for (NodeInfo node : nodeInfoList) {

                    try {

                        System.out.println(node.nodeStart());
                        Thread.sleep(1000);

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
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

    public HeartbeatUpdater getHeartbeatUpdater() {
        return heartbeatUpdater;
    }
}
