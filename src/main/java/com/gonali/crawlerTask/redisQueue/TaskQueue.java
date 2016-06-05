package com.gonali.crawlerTask.redisQueue;

import com.gonali.crawlerTask.model.TaskModel;
import com.gonali.crawlerTask.utils.JedisPoolUtils;
import com.gonali.crawlerTask.utils.ObjectSerializeUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;

/**
 * Created by TianyuanPan on 6/2/16.
 */
public class TaskQueue {


    private static final String QUEUE_KEY = QueueKeys.QUEUE_KEY_TASK;

    private TaskQueue() {

    }


    public static void pushCrawlerTaskQueue(TaskModel taskModel) {

        Jedis jedis = null;

        try {

            byte[] bytes = ObjectSerializeUtils.serializeToBytes(taskModel);
            jedis = JedisPoolUtils.getJedis();
            jedis.lpush(QUEUE_KEY.getBytes(), bytes);

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
            byte[] bytes = jedis.rpop(QUEUE_KEY.getBytes());
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

    public static long queueLenth() {

        Jedis jedis = null;

        try {
            jedis = JedisPoolUtils.getJedis();
            return jedis.llen(QUEUE_KEY.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            JedisPoolUtils.cleanJedis(jedis);
        }

        return 0;
    }

}
