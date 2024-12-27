package com.huazie.fleaframework.cache.common;

import com.huazie.fleaframework.cache.AbstractFleaCacheManager;
import com.huazie.fleaframework.cache.core.strategy.CoreFCMStrategy;
import com.huazie.fleaframework.cache.memcached.strategy.MemCachedFCMStrategy;
import com.huazie.fleaframework.cache.redis.strategy.RedisClusterFCMStrategy;
import com.huazie.fleaframework.cache.redis.strategy.RedisShardedFCMStrategy;
import com.huazie.fleaframework.common.FleaCommonConfig;
import com.huazie.fleaframework.common.strategy.FleaStrategyContext;
import com.huazie.fleaframework.common.strategy.IFleaStrategy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Flea缓存管理者策略上下文
 *
 * @author huazie
 * @version 1.1.0
 * @since 1.1.0
 */
public class FCMStrategyContext extends FleaStrategyContext<AbstractFleaCacheManager, FleaCommonConfig> {

    private static Map<String, IFleaStrategy<AbstractFleaCacheManager, FleaCommonConfig>> fleaStrategyMap;

    static {
        fleaStrategyMap = new HashMap<>();
        fleaStrategyMap.put(CacheEnum.MemCached.getName(), new MemCachedFCMStrategy());
        fleaStrategyMap.put(CacheEnum.RedisSharded.getName(), new RedisShardedFCMStrategy());
        fleaStrategyMap.put(CacheEnum.RedisCluster.getName(), new RedisClusterFCMStrategy());
        fleaStrategyMap.put(CacheEnum.FleaCore.getName(), new CoreFCMStrategy());
        fleaStrategyMap.put(CacheEnum.RedisSentinel.getName(), new CoreFCMStrategy());
        fleaStrategyMap = Collections.unmodifiableMap(fleaStrategyMap);
    }

    @Override
    public Map<String, IFleaStrategy<AbstractFleaCacheManager, FleaCommonConfig>> init() {
        return fleaStrategyMap;
    }
}
