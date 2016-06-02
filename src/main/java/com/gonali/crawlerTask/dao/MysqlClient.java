package com.gonali.crawlerTask.dao;

import com.gonali.crawlerTask.model.TaskModel;
import com.gonali.crawlerTask.utils.ConfigUtils;
import com.gonali.crawlerTask.utils.LoggerUtil;
import com.gonali.crawlerTask.utils.MySqlPoolUtils;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

/**
 * Created by TianyuanPan on 6/2/16.
 */
public class MysqlClient extends LoggerUtil {

    private Statement myStatement;
    private ConfigUtils configUtils;
    private MySqlPoolUtils pool;
    protected boolean connOpen;
    protected Connection connection;


    private void setConnOpen(boolean connOpen) {
        this.connOpen = connOpen;
    }

    public MysqlClient() {

        this.configUtils = ConfigUtils.getConfigUtils("MYSQL_");
        this.pool = MySqlPoolUtils.getMySqlPoolUtils(configUtils);
        this.myStatement = null;

    }


    public Connection getConnection() {

        try {
            logger.debug("get Mysql connection ...");
            this.connection = this.pool.getConnection();
            this.myStatement = this.connection.createStatement();
            if (this.connection != null)
                this.setConnOpen(true);

        } catch (Exception ex) {

            logger.error("get mysql connection error!! Exception Message: " + ex.getMessage());
            ex.printStackTrace();
            this.setConnOpen(false);
            return null;
        }

        return this.connection;
    }


    public void closeConnection() {

        try {

            this.pool.releaseConnection(this.connection);

            this.setConnOpen(false);

        } catch (Exception ex) {

            this.setConnOpen(false);
            logger.error("connection.close() exception!! Message:" + ex.getMessage());
            ex.printStackTrace();

        }
    }

    public int insert(String tableName, TaskModel taskModel){

        return 0;
    }

    public int update(String tableName, TaskModel taskModel){

        return 0;
    }

    public int delete(String tableName, TaskModel taskModel){

        return 0;
    }

    public TaskModel selectByUserId(String tableName, String userId){

        return null;
    }

    public TaskModel selectByTaskId(String tableName, String taskId){

        return null;
    }

    public TaskModel selectByPrimaryKey(String tableName, String taskId, String userId){

        return null;
    }

    public List<TaskModel> selectWhere(String tableName, String whereStatement){

        return null;
    }

    public List<TaskModel> selectAll(String tableName){

        return null;
    }
}
