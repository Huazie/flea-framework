package com.huazie.fleaframework.cache.redis.config;

import com.huazie.fleaframework.cache.common.CacheConfigUtils;
import com.huazie.fleaframework.cache.common.CacheConstants.RedisConfigConstants;
import com.huazie.fleaframework.cache.common.CacheUtils;
import com.huazie.fleaframework.cache.exceptions.FleaCacheConfigException;
import com.huazie.fleaframework.cache.exceptions.FleaCacheException;
import com.huazie.fleaframework.common.CommonConstants;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.*;
import org.apache.commons.lang.builder.ToStringBuilder;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.util.Hashing;
import redis.clients.jedis.util.Sharded;

import java.util.*;


public class RedisSentinelConfig extends RedisCommonConfig {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(RedisSentinelConfig.class);



    private static volatile RedisSentinelConfig config;

    private static Properties prop;

    private String masterName;

    private Set<String> sentinels;

    private String password; // Redis集群的服务节点登录密码（集群配置同一个）

    private int connectionTimeout; // Redis集群客户端socket连接超时时间（单位：ms）

    private int soTimeout; // Redis集群客户端socket读写超时时间（单位：ms）

    private int dataBase;

    static {
        String fileName = RedisConfigConstants.REDIS_SENTINEL_FILE_NAME;
        if (StringUtils.isNotBlank(System.getProperty(RedisConfigConstants.REDIS_SENTINEL_CONFIG_FILE_SYSTEM_KEY))) {
            fileName = StringUtils.trim(System.getProperty(RedisConfigConstants.REDIS_SENTINEL_CONFIG_FILE_SYSTEM_KEY));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("RedisSENTINELConfig Use the specified redis.SENTINEL.properties：{}", fileName);
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RedisSENTINELConfig Use the current redis.SENTINEL.properties：{}", fileName);
        }
        // 获取配置文件
        prop = PropertiesUtil.getProperties(fileName);
    }

    private RedisSentinelConfig() {
        try {
            // Redis sb配置开关（1：开启 0：关闭），如果不配置也默认开启
            setSwitchFlag(prop, RedisConfigConstants.REDIS_SENTINEL_CONFIG_SWITCH);
            // 缓存归属系统
            setSystemName(prop);
            // 空缓存数据有效期
            setNullCacheExpiry(prop);
            // Jedis连接配置信息
            setJedisPoolConfig(prop);
            //主节点名
            setMasterName();
            // 添加Redis sb的服务节点Set集合
            setSentinels();
            // 设置Redis sb的服务节点登录密码
            setPassword();
            // Redis sb客户端socket连接超时时间（单位：ms）
            setConnectionTimeout();
            // Redis客户端socket读写超时时间（单位：ms）
            setSoTimeout();
            //数据库索引
            setDataBase();
            // Redis客户端操作最大尝试次数【包含第一次操作】
            setMaxAttempts(prop);
        } catch (FleaCacheException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Please check the redis sentinel config :", e);
            }
        }
    }

    /**
     * 获取Redis缓存配置类实例
     *
     * @return Redis缓存配置类实例
     * @since 1.0.0
     */
    public static RedisSentinelConfig getConfig() {
        if (ObjectUtils.isEmpty(config)) {
            synchronized (RedisSentinelConfig.class) {
                if (ObjectUtils.isEmpty(config)) {
                    config = new RedisSentinelConfig();
                }
            }
        }
        return config;
    }


    public String getPassword() {
        return password;
    }

    /**
     * 设置Redis集群的服务节点登录密码（集群配置同一个）
     *
     * @since 1.1.0
     */
    private void setPassword() {
        this.password = PropertiesUtil.getStringValue(prop, RedisConfigConstants.REDIS_SENTINEL_CONFIG_PASSWORD);
    }


    public int getConnectionTimeout() {
        return connectionTimeout;
    }


    private void setConnectionTimeout() {
        int connectionTimeout = Protocol.DEFAULT_TIMEOUT; // 默认Redis集群客户端socket连接超时时间（单位：ms）
        String connectionTimeoutStr = PropertiesUtil.getStringValue(prop, RedisConfigConstants.REDIS_SENTINEL_CONFIG_CONNECTIONTIMEOUT);
        if (StringUtils.isNotBlank(connectionTimeoutStr)) {
            connectionTimeout = Integer.parseInt(connectionTimeoutStr);
        }
        this.connectionTimeout = connectionTimeout;
    }


    public int getSoTimeout() {
        return soTimeout;
    }


    private void setSoTimeout() {
        int soTimeout = Protocol.DEFAULT_TIMEOUT; // 默认Redis客户端socket读写超时时间（单位：ms）
        String soTimeoutStr = PropertiesUtil.getStringValue(prop, RedisConfigConstants.REDIS_SENTINEL_CONFIG_SOTIMEOUT);
        if (StringUtils.isNotBlank(soTimeoutStr)) {
            soTimeout = Integer.parseInt(soTimeoutStr);
        }
        this.soTimeout = soTimeout;
    }


    public String getClientName() {
        return CacheConfigUtils.getClientName(getSystemName());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getMasterName() {
        return masterName;
    }

    private void setMasterName() {
        this.masterName = PropertiesUtil.getStringValue(prop, RedisConfigConstants.REDIS_SENTINEL_CONFIG_MASTERNAME);
    }


    public Set<String> getSentinels() {
        return sentinels;
    }


    private void setSentinels() throws FleaCacheConfigException {
        // Redis sb服务节点Set集合
        Set<String> sentinels = null;
        try {
            // 取 Redis sb的服务器地址
            String servers = PropertiesUtil.getStringValue(prop, RedisConfigConstants.REDIS_SENTINEL_CONFIG_SERVER);
            if (StringUtils.isNotBlank(servers)) {
                // 取逗号分隔的服务器地址信息
                String[] serverArr = StringUtils.split(servers, CommonConstants.SymbolConstants.COMMA);
                if (ArrayUtils.isNotEmpty(serverArr)) {
                    sentinels = new HashSet<>();
                    for (String server : serverArr) {
                        sentinels.add(server);
                    }
                }
            }
        } catch (Exception e) {
            ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "Redis哨兵服务器地址有误，请检查配置【" + RedisConfigConstants.REDIS_SENTINEL_CONFIG_SERVER + "】", e);
        }
        if (ObjectUtils.isEmpty(sentinels)) {
            ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "未配置Redis哨兵服务器地址，请检查配置【" + RedisConfigConstants.REDIS_SENTINEL_CONFIG_SERVER + "】");
        }
        this.sentinels = sentinels;
    }

    public int getDataBase() {
        return dataBase;
    }

    private void setDataBase() {
        int database = Protocol.DEFAULT_DATABASE; //
        String databaseStr = PropertiesUtil.getStringValue(prop, RedisConfigConstants.REDIS_SENTINEL_CONFIG_DATABASE);
        if (StringUtils.isNotBlank(databaseStr)) {
            database = Integer.parseInt(databaseStr);
            if (database < 0 || database > 15) {
                ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "范围不对，应为0-15【" + RedisConfigConstants.REDIS_SENTINEL_CONFIG_DATABASE + "】");
            }
        }
        this.connectionTimeout = database;
    }
}
