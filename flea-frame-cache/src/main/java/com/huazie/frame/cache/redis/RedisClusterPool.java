package com.huazie.frame.cache.redis;

import com.huazie.frame.cache.common.CacheConfigUtils;
import com.huazie.frame.cache.common.CacheConstants.RedisConfigConstants;
import com.huazie.frame.cache.common.CacheUtils;
import com.huazie.frame.cache.config.CacheParam;
import com.huazie.frame.cache.config.CacheServer;
import com.huazie.frame.cache.exceptions.FleaCacheConfigException;
import com.huazie.frame.cache.redis.config.RedisClusterConfig;
import com.huazie.frame.common.CommonConstants;
import com.huazie.frame.common.util.CollectionUtils;
import com.huazie.frame.common.util.ObjectUtils;
import com.huazie.frame.common.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Redis集群连接池，用于初始化Jedis集群实例。
 *
 * <p> 针对单独缓存接入场景，采用默认连接池初始化的方式；<br/>
 * 可参考如下：
 * <pre>
 *   // 初始化默认连接池
 *   RedisClusterPool.getInstance().initialize(); </pre>
 *
 * <p> 针对整合缓存接入场景，采用指定连接池初始化的方式；<br/>
 * 可参考如下：
 * <pre>
 *   // 初始化指定连接池
 *   RedisClusterPool.getInstance(group).initialize(cacheServerList); </pre>
 *
 * @author huazie
 * @version 1.1.0
 * @since 1.1.0
 */
public class RedisClusterPool {

    private static final ConcurrentMap<String, RedisClusterPool> redisClusterPools = new ConcurrentHashMap<>();

    private String poolName; // 连接池名

    private JedisCluster jedisCluster; // Jedis集群实例

    private RedisClusterPool(String poolName) {
        this.poolName = poolName;
    }

    /**
     * <p> 获取Redis集群连接池实例 (默认连接池) </p>
     *
     * @return Redis集群连接池实例对象
     * @since 1.1.0
     */
    public static RedisClusterPool getInstance() {
        return getInstance(CommonConstants.FleaPoolConstants.DEFAULT_POOL_NAME);
    }

    /**
     * <p> 获取Redis集群连接池实例 (指定连接池名)</p>
     *
     * @param poolName 连接池名
     * @return Redis集群连接池实例
     * @since 1.1.0
     */
    public static RedisClusterPool getInstance(String poolName) {
        if (!redisClusterPools.containsKey(poolName)) {
            synchronized (redisClusterPools) {
                if (!redisClusterPools.containsKey(poolName)) {
                    RedisClusterPool redisClusterPool = new RedisClusterPool(poolName);
                    redisClusterPools.putIfAbsent(poolName, redisClusterPool);
                }
            }
        }
        return redisClusterPools.get(poolName);
    }

    /**
     * <p> 默认初始化 </p>
     *
     * @since 1.1.0
     */
    void initialize() {
        if (!CommonConstants.FleaPoolConstants.DEFAULT_POOL_NAME.equals(poolName)) {
            throw new FleaCacheConfigException("采用默认初始化，请使用RedisClusterPool##getInstance()");
        }

        RedisClusterConfig redisClusterConfig = RedisClusterConfig.getConfig();

        // 1. 获取Redis集群服务节点Set集合
        Set<HostAndPort> nodes = redisClusterConfig.getNodes();
        // 2. 获取Redis集群客户端socket连接超时时间（单位：ms）
        int connectionTimeout = redisClusterConfig.getConnectionTimeout();
        // 3. 获取Redis集群客户端socket读写超时时间（单位：ms）
        int soTimeout = redisClusterConfig.getSoTimeout();
        // 4. 获取Redis客户端操作最大尝试次数【包含第一次操作】
        int maxAttempts = redisClusterConfig.getMaxAttempts();
        // 5. 获取Redis集群服务节点登录密码（集群配置同一个）
        String password = redisClusterConfig.getPassword();
        // 6. 获取Redis集群客户端当前连接的名称
        String clientName = redisClusterConfig.getClientName();
        // 7. 获取Jedis连接池配置信息
        JedisPoolConfig poolConfig = redisClusterConfig.getJedisPoolConfig();

        if (ObjectUtils.isEmpty(jedisCluster)) {
            jedisCluster = new JedisCluster(nodes, connectionTimeout, soTimeout, maxAttempts, password, clientName, poolConfig);
        }
    }

    /**
     * <p> 初始化 (非默认连接池) </p>
     *
     * @param cacheServerList 缓存服务器集
     * @since 1.1.0
     */
    void initialize(List<CacheServer> cacheServerList) {
        if (CommonConstants.FleaPoolConstants.DEFAULT_POOL_NAME.equals(poolName)) {
            throw new FleaCacheConfigException("采用指定连接池名初始化，请使用RedisClusterPool##getInstance(String poolName)");
        }

        if (CollectionUtils.isEmpty(cacheServerList)) {
            return;
        }

        // 1. 获取Redis集群服务节点Set集合
        Set<HostAndPort> nodes = new HashSet<>();
        // 遍历缓存服务器集
        for (CacheServer cacheServer : cacheServerList) {
            if (ObjectUtils.isNotEmpty(cacheServer)) {
                String server = cacheServer.getServer();
                if (StringUtils.isBlank(server)) {
                    throw new FleaCacheConfigException("请检查flea-cache-config.xml配置,【<cache-server group=" + poolName + " ></cache-server>】未配置缓存服务器");
                }
                nodes.add(CacheUtils.parseString(server));
            }
        }

        // 2. 获取Redis集群客户端socket连接超时时间（单位：ms）
        CacheParam connectionTimeoutParam = CacheConfigUtils.getCacheParam(RedisConfigConstants.REDIS_CLUSTER_CONFIG_CONNECTIONTIMEOUT);
        if (ObjectUtils.isEmpty(connectionTimeoutParam) || StringUtils.isBlank(connectionTimeoutParam.getValue())) {
            throw new FleaCacheConfigException("请检查flea-cache-config.xml配置，【<cache-param key=" + RedisConfigConstants.REDIS_CLUSTER_CONFIG_CONNECTIONTIMEOUT + " ></cache-param>】未配置或配置值为空");
        }
        int connectionTimeout = Integer.parseInt(connectionTimeoutParam.getValue());

        // 3. 获取Redis集群客户端socket读写超时时间（单位：ms）
        CacheParam soTimeoutParam = CacheConfigUtils.getCacheParam(RedisConfigConstants.REDIS_CLUSTER_CONFIG_SOTIMEOUT);
        if (ObjectUtils.isEmpty(soTimeoutParam) || StringUtils.isBlank(soTimeoutParam.getValue())) {
            throw new FleaCacheConfigException("请检查flea-cache-config.xml配置，【<cache-param key=" + RedisConfigConstants.REDIS_CLUSTER_CONFIG_SOTIMEOUT + " ></cache-param>】未配置或配置值为空");
        }
        int soTimeout = Integer.parseInt(soTimeoutParam.getValue());

        // 4. 获取Redis客户端操作最大尝试次数【包含第一次操作】
        int maxAttempts = CacheConfigUtils.getMaxAttempts();

        // 5. 获取Redis集群服务节点登录密码（集群各节点配置同一个）
        CacheParam passwordParam = CacheConfigUtils.getCacheParam(RedisConfigConstants.REDIS_CLUSTER_CONFIG_PASSWORD);
        if (ObjectUtils.isEmpty(passwordParam) || StringUtils.isBlank(passwordParam.getValue())) {
            throw new FleaCacheConfigException("请检查flea-cache-config.xml配置，【<cache-param key=" + RedisConfigConstants.REDIS_CLUSTER_CONFIG_PASSWORD + " ></cache-param>】未配置或配置值为空");
        }
        String password = passwordParam.getValue();

        // 6. 获取Redis集群客户端当前连接的名称
        String clientName = CacheConfigUtils.getClientName(CacheConfigUtils.getSystemName());
        // 7. 获取Jedis连接池配置信息
        JedisPoolConfig poolConfig = CacheConfigUtils.getJedisPoolConfig();

        if (ObjectUtils.isEmpty(jedisCluster)) {
            jedisCluster = new JedisCluster(nodes, connectionTimeout, soTimeout, maxAttempts, password, clientName, poolConfig);
        }
    }

    /**
     * <p> 获取当前连接池名 </p>
     *
     * @return 连接池名
     * @since 1.1.0
     */
    public String getPoolName() {
        return poolName;
    }

    /**
     * <p> 获取Jedis集群实例对象 </p>
     *
     * @return Jedis集群实例对象
     * @since 1.1.0
     */
    public JedisCluster getJedisCluster() {
        if (ObjectUtils.isEmpty(jedisCluster)) {
            throw new FleaCacheConfigException("获取Jedis集群实例对象失败：请先调用initialize初始化");
        }
        return jedisCluster;
    }
}
