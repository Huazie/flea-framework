package com.huazie.fleaframework.cache.common;

import com.huazie.fleaframework.cache.AbstractFleaCache;
import com.huazie.fleaframework.cache.IFleaCacheBuilder;
import com.huazie.fleaframework.cache.config.Cache;
import com.huazie.fleaframework.cache.config.CacheData;
import com.huazie.fleaframework.cache.config.CacheGroup;
import com.huazie.fleaframework.cache.config.CacheItem;
import com.huazie.fleaframework.cache.config.CacheServer;
import com.huazie.fleaframework.cache.exceptions.FleaCacheConfigException;
import com.huazie.fleaframework.common.util.CollectionUtils;
import com.huazie.fleaframework.common.util.ExceptionUtils;
import com.huazie.fleaframework.common.util.ObjectUtils;
import com.huazie.fleaframework.common.util.ReflectUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Flea Cache 工厂类，用于整合各类缓存接入时，创建具体的缓存实现类。
 *
 * <p> 同步集合类 {@code fleaCacheMap}，存储的键为缓存数据主关键字，
 * 对应缓存定义配置文件【flea-cache.xml】中的【{@code <cache
 * key="缓存数据主关键字"></cache>}】；它的值为具体的缓存实现类。
 *
 * @author huazie
 * @version 1.1.0
 * @since 1.0.0
 */
public class FleaCacheFactory {

    private static final ConcurrentMap<String, AbstractFleaCache> fleaCacheMap = new ConcurrentHashMap<>();

    private FleaCacheFactory() {
    }

    /**
     * 根据缓存数据主关键字获取指定Flea缓存对象
     *
     * @param name 缓存数据主关键字（对应 flea-cache.xml {@code <cache key="缓存数据主关键字"></cache>}）
     * @return Flea缓存对象
     * @since 1.0.0
     */
    public static AbstractFleaCache getFleaCache(String name) {
        if (!fleaCacheMap.containsKey(name)) {
            synchronized (fleaCacheMap) {
                if (!fleaCacheMap.containsKey(name)) {
                    fleaCacheMap.put(name, newFleaCache(name));
                }
            }
        }
        return fleaCacheMap.get(name);
    }

    /**
     * 根据缓存数据主关键字创建一个Flea缓存对象
     *
     * @param name 缓存数据主关键字（对应 flea-cache.xml {@code <cache key="缓存数据主关键字"></cache>}）
     * @return Flea缓存对象
     * @since 1.0.0
     */
    private static AbstractFleaCache newFleaCache(String name) {
        // 获取Flea缓存配置信息
        Cache cache = CacheConfigUtils.getCache(name);
        if (ObjectUtils.isEmpty(cache)) {
            ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "无法初始化Flea缓存，请检查flea-cache.xml配置【<cache key=" + name + " >】");
        }
        // 获取Flea缓存归属数据配置信息
        CacheData cacheData = CacheConfigUtils.getCacheData(cache.getType());
        if (ObjectUtils.isEmpty(cacheData)) {
            ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "无法初始化Flea缓存，请检查flea-cache-config.xml配置【<cache-data type=" + cache.getType() + " >】");
        }
        // 获取Flea缓存组
        CacheGroup cacheGroup = CacheConfigUtils.getCacheGroup(cacheData.getGroup());
        if (ObjectUtils.isEmpty(cacheGroup)) {
            ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "无法初始化Flea缓存，请检查flea-cache-config.xml配置【<cache-group group=" + cacheData.getGroup() + " >】");
        }
        // 获取缓存实现名
        String cacheImplName = cacheGroup.getCache();
        // 获取Flea缓存服务器
        List<CacheServer> cacheServerList = CacheConfigUtils.getCacheServer(cacheGroup.getGroup());
        if (CollectionUtils.isEmpty(cacheServerList)) {
            ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "无法初始化Flea缓存，请检查flea-cache-config.xml配置【<cache-server group=" + cacheGroup.getGroup() + " >】");
        }
        // 获取指定缓存系统名对应的Flea缓存建造者
        CacheItem cacheItem = CacheConfigUtils.getCacheItem(CacheConstants.FleaCacheConfigConstants.FLEA_CACHE_BUILDER, cacheImplName);
        if (ObjectUtils.isEmpty(cacheItem)) {
            ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "无法初始化Flea缓存，请检查flea-cache-config.xml配置【<cache-item key=" + cacheImplName + " >】");
        }
        // Flea缓存建造者
        String builder = cacheItem.getValue();
        if (ObjectUtils.isEmpty(builder)) {
            ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "无法初始化Flea缓存，请检查flea-cache-config.xml配置【<cache-item key=" + cacheImplName + " ></cache-item>】配置项值不能为空");
        }
        AbstractFleaCache fleaCache = null;
        try {
            IFleaCacheBuilder fleaCacheBuilder = (IFleaCacheBuilder) ReflectUtils.newInstance(builder);
            fleaCache = fleaCacheBuilder.build(name, cacheServerList);
        } catch (Exception e) {
            ExceptionUtils.throwFleaException(FleaCacheConfigException.class, "构建Flea缓存出现异常：\n", e);
        }
        return fleaCache;
    }

}
