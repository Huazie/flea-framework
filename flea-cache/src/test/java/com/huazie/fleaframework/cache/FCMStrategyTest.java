package com.huazie.fleaframework.cache;

import com.huazie.fleaframework.cache.common.CacheEnum;
import com.huazie.fleaframework.cache.common.FCMStrategyContext;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.strategy.FleaStrategyFacade;
import org.junit.Test;

/**
 * Flea缓存管理者策略单测
 *
 * @author huazie
 * @version 1.1.0
 * @since 1.1.0
 */
public class FCMStrategyTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FCMStrategyTest.class);

    @Test
    public void testFCMStrategy() {
        String name = CacheEnum.RedisSentinel.getName();
        FCMStrategyContext context = new FCMStrategyContext();
        AbstractFleaCacheManager manager = FleaStrategyFacade.invoke(name, context);
        LOGGER.debug("CoreFleaCacheManager = {}", manager);

//        String name1 = CacheEnum.MemCached.getName();
//        FCMStrategyContext context1 = new FCMStrategyContext();
//        AbstractFleaCacheManager manager1 = FleaStrategyFacade.invoke(name1, context1);
//        LOGGER.debug("MemCachedFleaCacheManager = {}", manager1);
//
//        String name2 = CacheEnum.RedisSharded.getName();
//        FCMStrategyContext context2 = new FCMStrategyContext();
//        AbstractFleaCacheManager manager2 = FleaStrategyFacade.invoke(name2, context2);
//        LOGGER.debug("RedisShardedFleaCacheManager = {}", manager2);
//
//        String name3 = CacheEnum.RedisCluster.getName();
//        FCMStrategyContext contex3 = new FCMStrategyContext();
//        AbstractFleaCacheManager manager3 = FleaStrategyFacade.invoke(name3, contex3);
//        LOGGER.debug("RedisClusterFleaCacheManager = {}", manager3);
    }

    @Test
    public void testUnknownFCMStrategy() {
        String name4 = "unknown";
        AbstractFleaCacheManager manager4 = FleaStrategyFacade.invoke(name4, new FCMStrategyContext());
        LOGGER.debug("FleaCacheManager = {}", manager4);
    }
}
