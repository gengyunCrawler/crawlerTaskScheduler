package com.gonali.crawlerTask.redisQueueTest;

import com.alibaba.fastjson.JSON;
import com.gonali.crawlerTask.model.*;
import com.gonali.crawlerTask.redisQueue.TaskQueue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TianyuanPan on 6/2/16.
 */
public class TestTaskQueue {

    private   static List<TaskModel> taskModelList;

    static {

        taskModelList = new ArrayList<TaskModel>();
        int i = 1;

        TaskModel taskModel;

        TaskUserModel taskUser = new TaskUserModel();
        taskUser.setUserId("123");
        taskUser.setUserDescription("fadsfadfad");
        taskUser.setUserAppkey("dfasdafdafads");

        taskModel = new TaskModel();
        taskModel.setTaskCrawlerAmountInfo(new TaskCrawlerAmountInfoModel("[\n" +
                "\n" +
                "  {\"times\":0, \"amount\": 512},\n" +
                "  {\"times\":1, \"amount\": 512},\n" +
                "  {\"times\":2, \"amount\": 512},\n" +
                "  {\"times\":3, \"amount\": 512}\n" +
                "\n" +
                "]"));
        taskModel.setTaskCrawlerInstanceInfo(new TaskCrawlerInstanceInfoModel());
        taskModel.setTaskSeedUrl(new TaskSeedUrlModel("[\n" +
                "  \"seedUrl 1\",\n" +
                "  \"seedUrl 2\",\n" +
                "  \"seedUrl 3\",\n" +
                "  \"seedUrl 4\"\n" +
                "]"));
        taskModel.setTaskUser(taskUser);

        while (i < 11){

           // TaskModel model = new TaskModel("NO." + i + "_tid_" + new Date().getTime(), "testQueue");
            taskModel.setTaskId("NO." + i + "_tid_" + new Date().getTime());
            taskModel.setUserId("testQueue");
            taskModelList.add(taskModel);
            ++i;
        }

    }

    public TestTaskQueue(){}

    private static void pushTest(){

        for (int i = 0; i < taskModelList.size(); ++i) {
            TaskQueue.pushCrawlerTaskQueue(taskModelList.get(i));
            System.out.println("PUSH NO. " + (i+1) + " " + taskModelList.get(i).getTaskId());
        }
    }

    private static void popTest(){

        TaskModel model;
        int i = 1;
        while ((model = TaskQueue.popCrawlerTaskQueue()) != null){

            System.out.println("NO. " + i + ": " + JSON.toJSONString(model));
            ++i;
        }
    }



    public static void main(String[] args) {

        try {

            pushTest();
            Thread.sleep(6 * 1000);
            popTest();

        }catch (Exception ex){

            ex.printStackTrace();
        }
    }

}
