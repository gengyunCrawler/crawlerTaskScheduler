package com.gonali.crawlerTask.scheduler;

import com.gonali.crawlerTask.dao.TaskModelDao;
import com.gonali.crawlerTask.model.TaskModel;
import com.gonali.crawlerTask.nodes.NodeInfo;
import com.gonali.crawlerTask.utils.ConfigUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 6/5/16.
 */
public class TaskScheduler {

    private static volatile TaskScheduler scheduler;
    private static TaskModelDao taskModelDao;
    private static TaskModel currentTask;
    private static TaskTimer taskTimer;

    private static List<NodeInfo> nodeInfoList;


    static {

        taskModelDao = new TaskModelDao();
        taskTimer = new TaskTimer();
        nodeInfoList = new ArrayList<>();

        int nodes = Integer.parseInt(ConfigUtils.getResourceBundle("nodes").getString("NODES"));
        NodeInfo nodeInfo;
        for (int i = 1; i <= nodes; ++i) {
            String info = ConfigUtils.getResourceBundle("nodes").getString("NODE_" + i);
            String[] strings = info.split("::");
            nodeInfo = new NodeInfo(strings[0], strings[1], strings[2]);
            nodeInfoList.add(nodeInfo);
        }
    }


    private static void setTaskInfo() {

        for (NodeInfo node : nodeInfoList) {

            node.setDepth(currentTask.getTaskCrawlerDepth())
                    .setPass(currentTask.getTaskCompleteTimes())
                    .setTid(currentTask.getTaskId())
                    .setStartTime(currentTask.getTaskStartTime())
                    .setSeedPath(currentTask.getTaskSeedPath())
                    .setProtocolDir(currentTask.getTaskProtocolFilter())
                    .setPostRegexDir(currentTask.getTaskSuffixFilter())
                    .setType(currentTask.getTaskType().name())
                    .setTemplateDir(currentTask.getTaskTemplatePath());
//                    .setShellName("myls")
//                    .setCrawlerPath("~/Desktop");
        }
    }


    private TaskScheduler() {

    }

    public TaskScheduler getTaskScheduler(){

        if (scheduler == null)
            scheduler = new TaskScheduler();
        return scheduler;
    }

    public  synchronized  void updateHeartbeat(){

    }

    public static void schedulerStart() {

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

    }

}
