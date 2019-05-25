package com.huazie.frame.cache.redis.impl;

import com.huazie.frame.cache.AbstractFleaCache;
import com.huazie.frame.cache.common.CacheEnum;
import com.huazie.frame.cache.redis.RedisClient;
import com.huazie.frame.common.CommonConstants;
import com.huazie.frame.common.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p> 自定义Redis缓存类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class RedisFleaCache extends AbstractFleaCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisFleaCache.class);

    private RedisClient redisClient; // Redis客户端类

    public RedisFleaCache(String name, long expiry) {
        super(name, expiry);
        cache = CacheEnum.Redis;
        redisClient = RedisClientProxy.getProxyInstance();
    }

    @Override
    protected Object getNativeValue(String key) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RedisFleaCache##getNativeValue(String) KEY={}", key);
        }
        // 反序列化
        return ObjectUtils.deserialize(redisClient.get(key.getBytes()));
    }

    @Override
    protected void putNativeValue(String key, Object value, long expiry) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RedisFleaCache##putNativeValue(String, Object, long) KEY={}", key);
            LOGGER.debug("RedisFleaCache##putNativeValue(String, Object, long) VALUE={}", value);
            LOGGER.debug("RedisFleaCache##putNativeValue(String, Object, long) EXPIRY={}s", expiry);
        }
        // 序列化
        if (ObjectUtils.isNotEmpty(value)) {
            byte[] valueBytes = ObjectUtils.serialize(value);
            if (expiry == CommonConstants.NumeralConstants.ZERO) {
                redisClient.set(key.getBytes(), valueBytes);
            } else {
                redisClient.set(key.getBytes(), valueBytes, (int) expiry);
            }
        }

    }

    @Override
    protected void deleteNativeValue(String key) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RedisFleaCache##deleteNativeValue(String) KEY=" + key);
        }
        redisClient.del(key);
    }

}
