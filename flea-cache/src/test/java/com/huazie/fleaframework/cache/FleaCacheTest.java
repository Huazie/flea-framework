package com.huazie.fleaframework.cache;

import com.huazie.fleaframework.cache.common.CacheEnum;
import com.huazie.fleaframework.cache.common.FleaCacheManagerFactory;
import com.huazie.fleaframework.cache.memcached.config.MemCachedConfig;
import com.huazie.fleaframework.cache.redis.config.RedisClusterConfig;
import com.huazie.fleaframework.cache.redis.config.RedisSentinelConfig;
import com.huazie.fleaframework.cache.redis.config.RedisShardedConfig;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import org.junit.Test;
import redis.clients.jedis.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Flea 缓存单测类
 *
 * @author huazie
 * @version 1.0.0
 */
public class FleaCacheTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaCacheTest.class);

    @Test
    public void testProperties() {
//        MemCachedConfig memCachedConfig = MemCachedConfig.getConfig();
//        LOGGER.debug("MemCachedConfig={}", memCachedConfig);
//        RedisShardedConfig redisShardedConfig = RedisShardedConfig.getConfig();
//        LOGGER.debug("RedisShardedConfig={}", redisShardedConfig);
//        RedisClusterConfig redisClusterConfig = RedisClusterConfig.getConfig();
//        LOGGER.debug("RedisClusterConfig={}", redisClusterConfig);
        RedisSentinelConfig sentinelConfig = RedisSentinelConfig.getConfig();
        LOGGER.debug("-------------------------------RedisSentinelConfig={}-------------------------------------------", sentinelConfig);

    }

    @Test
    public void testMemcachedExpire() {
        Date date = new Date(1000);
        long aa = date.getTime() / 1000;
        LOGGER.debug("huazie:{}", aa);
    }

    @Test
    public void testRedis() {
        Jedis jedis = new Jedis("127.0.0.1", 63793);
        //jedis.auth("huazie123");
        //查看服务是否运行
        LOGGER.debug("服务正在运行: {} ", jedis.ping());

        // #1. redis 字符串数据
        //jedis.set("huazie", "hello world");

        //#2. 获取redis字符串
        String msg = jedis.get("huazie");
        LOGGER.debug("value = {}", msg);

        // #3. 存储数据到列表中
        jedis.lpush("huazie-list", "Beijing");
        jedis.lpush("huazie-list", "Shanghai");
        jedis.lpush("huazie-list", "Hangzhou");

//        // #4. 获取列表数据输出
        List<String> list = jedis.lrange("huazie-list", 0, 2);
        LOGGER.debug("list = {}", list);

        // #5. 获取数据并输出
        Set<String> keys = jedis.keys("*");
        Iterator<String> it = keys.iterator();
        int i = 1;
        while (it.hasNext()) {
            String key = it.next();
            LOGGER.debug("key{} = {}", i++, key);
        }
//
//        // #6. 删除数据
        LOGGER.debug("del ret = {}", jedis.del("huazie-list"));

    }

    @Test
    public void testSentinel() {

          JedisSentinelPool pool = null;
        // 集群的服务节点Set集合
        Set<String> nodes = new HashSet<>();
        nodes.add("127.0.0.1:36379");
        nodes.add("127.0.0.1:36380");
        nodes.add("127.0.0.1:36381");
//        nodes.add(new HostAndPort("127.0.0.1", 20022));
//        nodes.add(new HostAndPort("127.0.0.1", 20031));
//        nodes.add(new HostAndPort("127.0.0.1", 20032));

        // Jedis连接池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲连接数, 默认8个
        //jedisPoolConfig.setMaxIdle(100);
        // 最大连接数, 默认8个
        jedisPoolConfig.setMaxTotal(10);
        //最小空闲连接数, 默认0
        //jedisPoolConfig.setMinIdle(0);
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        //jedisPoolConfig.setMaxWaitMillis(2000); // 设置2秒
        //对拿到的connection进行validateObject校验
        //jedisPoolConfig.setTestOnBorrow(true);

        int connectionTimeout = 2000;
        int soTimeout = 2000;
        int maxAttempts = 5;
        String password = null;
        String clientName = "FleaFrame";
        String masterName = "mymaster";
        //JedisCluster jedis = new JedisCluster(nodes, connectionTimeout, soTimeout, maxAttempts, password, clientName, jedisPoolConfig);
        JedisSentinelPool jedisSentinelPool =
                new JedisSentinelPool(masterName, nodes,jedisPoolConfig);
        Jedis jedis = jedisSentinelPool.getResource();
        jedis.set("huazie", "hello world");
        jedis.close();
        LOGGER.debug("get huazie = {}", jedis.get("huazie"));
    }

    @Test
    public void testpool() {
        // 集群的服务节点Set集合
        Set<String> nodes = new HashSet<>();
        nodes.add("127.0.0.1:63791");
        nodes.add("127.0.0.1:63792");
        nodes.add("127.0.0.1:63793");
//        nodes.add(new HostAndPort("127.0.0.1", 20022));
//        nodes.add(new HostAndPort("127.0.0.1", 20031));
//        nodes.add(new HostAndPort("127.0.0.1", 20032));

        // Jedis连接池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲连接数, 默认8个
        //jedisPoolConfig.setMaxIdle(100);
        // 最大连接数, 默认8个
        jedisPoolConfig.setMaxTotal(10);
        //最小空闲连接数, 默认0
        //jedisPoolConfig.setMinIdle(0);
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        //jedisPoolConfig.setMaxWaitMillis(2000); // 设置2秒
        //对拿到的connection进行validateObject校验
        //jedisPoolConfig.setTestOnBorrow(true);

        int connectionTimeout = 2000;
        int soTimeout = 2000;
        int maxAttempts = 5;
        String password = null;
        String clientName = "FleaFrame";
        String masterName = "mymaster";
        //JedisCluster jedis = new JedisCluster(nodes, connectionTimeout, soTimeout, maxAttempts, password, clientName, jedisPoolConfig);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 63791);

        LOGGER.debug("jedisPool = {}", jedisPool);
    }

    @Test
    public void testMemeCachedFleaCache() {

        try {
            AbstractFleaCacheManager manager = FleaCacheManagerFactory.getFleaCacheManager(CacheEnum.MemCached.getName());
            AbstractFleaCache cache = manager.getCache("fleaconfigdata");
            LOGGER.debug("Cache={}", cache);
            //#### 1.  简单字符串
//            cache.put("menu1", "huazie");
//            cache.put("menu2", null);
//            cache.get("menu1");
//            cache.get("menu2");
//            cache.delete("menu1");
//            cache.delete("menu2");
//            cache.clear();
            cache.getCacheKey();
            LOGGER.debug(cache.getCacheName() + ">>>" + cache.getCacheDesc());
        } catch (Exception e) {
            LOGGER.error("Exception:", e);
        }
    }

    @Test
    public void testRedisShardedFleaCache() {
        try {
            // 分片模式下Flea缓存管理类
            AbstractFleaCacheManager manager = FleaCacheManagerFactory.getFleaCacheManager(CacheEnum.RedisSharded.getName());
            AbstractFleaCache cache = manager.getCache("fleaconfigdata");
            LOGGER.debug("Cache={}", cache);
            //#### 1.  简单字符串
            cache.put("menu1", "huazie");
            cache.put("menu2", null);
//            cache.get("menu1");
//            cache.get("menu2");
//            cache.delete("menu1");
//            cache.delete("menu2");
//            cache.clear();
            cache.getCacheKey();
            LOGGER.debug(cache.getCacheName() + ">>>" + cache.getCacheDesc());
        } catch (Exception e) {
            LOGGER.error("Exception:", e);
        }
    }

    @Test
    public void testCoreFleaCache() {
        try {
            AbstractFleaCacheManager manager = FleaCacheManagerFactory.getFleaCacheManager(CacheEnum.FleaCore.getName());
            AbstractFleaCache cache = manager.getCache("fleaconfigdata");
            LOGGER.debug("Cache={}", cache);
            //#### 1.  简单字符串
//            cache.put("menu1", "huazie");
//            cache.put("menu2", null);
            cache.get("menu1");
            cache.get("menu2");
//            cache.delete("menu1");
//            cache.delete("menu2");
//            cache.clear();
            cache.getCacheKey();
            LOGGER.debug(cache.getCacheName() + ">>>" + cache.getCacheDesc());
        } catch (Exception e) {
            LOGGER.error("Exception:", e);
        }
    }

    @Test
    public void testRedisClusterFleaCache() {
        try {
            // 集群模式下Flea缓存管理类
            AbstractFleaCacheManager manager = FleaCacheManagerFactory.getFleaCacheManager(CacheEnum.RedisCluster.getName());
            AbstractFleaCache cache = manager.getCache("fleamenufavorites");
            LOGGER.debug("Cache={}", cache);
            //#### 1.  简单字符串
//            cache.put("menu1", "huazie");
//            cache.put("menu2", null);
//            cache.get("menu1");
//            cache.get("menu2");
//            cache.delete("menu1");
//            cache.delete("menu2");
            cache.clear();
            cache.getCacheKey();
            LOGGER.debug(cache.getCacheName() + ">>>" + cache.getCacheDesc());
        } catch (Exception e) {
            LOGGER.error("Exception:", e);
        }
    }

    @Test
    public void testCoreFleaCacheForRedisCluster() {
        try {
            AbstractFleaCacheManager manager = FleaCacheManagerFactory.getFleaCacheManager(CacheEnum.FleaCore.getName());
            AbstractFleaCache cache = manager.getCache("fleamenufavorites");
            LOGGER.debug("Cache={}", cache);
            //#### 1.  简单字符串
//            cache.put("menu1", "huazie");
//            cache.put("menu2", null);
//            cache.get("menu1");
//            cache.get("menu2");
//            cache.delete("menu1");
//            cache.delete("menu2");
            cache.clear();
            cache.getCacheKey();
            LOGGER.debug(cache.getCacheName() + ">>>" + cache.getCacheDesc());
        } catch (Exception e) {
            LOGGER.error("Exception:", e);
        }
    }
}
