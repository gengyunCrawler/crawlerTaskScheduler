package com.gonali.crawlerTask.scheduler;

import com.gonali.crawlerTask.handler.model.HeartbeatMsgModel;
import com.gonali.crawlerTask.handler.model.HeartbeatStatusCode;
import com.gonali.crawlerTask.model.TaskModel;
import com.gonali.crawlerTask.model.TaskStatus;
import com.gonali.crawlerTask.nodes.NodeInfo;
import com.gonali.crawlerTask.redisQueue.HeartbeatMsgQueue;
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

    private CurrentTask currentTasks;
    private HeartbeatUpdater heartbeatUpdater;
    private List<NodeInfo> nodeInfoList;
    private String command = "echo \" Hello World !! \"";
    private String sh;
    private Ruler ruler;

    private int nodeNumber;
    private List<String[]> hostInfoList;


    public static CurrentTask getSchedulerCurrentTask() {

        if (scheduler == null)
            return null;
        return scheduler.getCurrentTasks();
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

        heartbeatUpdater = new HeartbeatUpdater();
        try {

            this.sh = ConfigUtils.getResourceBundle().getString("NODE_START_SH");

        } catch (Exception e) {
            sh = "crawlerStart.sh";
            e.printStackTrace();
        }
        int currentTaskSize;

        try {
            currentTaskSize = Integer.parseInt(ConfigUtils.getResourceBundle().getString("CURRENT_TASK_ARRAY_SIZE"));
        } catch (NumberFormatException e) {
            currentTaskSize = 3;
            e.printStackTrace();
        }

        try {
            nodeNumber = Integer.parseInt(ConfigUtils.getResourceBundle("nodes").getString("NODES"));
        } catch (NumberFormatException e) {
            nodeNumber = 0;
            e.printStackTrace();
        }


        hostInfoList = new ArrayList<>();

        try {
            for (int i = 1; i <= nodeNumber; ++i) {
                String info = ConfigUtils.getResourceBundle("nodes").getString("NODE_" + i);
                hostInfoList.add(info.split("::"));
            }
        } catch (Exception e) {

            e.printStackTrace();
        }


        currentTasks = new CurrentTask(currentTaskSize);
        currentTasks.setNodes(nodeNumber);

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
     */
    private void setTaskStartInfo(TaskModel currentTask) {

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


        NodeInfo nodeInfo;

        nodeInfoList = new ArrayList<>();

        for (int i = 0; i < nodeNumber; ++i) {

            nodeInfo = new NodeInfo(hostInfoList.get(i)[0], hostInfoList.get(i)[1], hostInfoList.get(i)[2], command);
            nodeInfoList.add(nodeInfo);
        }

    }

    private void killTimeoutNodes(CurrentTask currentTasks) {

        NodeInfo nodeInfo;
        List<HeartbeatMsgModel> heartbeatMsgModelList;


        List<TaskModel> taskModelList = currentTasks.getCurrentTaskElements();
        nodeInfoList = new ArrayList<>();

        for(TaskModel t : taskModelList){

            heartbeatMsgModelList = currentTasks.getHeartbeatList(t.getTaskId());

            for (HeartbeatMsgModel h : heartbeatMsgModelList){

                if (h.getStatusCode() != HeartbeatStatusCode.FINISHED &&
                        h.getTimeoutCount() > HeartbeatUpdater.getMaxTimeoutCount()){

                    for(String[] ss : hostInfoList){
                        if (ss[2].equals(h.getHostname())){
                            nodeInfo = new NodeInfo(ss[0], ss[1], ss[2], "kill -9 " + h.getPid());
                            nodeInfoList.add(nodeInfo);
                        }
                    }
                }
            }
        }

        for(NodeInfo n : nodeInfoList){

            String output = n.nodeExecute();

            System.out.println("===>> kill timeout node, COMMAND = [ " + n.getCommand() + " ]:" + output);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    private void cleanTaskQueue() {

        while (TaskQueue.popCrawlerTaskQueue() != null) ;
    }

    private void cleanHeartbeatMsgQueue() {

        HeartbeatMsgQueue queue = new HeartbeatMsgQueue();

        while (queue.popMessage() != null) ;
    }

    public static TaskScheduler createTaskScheduler() {

        if (scheduler == null)
            scheduler = new TaskScheduler();
        return scheduler;
    }


    public void schedulerStart() {

        cleanTaskQueue();
        cleanHeartbeatMsgQueue();
        new Thread(heartbeatUpdater).start();

        while (true) {


            try {
                ruler.writeBack(this);
                currentTasks = ruler.doSchedule(this);

                List<TaskModel> tasks = currentTasks.getUncrawlTask();

                for (TaskModel t : tasks) {

                    t.setTaskStatus(TaskStatus.CRAWLING);
                    currentTasks.setTaskStatus(t.getTaskId(), t.getTaskStatus());
                    ((RulerBase) ruler).addToWriteBack(t);

                    setTaskStartInfo(t);

                    System.out.println("Starting task, Id = [ " + t.getTaskId() + " ]");

                    for (NodeInfo node : nodeInfoList) {

                        try {

                            String startMsgOut = node.nodeExecute();
                            System.out.println("Start node [" + node.getHostname() + "]\n\t" + startMsgOut);
                            Thread.sleep(500);

                        } catch (Exception ex) {

                            ex.printStackTrace();
                        }
                    }
                }

                killTimeoutNodes(currentTasks);

                currentTasks.taskStatusChecking();
                currentTasks.taskNodeTimeoutChecking();
                currentTasks.taskFinishChecking();
                currentTasks.cleanFinishedHeartbeat(this.heartbeatUpdater);

                System.out.println("Is CurrentTaskArray have finished task ? : " + currentTasks.isHaveFinishedTask());

            } catch (Exception e) {

                e.printStackTrace();
            }

            try {

                Thread.sleep(500);

            } catch (InterruptedException e) {

                e.printStackTrace();
            }

        }

    }


    public CurrentTask getCurrentTasks() {
        return currentTasks;
    }

    public int getNodeNumber() {
        return nodeNumber;
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
