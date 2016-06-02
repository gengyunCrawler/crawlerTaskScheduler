package com.gonali.crawlerTask.model;

/**
 * Created by TianyuanPan on 6/2/16.
 */
public class TaskModel implements EntityModel {

    private String taskId = "";
    private TaskType taskType;
    private String taskRemark = "";
    private TaskSeedUrlModel taskSeedUrl;
    private int taskCrawlerDepth;
    private int taskDynamicDepth;
    private int taskWeight;
    private long taskStartTime;
    private int taskRecrawlerDays;
    private String taskTemplatePath = "";
    private String taskTagPath = "";
    private String taskProtocolFilter = "";
    private String taskSuffixFilter = "";
    private String taskRegexFilter = "";
    private TaskStatus taskStatus;
    private boolean taskDeleteFlag;
    private int taskSeedAmount;
    private int taskSeedImportAmount;
    private int taskCompleteTimes;
    private int taskNodeThread;
    private long taskStopTime;
    private TaskCrawlerAmountInfoModel taskCrawlerAmountInfo;
    private TaskCrawlerInstanceInfoModel taskCrawlerInstanceInfo;

    private TaskUserModel taskUser;


    public TaskModel() {

        this.taskDeleteFlag = false;
        this.taskType = TaskType.UNSET;
        this.taskStatus = TaskStatus.UNCRAWL;
        this.taskNodeThread = 1;
    }

    public TaskModel(String taskId, String userId) {

        this.taskId = taskId;
        this.taskUser.setUserId(userId);
        this.taskDeleteFlag = false;
        this.taskType = TaskType.UNSET;
        this.taskStatus = TaskStatus.UNCRAWL;
        this.taskNodeThread = 1;
    }


    public String getUserId() {
        return this.taskUser.getUserId();
    }

    public void setUserId(String userId) {
        this.taskUser.setUserId(userId);
    }

    public String getUserAppkey() {
        return this.taskUser.getUserAppkey();
    }

    public void setUserAppkey(String userAppkey) {
        this.taskUser.setUserAppkey(userAppkey);
    }

    public String getUserDescription() {
        return this.taskUser.getUserDescription();
    }

    public void setUserDescription(String userDescription) {
        this.taskUser.setUserDescription(userDescription);
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public String getTaskRemark() {
        return taskRemark;
    }

    public void setTaskRemark(String taskRemark) {
        this.taskRemark = taskRemark;
    }

    public TaskSeedUrlModel getTaskSeedUrl() {
        return taskSeedUrl;
    }

    public void setTaskSeedUrl(TaskSeedUrlModel taskSeedUrl) {
        this.taskSeedUrl = taskSeedUrl;
    }

    public int getTaskCrawlerDepth() {
        return taskCrawlerDepth;
    }

    public void setTaskCrawlerDepth(int taskCrawlerDepth) {
        this.taskCrawlerDepth = taskCrawlerDepth;
    }

    public int getTaskDynamicDepth() {
        return taskDynamicDepth;
    }

    public void setTaskDynamicDepth(int taskDynamicDepth) {
        this.taskDynamicDepth = taskDynamicDepth;
    }

    public int getTaskWeight() {
        return taskWeight;
    }

    public void setTaskWeight(int taskWeight) {
        this.taskWeight = taskWeight;
    }

    public long getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(long taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public int getTaskRecrawlerDays() {
        return taskRecrawlerDays;
    }

    public void setTaskRecrawlerDays(int taskRecrawlerDays) {
        this.taskRecrawlerDays = taskRecrawlerDays;
    }

    public String getTaskTemplatePath() {
        return taskTemplatePath;
    }

    public void setTaskTemplatePath(String taskTemplatePath) {
        this.taskTemplatePath = taskTemplatePath;
    }

    public String getTaskTagPath() {
        return taskTagPath;
    }

    public void setTaskTagPath(String taskTagPath) {
        this.taskTagPath = taskTagPath;
    }

    public String getTaskProtocolFilter() {
        return taskProtocolFilter;
    }

    public void setTaskProtocolFilter(String taskProtocolFilter) {
        this.taskProtocolFilter = taskProtocolFilter;
    }

    public String getTaskSuffixFilter() {
        return taskSuffixFilter;
    }

    public void setTaskSuffixFilter(String taskSuffixFilter) {
        this.taskSuffixFilter = taskSuffixFilter;
    }

    public String getTaskRegexFilter() {
        return taskRegexFilter;
    }

    public void setTaskRegexFilter(String taskRegexFilter) {
        this.taskRegexFilter = taskRegexFilter;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public boolean isTaskDeleteFlag() {
        return taskDeleteFlag;
    }

    public void setTaskDeleteFlag(boolean taskDeleteFlag) {
        this.taskDeleteFlag = taskDeleteFlag;
    }

    public int getTaskSeedAmount() {
        return taskSeedAmount;
    }

    public void setTaskSeedAmount(int taskSeedAmount) {
        this.taskSeedAmount = taskSeedAmount;
    }

    public int getTaskSeedImportAmount() {
        return taskSeedImportAmount;
    }

    public void setTaskSeedImportAmount(int taskSeedImportAmount) {
        this.taskSeedImportAmount = taskSeedImportAmount;
    }

    public int getTaskCompleteTimes() {
        return taskCompleteTimes;
    }

    public void setTaskCompleteTimes(int taskCompleteTimes) {
        this.taskCompleteTimes = taskCompleteTimes;
    }

    public int getTaskNodeThread() {
        return taskNodeThread;
    }

    public void setTaskNodeThread(int taskNodeThread) {
        this.taskNodeThread = taskNodeThread;
    }

    public long getTaskStopTime() {
        return taskStopTime;
    }

    public void setTaskStopTime(long taskStopTime) {
        this.taskStopTime = taskStopTime;
    }

    public TaskCrawlerAmountInfoModel getTaskCrawlerAmountInfo() {
        return taskCrawlerAmountInfo;
    }

    public void setTaskCrawlerAmountInfo(TaskCrawlerAmountInfoModel taskCrawlerAmountInfo) {
        this.taskCrawlerAmountInfo = taskCrawlerAmountInfo;
    }

    public TaskCrawlerInstanceInfoModel getTaskCrawlerInstanceInfo() {
        return taskCrawlerInstanceInfo;
    }

    public void setTaskCrawlerInstanceInfo(TaskCrawlerInstanceInfoModel taskCrawlerInstanceInfo) {
        this.taskCrawlerInstanceInfo = taskCrawlerInstanceInfo;
    }

    public TaskUserModel getTaskUser() {
        return taskUser;
    }

    public void setTaskUser(TaskUserModel taskUser) {
        this.taskUser = taskUser;
    }


    public String insertSqlBuilder(String tableName, EntityModel taskModel) {

        String values = "'" + ((TaskModel) taskModel).getUserId() + "'," +
                "'" + ((TaskModel) taskModel).getTaskId() + "'," +
                "'" + ((TaskModel) taskModel).getTaskType() + "'," +
                "'" + ((TaskModel) taskModel).getTaskRemark() + "'," +
                "'" + ((TaskModel) taskModel).getTaskSeedUrl().getSeedurlJsonString() + "'," +
                ((TaskModel) taskModel).getTaskCrawlerDepth() + "," +
                ((TaskModel) taskModel).getTaskDynamicDepth() + "," +
                ((TaskModel) taskModel).getTaskWeight() + "," +
                ((TaskModel) taskModel).getTaskStartTime() + "," +
                ((TaskModel) taskModel).getTaskRecrawlerDays() + "," +
                "'" + ((TaskModel) taskModel).getTaskTemplatePath() + "'," +
                "'" + ((TaskModel) taskModel).getTaskTagPath() + "'," +
                "'" + ((TaskModel) taskModel).getTaskProtocolFilter() + "'," +
                "'" + ((TaskModel) taskModel).getTaskSuffixFilter() + "'," +
                "'" + ((TaskModel) taskModel).getTaskRegexFilter() + "'," +
                "'" + ((TaskModel) taskModel).getTaskStatus() + "'," +
                ((TaskModel) taskModel).isTaskDeleteFlag() + "," +
                ((TaskModel) taskModel).getTaskSeedAmount() + "," +
                ((TaskModel) taskModel).getTaskSeedImportAmount() + "," +
                ((TaskModel) taskModel).getTaskCompleteTimes() + "," +
                ((TaskModel) taskModel).getTaskNodeThread() + "," +
                ((TaskModel) taskModel).getTaskStopTime() + "," +
                "'" + ((TaskModel) taskModel).getTaskCrawlerAmountInfo().getCrawlerAmountInfoJsonString() + "'," +
                "'" + ((TaskModel) taskModel).getTaskCrawlerInstanceInfo().getCrawlerInstanceInfoJsonString() + "'";


        String sql = "INSERT INTO " + tableName +
                "(userId, taskId, " +
                " taskType, taskRemark, taskSeedUrl, taskCrawlerDepth, " +
                " taskDynamicDepth, taskWeight, taskStartTime, " +
                " taskRecrawlerDays, taskTemplatePath, taskTagPath, " +
                " taskProtocolFilter, taskSuffixFilter, taskRegexFilter, " +
                " taskStatus, taskDeleteFlag, taskSeedAmount, taskSeedImportAmount, " +
                " taskCompleteTimes, taskNodeThread, taskStopTime, " +
                " taskCrawlerAmountInfo, taskCrawlerInstanceInfo) VALUES (" + values + ")";

        return sql;
    }

    public String updateSqlBuilder(String tableName, EntityModel model) {
        return null;
    }
}
