package com.huazie.fleaframework.cache.common;

/**
 * 缓存实现枚举，定义了Flea框架支持的缓存实现。
 *
 * <p> 目前包含MemCached、RedisSharded、RedisCluster 和 FleaCore，
 * 其中【{@code FleaCore}】是Flea框架为了整合各类缓存而抽象出来的一类缓存实现。
 *
 * @author huazie
 * @version 1.1.0
 * @since 1.0.0
 */
public enum CacheEnum {
    EmptyCache("EmptyCache", "空缓存实现"),
    /**
     * 一个高性能的分布式内存对象缓存系统。
     */
    MemCached("MemCached", "一个高性能的分布式内存对象缓存系统"),
    /**
     * 一个开源的使用ANSI C语言编写、支持网络、可基于内存亦可持久化的高性能的日志型、Key-Value存储系统。<br/>
     * 这里缓存实现主要使用分片模式，支持使用分布式Jedis对象操作Redis缓存。
     */
    RedisSharded("RedisSharded", "一个开源的使用ANSI C语言编写、支持网络、可基于内存亦可持久化的高性能的日志型、Key-Value存储系统。这里缓存实现主要针对分片模式，支持使用分布式Jedis对象操作Redis缓存。"),
    /**
     * 一个开源的使用ANSI C语言编写、支持网络、可基于内存亦可持久化的高性能的日志型、Key-Value存储系统。<br/>
     * 这里缓存实现主要使用集群模式，支持使用Jedis集群实例对象操作Redis缓存。
     */
    RedisCluster("RedisCluster", "一个开源的使用ANSI C语言编写、支持网络、可基于内存亦可持久化的高性能的日志型、Key-Value存储系统。这里缓存实现主要针对集群模式，支持使用Jedis集群实例对象操作Redis缓存。"),
    RedisSentinel("RedisSentinel", "一个开源的使用ANSI C语言编写、支持网络、可基于内存亦可持久化的高性能的日志型、Key-Value存储系统。这里缓存实现主要针对哨兵模式，支持使用Jedis集群实例对象操作Redis缓存。"),
    /**
     * 一个用于组合并使用其他缓存（如MemCached和Redis）的Flea核心缓存实现。
     */
    FleaCore("FleaCore", "一个用于组合并使用其他缓存（如MemCached和Redis）的Flea核心缓存实现");

    private String name;    // 缓存名称
    private String desc;    // 缓存描述

    CacheEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

}
