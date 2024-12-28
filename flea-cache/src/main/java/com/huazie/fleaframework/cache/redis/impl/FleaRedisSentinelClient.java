package com.huazie.fleaframework.cache.redis.impl;

import com.huazie.fleaframework.cache.redis.*;
import com.huazie.fleaframework.cache.redis.config.RedisSentinelConfig;
import com.huazie.fleaframework.common.CommonConstants;
import com.huazie.fleaframework.common.util.ObjectUtils;
import com.huazie.fleaframework.common.util.StringUtils;
import redis.clients.jedis.*;

import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.util.JedisClusterCRC16;
import redis.clients.jedis.util.SafeEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Flea哨兵模式Redis客户端实现，封装了Flea框架操作Redis缓存的基本操作。
 *
 * <p> 它内部具体操作Redis哨兵缓存的功能，由Jedis哨兵实例对象完成，
 * 包含读、写、删除Redis缓存的基本操作方法。
 *
 * <p> 哨兵模式下，单个缓存接入场景，可通过如下方式使用：
 * <pre>
 * RedisClient redisClient = new FleaRedisClusterClient.Builder().build();
 * // 执行读，写，删除等基本操作
 * redisClient.set("key", "value"); </pre>
 *
 * <p> 哨兵模式下，整合缓存接入场景，可通过如下方式使用：
 * <pre>
 * RedisClient redisClient = new FleaRedisClusterClient.Builder(poolName).build();
 * // 执行读，写，删除等基本操作
 * redisClient.set("key", "value"); </pre>
 *
 * <p> 当然每次都新建Redis客户端显然不可取，我们可通过Redis客户端工厂获取Redis客户端。
 * <p> 哨兵模式下，单个缓存接入场景，可通过如下方式使用：
 * <pre>
 * RedisClient redisClient = RedisClientFactory.getInstance(CacheModeEnum.CLUSTER);</pre>
 * <p> 哨兵模式下，整合缓存接入场景，可通过如下方式使用：
 * <pre>
 * RedisClient redisClient = RedisClientFactory.getInstance(poolName, CacheModeEnum.CLUSTER); </pre>
 *
 * @author huazie
 * @version 1.1.0
 * @since 1.1.0
 */
public class FleaRedisSentinelClient extends FleaRedisClient {

    private JedisSentinelPool jedisSentinelPool;

    private int maxAttempts;

    /**
     * Redis哨兵客户端构造方法 (默认)
     *
     * @since 1.1.0
     */
    private FleaRedisSentinelClient() {
        this(CommonConstants.FleaPoolConstants.DEFAULT_POOL_NAME);
    }

    /**
     * Redis哨兵客户端构造方法（指定连接池名）
     *
     * @param poolName 连接池名
     * @since 1.1.0
     */
    private FleaRedisSentinelClient(String poolName) {
        super(poolName);
        init();
    }

    /**
     * 初始化Jedis哨兵实例
     *
     * @since 1.1.0
     */
    private void init() {
        if (CommonConstants.FleaPoolConstants.DEFAULT_POOL_NAME.equals(getPoolName())) {
            jedisSentinelPool = RedisSentinelPool.getInstance().getJedisSentinelPool();
        } else {
            jedisSentinelPool = RedisSentinelPool.getInstance(getPoolName()).getJedisSentinelPool();
        }

    }

    @Override
    public String set(String key, Object value) {
        if (value instanceof String)
            return jedisSentinelPool.getResource().set(key, (String) value);
        else
            return jedisSentinelPool.getResource().set(SafeEncoder.encode(key), ObjectUtils.serialize(value));
    }

    @Override
    public String set(byte[] key, byte[] value) {
        return jedisSentinelPool.getResource().set(key, value);
    }

    @Override
    public String set(final String key, final Object value, final int expiry) {
        if (value instanceof String)
            return jedisSentinelPool.getResource().setex(key, expiry, (String) value);
        else
            return jedisSentinelPool.getResource().setex(SafeEncoder.encode(key), expiry, ObjectUtils.serialize(value));
    }

    @Override
    public String set(byte[] key, byte[] value, int expiry) {
        return jedisSentinelPool.getResource().setex(key, expiry, value);
    }

    @Override
    public String set(String key, Object value, long expiry) {
        if (value instanceof String)
            return jedisSentinelPool.getResource().psetex(key, expiry, (String) value);
        else
            return jedisSentinelPool.getResource().psetex(SafeEncoder.encode(key), expiry, ObjectUtils.serialize(value));
    }

    @Override
    public String set(byte[] key, byte[] value, long expiry) {
        return jedisSentinelPool.getResource().psetex(key, expiry, value);
    }

    @Override
    public String set(final String key, final Object value, final SetParams params) {
        if (value instanceof String)
            return jedisSentinelPool.getResource().set(key, (String) value, params);
        else
            return jedisSentinelPool.getResource().set(SafeEncoder.encode(key), ObjectUtils.serialize(value), params);
    }

    @Override
    public String set(byte[] key, byte[] value, SetParams params) {
        return jedisSentinelPool.getResource().set(key, value, params);
    }

    @Override
    public byte[] get(byte[] key) {
        return jedisSentinelPool.getResource().get(key);
    }

    @Override
    public Long del(final String key) {
        return jedisSentinelPool.getResource().del(key);
    }


    /**
     * 获取客户端类
     *
     * @param key 缓存实际存储的键
     * @return 客户端类
     * @since 1.1.0
     */
    @Override
    protected Client getClientByKey(Object key) {
        RedisSentinelConfig redisSentinelConfig = RedisSentinelConfig.getConfig();
        String masterName = redisSentinelConfig.getMasterName();
        Jedis jedis = jedisSentinelPool.getResource();
        List<String> masterAddr = jedis.sentinelGetMasterAddrByName(masterName);
        String masterHost = masterAddr.get(0);
        int masterPort = Integer.parseInt(masterAddr.get(1));

        Set<HostAndPort> clusterNodes = new HashSet<>();
        clusterNodes.add(new HostAndPort(masterHost, masterPort));

        JedisCluster cluster = new JedisCluster(clusterNodes);

        Client client = null;
        if (ObjectUtils.isNotEmpty(key)) {
            int slot = 0;
            if (key instanceof String) {
                slot = JedisClusterCRC16.getSlot(key.toString());
            } else if (key instanceof byte[]) {
                slot = JedisClusterCRC16.getSlot((byte[]) key);
            }
            // 根据槽位slot获取缓存键key所在的节点
            client = cluster.getConnectionFromSlot(slot).getClient();
        }
        cluster.close();
        return client;
    }

    /**
     * 内部建造者类
     *
     * @author huazie
     * @version 1.1.0
     * @since 1.1.0
     */
    public static class Builder {

        private String poolName; // 连接池名

        /**
         * 默认构造器
         *
         * @since 1.1.0
         */
        public Builder() {
        }

        /**
         * 指定连接池的构造器
         *
         * @param poolName 连接池名
         * @since 1.1.0
         */
        public Builder(String poolName) {
            this.poolName = poolName;
        }

        /**
         * 构建Redis集群客户端对象
         *
         * @return Redis集群客户端
         * @since 1.1.0
         */
        public RedisClient build() {
            if (StringUtils.isBlank(poolName)) {
                return new FleaRedisSentinelClient();
            } else {
                return new FleaRedisSentinelClient(poolName);
            }
        }
    }
}
