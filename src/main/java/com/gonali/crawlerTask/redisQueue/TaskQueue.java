package com.gonali.crawlerTask.redisQueue;

import com.gonali.crawlerTask.model.TaskModel;
import com.gonali.crawlerTask.utils.JedisPoolUtils;
import com.gonali.crawlerTask.utils.ObjectSerializeUtils;
import redis.clients.jedis.Jedis;

/**
 * Created by TianyuanPan on 6/2/16.
 */
public class TaskQueue {


    private TaskQueue() {

    }


    public static void pushCrawlerTaskQueue(TaskModel taskModel) {

        Jedis jedis = null;

        try {

            byte[] bytes = ObjectSerializeUtils.serializeToBytes(taskModel);
            jedis = JedisPoolUtils.getJedis();
            jedis.lpush(QueueKeys.QUEUE_KEY_TASK.getBytes(), bytes);

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {

            JedisPoolUtils.cleanJedis(jedis);
        }

    }

    public static TaskModel popCrawlerTaskQueue() {

        Jedis jedis = null;

        try {

            jedis = JedisPoolUtils.getJedis();
            byte[] bytes = jedis.rpop(QueueKeys.QUEUE_KEY_TASK.getBytes());
            if (bytes == null)
                return null;
            TaskModel taskModel = (TaskModel) ObjectSerializeUtils.getEntityFromBytes(bytes);

            return taskModel;

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {

            JedisPoolUtils.cleanJedis(jedis);
        }

        return null;
    }

}