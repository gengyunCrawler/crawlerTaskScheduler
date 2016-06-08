package com.gonali.crawlerTask.daoTest;

import com.alibaba.fastjson.JSON;
import com.gonali.crawlerTask.dao.QueryDao;
import com.gonali.crawlerTask.dao.TaskModelDao;
import com.gonali.crawlerTask.dao.client.MysqlClient;
import com.gonali.crawlerTask.model.*;

import java.util.Date;
import java.util.List;

/**
 * Created by TianyuanPan on 6/2/16.
 */
public class TestQueryDao {

    static TaskModelDao dao;

    public static void main(String[] args) {

        dao = new TaskModelDao();


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

        dao.insert("crawlerTaskTable", taskModel);




/*        List<EntityModel> taskModelList;

        taskModelList = dao.selectAll("crawlerTaskTable");

        if (taskModelList != null) {
            System.out.println(JSON.toJSONString(taskModelList));
        }*/

/*        EntityModel model = dao.selectByTaskId("crawlerTaskTable", "tid-1465287568305");

        if (model != null) {

            ((TaskModel)model).setUserId("App:123456");

            dao.update("crawlerTaskTable", model);
        }*/
    }

}
