package com.gonali.crawlerTask.handler.model;

import com.gonali.crawlerTask.message.Message;

/**
 * Created by TianyuanPan on 6/4/16.
 */
public class HeartbeatMsgModel implements Message{

    private String hostname;
    private int    pid;
    private int    theads;
    private long   time;
    private int    statusCode;

    public HeartbeatMsgModel(){

        hostname = "";
        pid = -1;
        theads = 1;
        statusCode = -1;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getTheads() {
        return theads;
    }

    public void setTheads(int theads) {
        this.theads = theads;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public Message getMessage() {

        return this;
    }
}
