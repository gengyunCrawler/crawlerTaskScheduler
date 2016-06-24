package com.gonali.crawlerTask.redisQueue;

import com.alibaba.fastjson.JSON;
import com.gonali.crawlerTask.handler.model.HeartbeatMsgModel;
import com.gonali.crawlerTask.message.Message;
import com.gonali.crawlerTask.utils.RedisJedisPoolUtils;
import com.gonali.crawlerTask.utils.LoggerUtils;
import com.gonali.crawlerTask.utils.ObjectSerializeUtils;
import redis.clients.jedis.Jedis;

/**
 * Created by TianyuanPan on 6/4/16.
 */
public class HeartbeatMsgQueue extends LoggerUtils implements MessageQueue {


    private static final String QUEUE_KEY = QueueKeys.QUEUE_KEY_HEARTBEAT_MESSAGE;
    private static final int dbIndex = 7;

    private Jedis jedis;
    private HeartbeatMsgModel heartbeat;

    public HeartbeatMsgQueue() {

        heartbeat = new HeartbeatMsgModel();

    }


    @Override
    public MessageQueue setMessage(Message message) {
        this.heartbeat = (HeartbeatMsgModel) message;
        return this;
    }

    @Override
    public Message pushMessage() {

        try {
            String jsonMsg = JSON.toJSONString(this.heartbeat);
            byte[] bytes = ObjectSerializeUtils.serializeToBytes(jsonMsg);
            jedis = RedisJedisPoolUtils.getJedis();
            jedis.select(dbIndex);
            jedis.lpush(QUEUE_KEY.getBytes(), bytes);

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {

            RedisJedisPoolUtils.cleanJedis(jedis);
        }

        return heartbeat;
    }


    @Override
    public Message popMessage() {

        try {
            jedis = RedisJedisPoolUtils.getJedis();
            jedis.select(dbIndex);
            byte[] bytes = jedis.rpop(QueueKeys.QUEUE_KEY_HEARTBEAT_MESSAGE.getBytes());

            if (bytes == null)
                return null;

            //System.out.println("bytes:" + bytes);
            String jsonMsg = (String) ObjectSerializeUtils.getEntityFromBytes(bytes);
            heartbeat = JSON.parseObject(jsonMsg, HeartbeatMsgModel.class);

            return heartbeat;

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {

            RedisJedisPoolUtils.cleanJedis(jedis);
        }

        return null;
    }
}
