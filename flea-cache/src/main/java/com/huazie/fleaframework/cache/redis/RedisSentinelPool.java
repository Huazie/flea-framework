package com.huazie.fleaframework.cache.redis;

import com.huazie.fleaframework.cache.common.CacheConfigUtils;
import com.huazie.fleaframework.cache.common.CacheConstants.RedisConfigConstants;
import com.huazie.fleaframework.cache.common.CacheUtils;
import com.huazie.fleaframework.cache.config.CacheParam;
import com.huazie.fleaframework.cache.config.CacheServer;
import com.huazie.fleaframework.cache.exceptions.FleaCacheConfigException;
import com.huazie.fleaframework.cache.redis.config.RedisSentinelConfig;
import com.huazie.fleaframework.common.CommonConstants;
import com.huazie.fleaframework.common.util.CollectionUtils;
import com.huazie.fleaframework.common.util.ExceptionUtils;
import com.huazie.fleaframework.common.util.ObjectUtils;
import com.huazie.fleaframework.common.util.StringUtils;
import redis.clients.jedis.*;
import redis.clients.jedis.util.Hashing;
import redis.clients.jedis.util.Sharded;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class RedisSentinelPool {

    private static final ConcurrentMap<String, RedisSentinelPool> redisPools = new ConcurrentHashMap<>();

    private String poolName; // 连接池名

    private JedisSentinelPool jedisSentinelPool; // 分布式Jedis连接池

    private RedisSentinelPool(String poolName) {
        this.poolName = poolName;
    }

    /**
     * 获取Redis连接池实例 (默认连接池)
     *
     * @return Redis连接池实例对象
     * @since 1.0.0
     */
    public static RedisSentinelPool getInstance() {
        return getInstance(CommonConstants.FleaPoolConstants.DEFAULT_POOL_NAME);
    }

    /**
     * 获取Redis连接池实例 (指定连接池名)
     *
     * @param poolName 连接池名
     * @return Redis连接池实例对象
     * @since 1.0.0
     */
    public static RedisSentinelPool getInstance(String poolName) {
        if (!redisPools.containsKey(poolName)) {
            synchronized (redisPools) {
                if (!redisPools.containsKey(poolName)) {
                    RedisSentinelPool redisSentinelPool = new RedisSentinelPool(poolName);
                    redisPools.putIfAbsent(poolName, redisSentinelPool);
                }
            }
        }
        return redisPools.get(poolName);
    }

    /**
     * 默认初始化
     *
     * @since 1.0.0
     */
    public void initialize() {
        if (!CommonConstants.FleaPoolConstants.DEFAULT_POOL_NAME.equals(poolName)) {
            ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "采用默认初始化，请使用RedisSentinelPool##getInstance()");
        }
        RedisSentinelConfig redisSentinelConfig = RedisSentinelConfig.getConfig();

        String masterName = redisSentinelConfig.getMasterName();

        Set<String> sentinels = redisSentinelConfig.getSentinels();

        int connectionTimeout = redisSentinelConfig.getConnectionTimeout();

        int soTimeout = redisSentinelConfig.getSoTimeout();

        String password = redisSentinelConfig.getPassword();

        int dataBase = redisSentinelConfig.getDataBase();

        String clientName = redisSentinelConfig.getClientName();

        JedisPoolConfig poolConfig = redisSentinelConfig.getJedisPoolConfig();

        if (ObjectUtils.isEmpty(jedisSentinelPool)) {
            jedisSentinelPool = new JedisSentinelPool(masterName, sentinels, poolConfig, connectionTimeout, soTimeout, password, dataBase, clientName);
        }
    }

    /**
     * 初始化 (非默认连接池)
     *
     * @param cacheServerList 缓存服务器集
     * @since 1.0.0
     */
    public void initialize(List<CacheServer> cacheServerList) {
        if (CommonConstants.FleaPoolConstants.DEFAULT_POOL_NAME.equals(poolName)) {
            ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "采用指定连接池名初始化，请使用RedisSentinelPool##getInstance(String poolName)");
        }

        if (CollectionUtils.isEmpty(cacheServerList)) {
            return;
        }

        CacheParam masternameParam = CacheConfigUtils.getCacheParam(RedisConfigConstants.REDIS_SENTINEL_CONFIG_MASTERNAME);
        if (ObjectUtils.isEmpty(masternameParam) || StringUtils.isBlank(masternameParam.getValue())) {
            ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "请检查flea-cache-config.xml配置，【<cache-param key=" + RedisConfigConstants.REDIS_SENTINEL_CONFIG_MASTERNAME + " ></cache-param>】未配置或配置值为空");
        }
        String masterName = masternameParam.getValue();

        // 1. 获取Redis sb服务节点Set集合
        Set<String> nodes = new HashSet<>();
        // 遍历缓存服务器集
        for (CacheServer cacheServer : cacheServerList) {
            if (ObjectUtils.isNotEmpty(cacheServer)) {
                String server = cacheServer.getServer();
                if (StringUtils.isBlank(server)) {
                    ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "请检查flea-cache-config.xml配置,【<cache-server group=" + poolName + " ></cache-server>】未配置缓存服务器");
                }
                nodes.add(server);
            }
        }

        // 2. 获取Redis sb客户端socket连接超时时间（单位：ms）
        CacheParam connectionTimeoutParam = CacheConfigUtils.getCacheParam(RedisConfigConstants.REDIS_SENTINEL_CONFIG_CONNECTIONTIMEOUT);
        if (ObjectUtils.isEmpty(connectionTimeoutParam) || StringUtils.isBlank(connectionTimeoutParam.getValue())) {
            ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "请检查flea-cache-config.xml配置，【<cache-param key=" + RedisConfigConstants.REDIS_SENTINEL_CONFIG_CONNECTIONTIMEOUT + " ></cache-param>】未配置或配置值为空");
        }
        int connectionTimeout = Integer.parseInt(connectionTimeoutParam.getValue());

        // 3. 获取Redis sb客户端socket读写超时时间（单位：ms）
        CacheParam soTimeoutParam = CacheConfigUtils.getCacheParam(RedisConfigConstants.REDIS_SENTINEL_CONFIG_SOTIMEOUT);
        if (ObjectUtils.isEmpty(soTimeoutParam) || StringUtils.isBlank(soTimeoutParam.getValue())) {
            ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "请检查flea-cache-config.xml配置，【<cache-param key=" + RedisConfigConstants.REDIS_SENTINEL_CONFIG_SOTIMEOUT + " ></cache-param>】未配置或配置值为空");
        }
        int soTimeout = Integer.parseInt(soTimeoutParam.getValue());

        // 4. 获取Redis客户端操作最大尝试次数【包含第一次操作】
        int maxAttempts = CacheConfigUtils.getMaxAttempts();

        // 5. 获取Redis sb服务节点登录密码（集群各节点配置同一个）
        CacheParam passwordParam = CacheConfigUtils.getCacheParam(RedisConfigConstants.REDIS_SENTINEL_CONFIG_PASSWORD);
        if (ObjectUtils.isEmpty(passwordParam) || StringUtils.isBlank(passwordParam.getValue())) {
            ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "请检查flea-cache-config.xml配置，【<cache-param key=" + RedisConfigConstants.REDIS_SENTINEL_CONFIG_PASSWORD + " ></cache-param>】未配置或配置值为空");
        }
        String password = passwordParam.getValue();

        // 6. 获取Redis sb客户端当前连接的名称
        String clientName = CacheConfigUtils.getClientName(CacheConfigUtils.getSystemName());
        // 7. 获取Jedis连接池配置信息
        JedisPoolConfig poolConfig = CacheConfigUtils.getJedisPoolConfig();
        //数据库索引
        int database = Protocol.DEFAULT_DATABASE;
        CacheParam dataBaseParam = CacheConfigUtils.getCacheParam(RedisConfigConstants.REDIS_SENTINEL_CONFIG_DATABASE);
        if (!(ObjectUtils.isEmpty(dataBaseParam) || StringUtils.isBlank(dataBaseParam.getValue()))) {
            database = Integer.parseInt(dataBaseParam.getValue());
            if (database < 0 || database > 15) {
                ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "范围不对，应为0-15【" + RedisConfigConstants.REDIS_SENTINEL_CONFIG_DATABASE + "】");
            }
        }

        if (ObjectUtils.isEmpty(jedisSentinelPool)) {
            jedisSentinelPool = new JedisSentinelPool(masterName, nodes, poolConfig, connectionTimeout, soTimeout, password, database, clientName);
        }
    }

    /**
     * 获取当前连接池名
     *
     * @return 连接池名
     * @since 1.0.0
     */
    public String getPoolName() {
        return poolName;
    }

    /**
     * 分布式Jedis连接池
     *
     * @return 分布式Jedis连接池
     * @since 1.0.0
     */
    public JedisSentinelPool getJedisSentinelPool() {
        if (ObjectUtils.isEmpty(jedisSentinelPool)) {
            ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "获取分布式Jedis连接池失败：请先调用initialize初始化");
        }
        return jedisSentinelPool;
    }

}
