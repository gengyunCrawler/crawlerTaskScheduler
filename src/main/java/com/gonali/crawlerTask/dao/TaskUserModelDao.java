package com.gonali.crawlerTask.dao;

import com.gonali.crawlerTask.dao.client.MysqlClient;
import com.gonali.crawlerTask.model.EntityModel;
import com.gonali.crawlerTask.utils.LoggerUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by TianyuanPan on 6/3/16.
 */
public class TaskUserModelDao extends LoggerUtil implements QueryDao {


    private MysqlClient mysqlClient;

    public TaskUserModelDao() {

        mysqlClient = new MysqlClient();
    }


    private List<EntityModel> boxingObject(ResultSet resultSet) throws SQLException {

        return null;
    }


    @Override
    public int insert(String tableName, EntityModel model) {
        return 0;
    }

    @Override
    public int update(String tableName, EntityModel model) {
        return 0;
    }

    @Override
    public int delete(String tableName, EntityModel model) {
        return 0;
    }

    @Override
    public List<EntityModel> selectByUserId(String tableName, String userId) {
        return null;
    }

    @Override
    public EntityModel selectByTaskId(String tableName, String taskId) {
        return null;
    }

    @Override
    public List<EntityModel> selectWhere(String tableName, String whereStatement) {
        return null;
    }

    @Override
    public List<EntityModel> selectAll(String tableName) {
        return null;
    }
}
