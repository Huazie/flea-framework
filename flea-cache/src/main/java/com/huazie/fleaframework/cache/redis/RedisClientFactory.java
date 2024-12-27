package com.huazie.fleaframework.cache.redis;

import com.huazie.fleaframework.cache.common.CacheModeEnum;
import com.huazie.fleaframework.cache.common.RedisClientStrategyContext;
import com.huazie.fleaframework.common.CommonConstants;
import com.huazie.fleaframework.common.strategy.FleaStrategyFacade;
import com.huazie.fleaframework.common.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Redis客户端工厂，用于获取Redis客户端。
 *
 * <p> 它有四种方式获取Redis客户端：<br/>
 * 一是获取分片模式下默认连接池的Redis客户端，应用在单个缓存接入场景；<br/>
 * 二是获取指定模式下默认连接池的Redis客户端，应用在单个缓存接入场景；<br/>
 * 三是获取分片模式下指定连接池的Redis客户端，应用在整合缓存接入场景；<br/>
 * 四是获取指定模式下指定连接池的Redis客户端，应用在整合缓存接入场景。
 *
 * <p> 同步集合类【{@code redisClients}】，存储的键为连接池名，值为Redis客户端。
 *
 * <p> 针对单个缓存接入场景，存储的键为【default_缓存模式】；<br/>
 * 例如：
 * <pre>
 *  default_0 ：表示分片模式下默认连接池的Redis客户端
 *  default_1 ：表示集群模式下默认连接池的Redis客户端
 * </pre>
 *
 * <p> 针对整合缓存接入场景，存储的键为【缓存组名_缓存模式】。
 * 例如【group代指缓存组名】：
 * <pre>
 *  group_0 ：表示分片模式下指定连接池为group的Redis客户端
 *  group_1 ：表示集群模式下指定连接池为group的Redis客户端
 * </pre>
 *
 * @author huazie
 * @version 1.1.0
 * @since 1.0.0
 */
public class RedisClientFactory {

    private static final ConcurrentMap<String, RedisClient> redisClients = new ConcurrentHashMap<>();

    private RedisClientFactory() {
    }

    /**
     * 获取分片模式下默认连接池的Redis客户端
     *
     * @return 分片模式的Redis客户端
     * @since 1.0.0
     */
    public static RedisClient getInstance() {
        return getInstance(CommonConstants.FleaPoolConstants.DEFAULT_POOL_NAME);
    }

    /**
     * 获取指定模式下默认连接池的Redis客户端
     *
     * @param mode 缓存模式
     * @return 指定模式的Redis客户端
     * @since 1.1.0
     */
    public static RedisClient getInstance(CacheModeEnum mode) {
        return getInstance(CommonConstants.FleaPoolConstants.DEFAULT_POOL_NAME, mode);
    }

    /**
     * 获取分片模式下指定连接池的Redis客户端
     *
     * @param poolName 连接池名
     * @return 分片模式的Redis客户端
     * @since 1.0.0
     */
    public static RedisClient getInstance(String poolName) {
        return getInstance(poolName, CacheModeEnum.SHARDED);
    }

    /**
     * 获取指定模式下指定连接池的Redis客户端
     *
     * @param poolName 连接池名
     * @param mode     缓存模式
     * @return 指定模式的Redis客户端
     * @since 1.1.0
     */
    public static RedisClient getInstance(String poolName, CacheModeEnum mode) {
        String key = StringUtils.strCat(poolName, CommonConstants.SymbolConstants.UNDERLINE, StringUtils.valueOf(mode.getMode()));
        if (!redisClients.containsKey(key)) {
            synchronized (redisClients) {
                if (!redisClients.containsKey(key)) {
                    RedisClientStrategyContext context = new RedisClientStrategyContext(poolName);
                    redisClients.putIfAbsent(key, FleaStrategyFacade.invoke(mode.name(), context));
                }
            }
        }
        return redisClients.get(key);
    }

}
