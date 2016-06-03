package com.gonali.crawlerTask.dao;

import com.gonali.crawlerTask.dao.client.MysqlClient;
import com.gonali.crawlerTask.model.EntityModel;
import com.gonali.crawlerTask.model.TaskModel;
import com.gonali.crawlerTask.model.TaskSeedUrlModel;
import com.gonali.crawlerTask.model.TaskType;
import com.gonali.crawlerTask.utils.LoggerUtil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TianyuanPan on 6/3/16.
 */
public class TaskModelDao extends LoggerUtil implements QueryDao {


    private MysqlClient mysqlClient;

    public TaskModelDao() {

        mysqlClient = new MysqlClient();
    }


    public int insert(String tableName, EntityModel model) {

        if (this.mysqlClient.getConnection() == null)
            return 0;

        int ret = 0;

        try {

            String sql = model.insertSqlBuilder(tableName, model);
            ret = this.mysqlClient.excuteUpdateSql(sql);

            return ret;

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {

            this.mysqlClient.closeConnection();
        }

        return 0;
    }


    public int update(String tableName, EntityModel model) {

        if (this.mysqlClient.getConnection() == null)
            return 0;

        int ret = 0;
        try {

            String sql = model.updateSqlBuilder(tableName, model);

            ret = this.mysqlClient.excuteUpdateSql(sql);

            return ret;

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {

            this.mysqlClient.closeConnection();
        }

        return 0;
    }


    public int delete(String tableName, EntityModel model) {
        return 0;
    }

    public List<EntityModel> selectByUserId(String tableName, String userId) {


        String sql = "SELECT * FROM " + tableName + " WHERE userId = '" + userId + "'";
        ResultSet resultSet;

        if (this.mysqlClient.getConnection() == null)
            return null;

        try {

            resultSet = this.mysqlClient.executeQuerySql(sql);

            List<EntityModel> modelList = new ArrayList<>();

            TaskModel model;

            do {

                model = new TaskModel();

                model.setTaskId(resultSet.getNString("taskId"));
                model.setUserId(resultSet.getNString(""));

                switch (resultSet.getNString("taskType")){
                    case "TOPIC":
                        model.setTaskType(TaskType.TOPIC);
                        break;
                    case "WHOLESITE":
                        model.setTaskType(TaskType.WHOLESITE);
                        break;
                    default:
                        model.setTaskType(TaskType.UNSET);
                        break;
                }
                model.setTaskRemark(resultSet.getNString("taskRemark"));
                model.setTaskSeedUrl(new TaskSeedUrlModel(resultSet.getNString("taskSeedUrl")));
                model.setTaskCrawlerDepth(resultSet.getInt("taskCrawlerDepth"));
                model.setTaskDynamicDepth(resultSet.getInt("taskDynamicDepth"));
                model.setTaskWeight(resultSet.getInt("taskWeight"));
                model.setTaskStartTime(resultSet.getLong("taskStartTime"));


                modelList.add(model);

            } while (resultSet.next());


            return modelList;

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {

            this.mysqlClient.closeConnection();
        }

        return null;
    }

    public EntityModel selectByTaskId(String tableName, String taskId) {
        return null;
    }


    public List<EntityModel> selectWhere(String tableName, String whereStatement) {
        return null;
    }

    public List<EntityModel> selectAll(String tableName) {
        return null;
    }
}
