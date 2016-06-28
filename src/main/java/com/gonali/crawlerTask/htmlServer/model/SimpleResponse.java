package com.gonali.crawlerTask.htmlServer.model;

import com.gonali.crawlerTask.handler.model.HeartbeatMsgModel;
import com.gonali.crawlerTask.model.TaskModel;
import com.gonali.crawlerTask.scheduler.TaskScheduler;
import com.gonali.crawlerTask.scheduler.rulers.RulerBase;
import com.sun.net.httpserver.HttpExchange;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TianyuanPan on 6/14/16.
 */
public class SimpleResponse extends ResposeBase {


    @Override
    public String responseContents(HttpExchange exchange, TaskScheduler scheduler) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd/HH:mm:ss");

        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

        List<String[]> hostInfosList = scheduler.getHostInfoList();
        List<String> nodeIpList = new ArrayList<>();
        for (String[] ss : hostInfosList)
            nodeIpList.add(ss[2]);
        long maxQueueLength = ((RulerBase) scheduler.getRuler()).getMaxTaskQueueLength();
        long currentQueueLength = ((RulerBase) scheduler.getRuler()).getCurrentTaskQueueLength();
        int maxTask = scheduler.getCurrentTasks().getTaskNumber();


        int nods = hostInfosList.size();
        String nodeIp = " ";
        for (String s : nodeIpList)
            nodeIp += "(" +s + ") ";

        String htmlPartA = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<meta http-equiv=\"refresh\" content=\"10\">" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "    <title>CrawlerSechedulerStatus</title>\n" +
                "    <style type=\"text/css\">\n" +
                "        <!--\n" +
                "        .STYLE1 {font-weight: bold}\n" +
                "        -->\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "<h1 align=\"center\">调 度 器 状 态<br/>PID:"+ pid +"</h1>\n" +
                "<ul>\n" +
                "    <li class=\"STYLE1\">\n" +
                "        <div align=\"center\">任务节点数量：" + nods + "<strong> 节点IP[ " + nodeIp + " ]</strong></div>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "        <div align=\"center\"><strong>最大队列数：" + maxQueueLength + "</strong></div>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "        <div align=\"center\"><strong>当前队列数：" + currentQueueLength + "</strong></div>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "        <div align=\"center\"><strong>最大任务数：" + maxTask + "</strong></div>\n" +
                "    </li>\n" +
                "</ul>";

        List<TaskModel> taskModelList = scheduler.getCurrentTasks().getCurrentTaskElements();
        String currentTask = "";
        for (TaskModel t : taskModelList) {

            long dateTime = System.currentTimeMillis() - t.getTaskStartTime();
            int hour = (int)((dateTime/1000)/60/60);
            int min = (int)(((dateTime/1000)/60%60));
            int sec = (int)((dateTime/1000)%60);
            currentTask += "<tr>\n" +
                    " <td> " + t.getTaskId() + " </td>\n" +
                    " <td> " + t.getUserId() + " </td>\n" +
                    " <td> " + t.getTaskStatus() + "</td>\n" +
                    " <td> " + dateFormat.format(new Date(t.getTaskStartTime())) + "</td>\n" +
                    " <td> " + hour +" 小时 "+ min + " 分 " + sec + " 秒 " + "</td>\n" +
                    "</tr>";
        }

        String htmlPartB = "<div align=\"center\">\n" +
                "    <table width=\"200\" border=\"1\">\n" +
                "        <caption>\n" +
                "            当前任务状态\n" +
                "        </caption>\n" +
                "        <tr>\n" +
                "            <td> 任务 id </td>\n" +
                "            <td> 用户 id </td>\n" +
                "            <td> 任务状态 </td>\n" +
                "            <td> 开始时间 </td>\n" +
                "            <td> 持续时间 </td>\n" +
                "        </tr>\n" + currentTask +
                "    </table>\n" +
                "    <p>&nbsp;</p>";


        List<String> queueTidList = ((RulerBase) scheduler.getRuler()).getInQueueTaskIdArray();
        String queueTid = "";
        for (String s : queueTidList)
            queueTid += "<tr>\n" +
                    "   <td> " + s + " </td>\n" +
                    "</tr>";

        String htmlPartC = "    <table width=\"200\" border=\"1\">\n" +
                "        <caption>\n" +
                "            在队列的 任务 id ["+ queueTidList.size() +"]\n" +
                "        </caption>\n" + queueTid +
                "    </table>\n" +
                "    <p>&nbsp;</p>";


        List<HeartbeatMsgModel> heartbeatMsgModelList = scheduler.getHeartbeatUpdater().getHeartbeatMsgList();

        String heartbeatMsg = "";


        for (HeartbeatMsgModel h : heartbeatMsgModelList)
            heartbeatMsg += "<tr>\n" +
                    "  <td> " + h.getTaskId() + " </td>\n" +
                    "  <td> " + h.getHostname() + " </td>\n" +
                    "  <td> " + h.getPid() + " </td>\n" +
                    "  <td> " + h.getStatusCode() + " </td>\n" +
                    "  <td> " + dateFormat.format(new Date(h.getTime() ))  + " </td>\n" +
                    "  <td> " + h.getTimeoutCount() + " </td>\n" +
                    "</tr>";

        String htmlPartD = "    <table width=\"200\" border=\"1\">\n" +
                "        <caption>\n" +
                "            节点心跳状态 ["+ heartbeatMsgModelList.size() +"]\n" +
                "        </caption>\n" +
                "        <tr>\n" +
                "            <td> 任务 ID </td>\n" +
                "            <td> 节点 IP </td>\n" +
                "            <td> 进程 PID </td>\n" +
                "            <td> 心 跳 状 态 </td>\n" +
                "            <td> 心 跳 时 间 </td>\n" +
                "            <td> 超 时 计 数 </td>\n" +
                "        </tr>\n" + heartbeatMsg +
                "    </table>\n" +
                "    <!-- <p>\n" +
                "        <input type=\"button\" name=\"refresh\" value=\"resfresh\" onclick=\"window.document.reload();\"/>\n" +
                "    </p>\n -->" +
                "    <p>&nbsp;</p>\n" +
                "    <p>&nbsp;</p>\n" +
                "    <p>&nbsp;</p>\n" +
                "    <p>&nbsp;</p>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";


        return (htmlPartA + htmlPartB + htmlPartC + htmlPartD);
    }


}
