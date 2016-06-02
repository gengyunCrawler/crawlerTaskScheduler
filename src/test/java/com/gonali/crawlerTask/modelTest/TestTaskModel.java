package com.gonali.crawlerTask.modelTest;

import com.gonali.crawlerTask.model.*;
import com.gonali.crawlerTask.utils.ObjectSerializeUtils;

/**
 * Created by TianyuanPan on 6/2/16.
 */
public class TestTaskModel {

    static TaskModel taskModel;


    public static void main(String[] args) {


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
        String s = taskModel.insertSqlBuilder("crawlerTaskTable", taskModel);
        System.out.println(s);



            byte[] bytes = ObjectSerializeUtils.serializeToBytes(taskModel);

            System.out.println(bytes);

            TaskModel model1 = (TaskModel)ObjectSerializeUtils.getEntityFromBytes(bytes);
            System.out.println(model1.insertSqlBuilder("table", model1));

    }


}
