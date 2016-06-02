package com.gonali.crawlerTask.model;

import java.io.Serializable;
import java.util.*;

/**
 * Created by TianyuanPan on 6/2/16.
 */
public class TaskCrawlerInstanceInfoModel implements Serializable {

    private List<String> nodeHosts;
    private Map<String, Long> nodeHeartbeat;
    private Map<String, TaskStatus> nodeStatus;
    private Map<String, List<Integer>> nodePid;

    public TaskCrawlerInstanceInfoModel() {

        nodeHosts = new ArrayList<String>();
        nodeHeartbeat = new HashMap<String, Long>();
        nodeStatus = new HashMap<String, TaskStatus>();
    }

    public void setNodeHosts(String... hosts) {

        for (int i = 0; i < hosts.length; ++i)
            nodeHosts.add(hosts[i]);

    }

    public void setNodeHeartbeat(String hostname) {

        if (!nodeHosts.contains(hostname))
            return;
        nodeHeartbeat.put(hostname, new Date().getTime());
    }

    public void setNodeStatus(String hostname, TaskStatus taskStatus) {

        if (!nodeHosts.contains(hostname))
            return;

        nodeStatus.put(hostname, taskStatus);
    }

    public List<String> getNodeHosts() {

        return nodeHosts;
    }

    public long getHostheartbeat(String hostname) {

        return nodeHeartbeat.get(hostname);
    }

    public Map<String, Long> getHostheartbeat() {

        return this.nodeHeartbeat;
    }

    public TaskStatus getNodeStatus(String hostname) {

        return nodeStatus.get(hostname);
    }

    public Map<String, TaskStatus> getNodeStatus() {

        return this.nodeStatus;
    }

    public String getCrawlerInstanceInfoJsonString(){

        return "123";
    }

}
