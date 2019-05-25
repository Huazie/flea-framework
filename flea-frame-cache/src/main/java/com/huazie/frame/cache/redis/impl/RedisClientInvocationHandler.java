package com.huazie.frame.cache.redis.impl;

import com.huazie.frame.cache.redis.RedisClient;
import com.huazie.frame.common.proxy.FleaInvocationHandler;
import com.huazie.frame.common.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.lang.reflect.Method;

/**
 * <p> Redis客户端调用处理类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class RedisClientInvocationHandler extends FleaInvocationHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisClientInvocationHandler.class);

    public RedisClientInvocationHandler(Object proxyObject) {
        super(proxyObject);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (ObjectUtils.isEmpty(proxyObject)) {
            throw new Exception("The proxyObject must be initialized");
        }

        if (!(proxyObject instanceof RedisClient)) {
            throw new Exception("The proxyObject must implement RedisClient interface");
        }

        RedisClient redisClient = (RedisClient) proxyObject;
        ShardedJedisPool jedisPool = redisClient.getJedisPool();
        if (ObjectUtils.isNotEmpty(jedisPool)) {
            redisClient.setShardedJedis(jedisPool.getResource());
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("RedisClientInvocationHandler#invoke(Object, Method, Object[]) Get ShardedJedis = {}", redisClient.getShardedJedis());
            }
        }
        try {
            return super.invoke(proxy, method, args);
        } finally {
            ShardedJedis shardedJedis = redisClient.getShardedJedis();
            if (ObjectUtils.isNotEmpty(shardedJedis)) {
                // 使用后，关闭连接
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("RedisClientInvocationHandler#invoke(Object, Method, Object[]) Close ShardedJedis");
                }
                shardedJedis.close();
            }
        }
    }
}
