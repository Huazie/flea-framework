package com.huazie.fleaframework.cache.common;

/**
 * Flea缓存常量类
 *
 * @author huazie
 * @version 1.1.0
 * @since 1.0.0
 */
public class CacheConstants {

    /**
     * Flea Cache配置数据常量
     *
     * @since 1.0.0
     */
    public interface FleaCacheConfigConstants {
        /**
         * Flea Cache 默认文件路径
         */
        String FLEA_CACHE_FILE_NAME = "flea/cache/flea-cache.xml";
        /**
         * Flea Cache 系统环境变量
         */
        String FLEA_CACHE_FILE_SYSTEM_KEY = "fleaframework.flea.cache.filename";
        /**
         * Flea Cache Config 默认文件路径
         */
        String FLEA_CACHE_CONFIG_FILE_NAME = "flea/cache/flea-cache-config.xml";
        /**
         * Flea Cache Config 系统环境变量
         */
        String FLEA_CACHE_CONFIG_FILE_SYSTEM_KEY = "fleaframework.cache.fleacache.config.filename";
        /**
         * Flea缓存建造者配置项集
         */
        String FLEA_CACHE_BUILDER = "FleaCacheBuilder";
        /**
         * Flea缓存初始化配置项集
         */
        String FLEA_CACHE_INIT = "FleaCacheInit";
        /**
         * Flea缓存所属系统名
         */
        String SYSTEM_NAME = "systemName";
        /**
         * 空缓存数据有效期
         */
        String NULL_CACHE_EXPIRY = "fleacore.nullCacheExpiry";
        /**
         * 默认有效期（单位：s）
         */
        int DEFAULT_EXPIRY = 300;
    }

    /**
     * MemCached配置数据常量
     *
     * @since 1.0.0
     */
    public interface MemCachedConfigConstants {
        /**
         * MemCached 配置文件名
         */
        String MEMCACHED_FILE_NAME = "flea/cache/memcached.properties";
        /**
         * MemCached 配置文件系统环境变量
         */
        String MEMCACHED_CONFIG_FILE_SYSTEM_KEY = "fleaframework.cache.memcached.config.filename";
        /**
         * MemCached 配置开关（1：开启 0：关闭）
         */
        String MEMCACHED_CONFIG_SWITCH = "memcached.switch";
        /**
         * MemCached 缓存所属系统名
         */
        String MEMCACHED_CONFIG_SYSTEM_NAME = "memcached.systemName";
        /**
         * MemCached 服务器地址
         */
        String MEMCACHED_CONFIG_SERVER = "memcached.server";
        /**
         * MemCached 服务器权重分配
         */
        String MEMCACHED_CONFIG_WEIGHT = "memcached.weight";
        /**
         * 初始化时对每个服务器建立的连接数目
         */
        String MEMCACHED_CONFIG_INITCONN = "memcached.initConn";
        /**
         * 每个服务器建立最小的连接数
         */
        String MEMCACHED_CONFIG_MINCONN = "memcached.minConn";
        /**
         * 每个服务器建立最大的连接数
         */
        String MEMCACHED_CONFIG_MAXCONN = "memcached.maxConn";
        /**
         * 自查线程周期进行工作，其每次休眠时间（单位：ms）
         */
        String MEMCACHED_CONFIG_MAINTSLEEP = "memcached.maintSleep";
        /**
         * Socket的参数，如果是true在写数据时不缓冲，立即发送出去
         */
        String MEMCACHED_CONFIG_NAGLE = "memcached.nagle";
        /**
         * Socket阻塞读取数据的超时时间（单位：ms）
         */
        String MEMCACHED_CONFIG_SOCKETTO = "memcached.socketTO";
        /**
         * Socket连接超时时间（单位：ms）
         */
        String MEMCACHED_CONFIG_SOCKETCONNECTTO = "memcached.socketConnectTO";
        /**
         * MemCached 一致性hash算法
         */
        String MEMCACHED_CONFIG_HASHINGALG = "memcached.hashingAlg";
        /**
         * MemCached 空缓存数据有效期（单位：s）
         */
        String MEMCACHED_CONFIG_NULLCACHEEXPIRY = "memcached.nullCacheExpiry";
    }

    /**
     * Redis配置数据常量
     *
     * @since 1.1.0
     */
    public interface RedisConfigConstants {
        /**
         * Redis 配置文件名
         */
        String REDIS_FILE_NAME = "flea/cache/redis.properties";
        /**
         * Redis 集群配置文件名
         */
        String REDIS_CLUSTER_FILE_NAME = "flea/cache/redis.cluster.properties";
        /**
         * Redis 哨兵配置文件名
         */
        String REDIS_SENTINEL_FILE_NAME = "flea/cache/redis.sentinel.properties";
        /**
         * Redis 配置文件系统环境变量
         */
        String REDIS_CONFIG_FILE_SYSTEM_KEY = "fleacache.redis.config.filename";
        /**
         * Redis 集群配置文件系统环境变量
         */
        String REDIS_CLUSTER_CONFIG_FILE_SYSTEM_KEY = "fleacache.redis.cluster.config.filename";
        String REDIS_SENTINEL_CONFIG_FILE_SYSTEM_KEY = "fleacache.redis.sentinel.config.filename";
        /**
         * Redis 分片配置开关（1：开启 0：关闭）
         */
        String REDIS_CONFIG_SWITCH = "redis.switch";
        /**
         * Redis 集群配置开关（1：开启 0：关闭）
         */
        String REDIS_CLUSTER_CONFIG_SWITCH = "redis.cluster.switch";
        String REDIS_SENTINEL_CONFIG_SWITCH = "redis.sentinel.switch";
        /**
         * Redis 缓存所属系统名
         */
        String REDIS_CONFIG_SYSTEM_NAME = "redis.systemName";
        /**
         * Redis 服务器地址
         */
        String REDIS_CONFIG_SERVER = "redis.server";
        /**
         * Redis 集群服务节点
         */
        String REDIS_CLUSTER_CONFIG_SERVER = "redis.cluster.server";
        String REDIS_SENTINEL_CONFIG_SERVER = "redis.sentinel.server";
        /**
         * Redis 授权密码
         */
        String REDIS_CONFIG_PASSWORD = "redis.password";
        /**
         * Redis 授权密码
         */
        String REDIS_CLUSTER_CONFIG_PASSWORD = "redis.cluster.password";
        String REDIS_SENTINEL_CONFIG_PASSWORD = "redis.sentinel.password";
        /**
         * Redis 服务器权重配置
         */
        String REDIS_CONFIG_WEIGHT = "redis.weight";
        /**
         * Redis 客户端socket连接超时时间
         */
        String REDIS_CONFIG_CONNECTIONTIMEOUT = "redis.connectionTimeout";
        /**
         * Redis 集群客户端socket连接超时时间
         */
        String REDIS_CLUSTER_CONFIG_CONNECTIONTIMEOUT = "redis.cluster.connectionTimeout";
        String REDIS_SENTINEL_CONFIG_CONNECTIONTIMEOUT = "redis.sentinel.connectionTimeout";
        /**
         * Redis 客户端socket连接超时时间
         */
        String REDIS_CONFIG_SOTIMEOUT = "redis.soTimeout";
        /**
         * Redis 集群客户端socket连接超时时间
         */
        String REDIS_CLUSTER_CONFIG_SOTIMEOUT = "redis.cluster.soTimeout";
        String REDIS_SENTINEL_CONFIG_SOTIMEOUT = "redis.sentinel.soTimeout";
        /**
         * Redis客户端操作最大尝试次数【包含第一次操作】
         */
        String REDIS_MAXATTEMPTS = "redis.maxAttempts";
        /**
         * Redis客户端操作最大尝试次数【包含第一次操作】（默认5次）
         */
        int REDIS_MAXATTEMPTS_DEFAULT = 5;
        /**
         * Redis 分布式hash算法
         * 1 : MURMUR_HASH
         * 2 : MD5
         */
        String REDIS_CONFIG_HASHINGALG = "redis.hashingAlg";
        /**
         * MURMUR_HASH 分布式hash算法
         */
        int REDIS_CONFIG_HASHINGALG_MURMUR_HASH = 1;
        /**
         * MD5 分布式hash算法
         */
        int REDIS_CONFIG_HASHINGALG_MD5 = 2;
        /**
         * Redis客户端Jedis连接池最大连接数
         */
        String REDIS_CONFIG_POOL_MAXTOTAL = "redis.pool.maxTotal";
        /**
         * Redis客户端Jedis连接池最大空闲连接数
         */
        String REDIS_CONFIG_POOL_MAXIDLE = "redis.pool.maxIdle";
        /**
         * Redis客户端Jedis连接池最小空闲连接数
         */
        String REDIS_CONFIG_POOL_MINIDLE = "redis.pool.minIdle";
        /**
         * Redis客户端Jedis连接池获取连接时的最大等待毫秒数
         */
        String REDIS_CONFIG_POOL_MAXWAITMILLIS = "redis.pool.maxWaitMillis";
        /**
         * Redis 空缓存数据有效期（单位：s）
         */
        String REDIS_CONFIG_NULLCACHEEXPIRY = "redis.nullCacheExpiry";

        String REDIS_SENTINEL_CONFIG_MASTERNAME = "redis.sentinel.masterName";
        String REDIS_SENTINEL_CONFIG_DATABASE = "redis.sentinel.dataBase";
    }

}
