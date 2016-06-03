package com.gonali.crawlerTask.dao;

import com.gonali.crawlerTask.model.EntityModel;

import java.util.List;

/**
 * Created by TianyuanPan on 6/3/16.
 */
public interface QueryDao {

    public int insert(String tableName, EntityModel model);

    public int update(String tableName, EntityModel model);

    public int delete(String tableName, EntityModel model);


    public List<EntityModel> selectByUserId(String tableName, String userId);/* {


    }*/

    public EntityModel selectByTaskId(String tableName, String taskId);

    public List<EntityModel> selectWhere(String tableName, String whereStatement);

    public List<EntityModel> selectAll(String tableName);
}
