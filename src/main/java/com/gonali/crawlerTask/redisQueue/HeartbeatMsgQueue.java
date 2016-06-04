package com.gonali.crawlerTask.redisQueue;

import com.gonali.crawlerTask.handler.model.HeartbeatMsgModel;
import com.gonali.crawlerTask.message.Message;
import com.gonali.crawlerTask.utils.JedisPoolUtils;
import com.gonali.crawlerTask.utils.LoggerUtil;
import com.gonali.crawlerTask.utils.ObjectSerializeUtils;
import redis.clients.jedis.Jedis;

/**
 * Created by TianyuanPan on 6/4/16.
 */
public class HeartbeatMsgQueue extends LoggerUtil implements MessageQueue {


    private static final String QUEUE_KEY = QueueKeys.QUEUE_KEY_HEARTBEAT_MESSAGE;

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
            byte[] bytes = ObjectSerializeUtils.serializeToBytes(this.heartbeat);
            jedis = JedisPoolUtils.getJedis();
            jedis.lpush(QUEUE_KEY.getBytes(), bytes);

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {

            JedisPoolUtils.cleanJedis(jedis);
        }

        return heartbeat;
    }


    @Override
    public Message popMessage() {

        try {
            jedis = JedisPoolUtils.getJedis();

            byte[] bytes = jedis.rpop(QueueKeys.QUEUE_KEY_HEARTBEAT_MESSAGE.getBytes());

            if (bytes == null)
                return null;

            heartbeat = (HeartbeatMsgModel) ObjectSerializeUtils.getEntityFromBytes(bytes);

            return heartbeat;

        } catch (Exception ex) {

            ex.printStackTrace();

        } finally {

            JedisPoolUtils.cleanJedis(jedis);
        }

        return null;
    }
}
