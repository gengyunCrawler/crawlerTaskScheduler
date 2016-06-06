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
    private TaskModelDao taskModelDao;
    private TaskModel currentTask;
    private TaskTimer taskTimer;
    private HeartbeatUpdate heartbeatUpdate;
    private List<NodeInfo> nodeInfoList;
    private String command;
    private Ruler ruler;


    private TaskScheduler() {

        taskModelDao = new TaskModelDao();
        currentTask = new TaskModel();
        taskTimer = new TaskTimer();
        heartbeatUpdate = new HeartbeatUpdate();
        nodeInfoList = new ArrayList<>();
    }


    public TaskScheduler registerRuler(Ruler ruler){

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
        for (int i = 1; i <= nodes; ++i) {
            String info = ConfigUtils.getResourceBundle("nodes").getString("NODE_" + i);
            String[] strings = info.split("::");
            nodeInfo = new NodeInfo(strings[0], strings[1], strings[2], command);
            nodeInfoList.add(nodeInfo);
        }

    }


    public static TaskScheduler getTaskScheduler() {

        if (scheduler == null)
            scheduler = new TaskScheduler();
        return scheduler;
    }


    public void schedulerStart() {

        currentTask = new TaskModel();

        setTaskInfo();
        for (NodeInfo node : nodeInfoList) {

            try {

                System.out.println(node.nodeStart());
                Thread.sleep(1000);

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }

//        new Thread(heartbeatUpdate).start();

    }

}
