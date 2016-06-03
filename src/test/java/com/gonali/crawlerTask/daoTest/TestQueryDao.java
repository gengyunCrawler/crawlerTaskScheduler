package com.gonali.crawlerTask.daoTest;

import com.gonali.crawlerTask.model.*;

import java.util.Date;

/**
 * Created by TianyuanPan on 6/2/16.
 */
public class TestQueryDao {

//    static MysqlClient client;

    public static void main(String[] args) {

        TaskUserModel taskUser = new TaskUserModel();
        taskUser.setUserId("123");
        taskUser.setUserDescription("fadsfadfad");
        taskUser.setUserAppkey("dfasdafdafads");

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("tid-" + new Date().getTime());
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
//
//        client = new MysqlClient();
//        client.selectByUserId("crawlerTaskUserTable", "123");

//        int ret = client.insert("crawlerTaskTable", taskModel);
//
//        System.out.println("insert return code: " + ret);
    }

}
