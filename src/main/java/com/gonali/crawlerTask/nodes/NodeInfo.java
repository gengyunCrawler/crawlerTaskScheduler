package com.gonali.crawlerTask.nodes;

/**
 * Created by TianyuanPan on 6/4/16.
 */

public class NodeInfo {

    private String username;
    private String password;
    private String hostname;
    private int port = 22;

    private String crawlerPath;
    private String shellName;
    private int instances;
    private int threadPerInstance;

    /* usage */
    private int depth;
    private int pass;
    private String tid;
    private long startTime;
    private String seedPath;
    private String protocolDir;
    private String postRegexDir;
    private String type;
    private int recallDepth;
    private String templateDir;
    private String clickRegexDir;
    private String configPath;

    /* shell utils */
    private ShellUtils shell;

    public NodeInfo(String username, String password, String hostname) {

        this.hostname = hostname;
        this.username = username;
        this.password = password;
        shell = ShellUtils.getShellUtils(this.username, this.password, this.hostname, this.port);
    }

    public String nodeStart() {

        String command = "";
        String usage = " ";

        command = "cd " + crawlerPath + ";./" + shellName + "  ";

        usage += " -depth " + depth;
        usage += " -pass " + pass;
        usage += " -tid " + tid;
        usage += " -startTime " + startTime;
        usage += " -seedPath " + seedPath;
        usage += " -protocolDir " + protocolDir;
        usage += " -postRegexDir " + postRegexDir;
        usage += " -type " + type;
        usage += " -recallDepth " + recallDepth;
        usage += " -templateDir " + templateDir;
        usage += " -clickRegexDir " + clickRegexDir;
        usage += " -configPath " + configPath;

        command += usage;

        return this.shell.doExecuteShell(command);
    }

    public String getCrawlerPath() {
        return crawlerPath;
    }

    public NodeInfo setCrawlerPath(String crawlerPath) {
        this.crawlerPath = crawlerPath;
        return this;
    }

    public String getShellName() {
        return shellName;
    }

    public NodeInfo setShellName(String shellName) {
        this.shellName = shellName;
        return this;
    }

    public int getInstances() {
        return instances;
    }

    public NodeInfo setInstances(int instances) {
        this.instances = instances;
        return this;
    }

    public int getThreadPerInstance() {
        return threadPerInstance;
    }

    public NodeInfo setThreadPerInstance(int threadPerInstance) {
        this.threadPerInstance = threadPerInstance;
        return this;
    }

    public int getDepth() {
        return depth;
    }

    public NodeInfo setDepth(int depth) {
        this.depth = depth;
        return this;
    }

    public int getPass() {
        return pass;
    }

    public NodeInfo setPass(int pass) {
        this.pass = pass;
        return this;
    }

    public String getTid() {
        return tid;
    }

    public NodeInfo setTid(String tid) {
        this.tid = tid;
        return this;
    }

    public long getStartTime() {
        return startTime;
    }

    public NodeInfo setStartTime(long startTime) {
        this.startTime = startTime;
        return this;
    }

    public String getSeedPath() {
        return seedPath;
    }

    public NodeInfo setSeedPath(String seedPath) {
        this.seedPath = seedPath;
        return this;
    }

    public String getProtocolDir() {
        return protocolDir;
    }

    public NodeInfo setProtocolDir(String protocolDir) {
        this.protocolDir = protocolDir;
        return this;
    }

    public String getPostRegexDir() {
        return postRegexDir;
    }

    public NodeInfo setPostRegexDir(String postRegexDir) {
        this.postRegexDir = postRegexDir;
        return this;
    }

    public String getType() {
        return type;
    }

    public NodeInfo setType(String type) {
        this.type = type;
        return this;
    }

    public int getRecallDepth() {
        return recallDepth;
    }

    public NodeInfo setRecallDepth(int recallDepth) {
        this.recallDepth = recallDepth;
        return this;
    }

    public String getTemplateDir() {
        return templateDir;
    }

    public NodeInfo setTemplateDir(String templateDir) {
        this.templateDir = templateDir;
        return this;
    }

    public String getClickRegexDir() {
        return clickRegexDir;
    }

    public NodeInfo setClickRegexDir(String clickRegexDir) {
        this.clickRegexDir = clickRegexDir;
        return this;
    }

    public String getConfigPath() {
        return configPath;
    }

    public NodeInfo setConfigPath(String configPath) {
        this.configPath = configPath;
        return this;
    }
}
