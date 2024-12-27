package com.huazie.fleaframework.cache.common;

import redis.clients.jedis.HostAndPort;

/**
 * Flea缓存工具类
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class CacheUtils {

    private CacheUtils() {
    }

    /**
     * 根据指定缓存模式枚举，判断是否是集群模式
     *
     * @param cacheMode 缓存模式枚举
     * @return true：集群模式  false：非集群模式
     * @since 1.1.0
     */
    public static boolean isClusterMode(CacheModeEnum cacheMode) {
        return CacheModeEnum.CLUSTER.getMode() == cacheMode.getMode();
    }


    public static boolean isSentinelMode(CacheModeEnum cacheMode) {
        return CacheModeEnum.SENTINEL.getMode() == cacheMode.getMode();
    }

    /**
     * 从服务器地址字符串创建HostAndPort实例，
     * 该字符串必须是( host + ":" + port ) 格式。
     *
     * @param server 服务器地址
     * @return HostAndPort实例
     * @since 1.1.0
     */
    public static HostAndPort parseString(String server) {
        try {
            String[] parts = HostAndPort.extractParts(server);
            String host = parts[0];
            int port = Integer.parseInt(parts[1]);
            return new HostAndPort(host, port);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
