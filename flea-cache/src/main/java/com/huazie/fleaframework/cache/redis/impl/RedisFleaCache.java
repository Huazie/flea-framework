package com.huazie.fleaframework.cache.redis.impl;

import com.huazie.fleaframework.cache.AbstractFleaCache;
import com.huazie.fleaframework.cache.NullCache;
import com.huazie.fleaframework.cache.common.CacheEnum;
import com.huazie.fleaframework.cache.common.CacheModeEnum;
import com.huazie.fleaframework.cache.common.CacheUtils;
import com.huazie.fleaframework.cache.redis.RedisClient;
import com.huazie.fleaframework.cache.redis.config.RedisClusterConfig;
import com.huazie.fleaframework.cache.redis.config.RedisSentinelConfig;
import com.huazie.fleaframework.cache.redis.config.RedisShardedConfig;
import com.huazie.fleaframework.common.CommonConstants;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.ObjectUtils;

/**
 * Redis Flea缓存类，实现了以Flea框架操作Redis缓存的基本操作方法。
 *
 * <p> 在上述基本操作方法中，实际使用Redis客户端【{@code redisClient}】
 * 读、写和删除Redis缓存。其中写缓存方法【{@code putNativeValue}】在
 * 添加的数据值为【{@code null}】时，默认添加空缓存数据【{@code NullCache}】
 * 到Redis中，有效期取初始化参数【{@code nullCacheExpiry}】。
 *
 * <p> 单个缓存接入场景，有效期配置可查看【redis.properties】中的配置参数
 * 【redis.nullCacheExpiry】
 *
 * <p> 整合缓存接入场景，有效期配置可查看【flea-cache-config.xml】中的缓存参数
 * 【{@code <cache-param key="fleacore.nullCacheExpiry"
 * desc="空缓存数据有效期（单位：s）">300</cache-param>}】
 *
 * @author huazie
 * @version 1.1.0
 * @since 1.0.0
 */
public class RedisFleaCache extends AbstractFleaCache {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(RedisFleaCache.class);

    private RedisClient redisClient; // Redis客户端

    private CacheModeEnum cacheMode; // 缓存模式【分片模式和集群模式\哨兵模式】

    /**
     * 带参数的构造方法，初始化Redis Flea缓存类
     *
     * @param name            缓存数据主关键字
     * @param expiry          缓存数据有效期（单位：s）
     * @param nullCacheExpiry 空缓存数据有效期（单位：s）
     * @param cacheMode       缓存模式【分分片模式和集群模式】
     * @param redisClient     Redis客户端
     * @since 1.0.0
     */
    public RedisFleaCache(String name, int expiry, int nullCacheExpiry, CacheModeEnum cacheMode, RedisClient redisClient) {
        super(name, expiry, nullCacheExpiry);
        this.cacheMode = cacheMode;
        this.redisClient = redisClient;
        if (CacheUtils.isClusterMode(cacheMode))
            cache = CacheEnum.RedisCluster; // 缓存实现之Redis集群模式
        else if (CacheUtils.isSentinelMode(cacheMode))
            cache = CacheEnum.RedisSentinel;  // 缓存实现之Redis哨兵模式
        else
            cache = CacheEnum.RedisSharded; // 缓存实现之Redis分片模式
    }

    @Override
    public Object getNativeValue(String key) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug1(new Object() {}, "KEY = {}", key);
        }
        return redisClient.get(key);
    }

    @Override
    public Object putNativeValue(String key, Object value, int expiry) {
        if (LOGGER.isDebugEnabled()) {
            Object obj = new Object() {};
            LOGGER.debug1(obj, "REDIS FLEA CACHE, KEY = {}", key);
            LOGGER.debug1(obj, "REDIS FLEA CACHE, VALUE = {}", value);
            LOGGER.debug1(obj, "REDIS FLEA CACHE, EXPIRY = {}s", expiry);
            LOGGER.debug1(obj, "REDIS FLEA CACHE, NULL CACHE EXPIRY = {}s", getNullCacheExpiry());
        }
        if (ObjectUtils.isEmpty(value)) {
            return redisClient.set(key, new NullCache(key), getNullCacheExpiry());
        } else {
            if (expiry == CommonConstants.NumeralConstants.INT_ZERO) {
                return redisClient.set(key, value);
            } else {
                return redisClient.set(key, value, expiry);
            }
        }
    }

    @Override
    public Object deleteNativeValue(String key) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug1(new Object() {}, "KEY = {}", key);
        }
        return redisClient.del(key);
    }

    @Override
    public String getSystemName() {
        if (CacheUtils.isClusterMode(cacheMode))
            // 集群模式下获取缓存归属系统名
            return RedisClusterConfig.getConfig().getSystemName();
        else if (CacheUtils.isSentinelMode(cacheMode))
            // 哨兵模式下获取缓存归属系统名
            return RedisSentinelConfig.getConfig().getSystemName();
            // 分片模式下获取缓存归属系统名
            return RedisShardedConfig.getConfig().getSystemName();
    }
}
