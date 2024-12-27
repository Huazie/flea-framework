package com.huazie.fleaframework.cache;

import com.huazie.fleaframework.common.CommonConstants;
import com.huazie.fleaframework.common.util.ObjectUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 抽象Flea缓存管理类，用于接入Flea框架管理缓存。
 *
 * <p> 同步集合类【{@code cacheMap}】, 存储的键为缓存数据主关键字，
 * 存储的值为具体的缓存实现类。<br/>
 * 如果是整合各类缓存接入，它的键对应缓存定义配置文件【flea-cache.xml】
 * 中的【{@code <cache key="缓存数据主关键字"></cache>}】；<br/>
 * 如果是单个缓存接入，它的键对应【applicationContext.xml】中
 * 【{@code <entry key="缓存数据主关键字"value="有效期（单位：s）"/>}】；
 *
 * <p> 抽象方法【{@code newCache}】，由子类实现具体的Flea缓存类创建。
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractFleaCacheManager {

    private static final ConcurrentMap<String, AbstractFleaCache> cacheMap = new ConcurrentHashMap<>();

    private Map<String, Integer> configMap = new HashMap<>();   // 各缓存的时间Map

    /**
     * 获取所有的Flea缓存
     *
     * @return 所有的Flea缓存
     * @since 1.0.0
     */
    protected Collection<AbstractFleaCache> loadCaches() {
        return cacheMap.values();
    }

    /**
     * 根据指定缓存名获取缓存对象
     *
     * @param name 缓存名
     * @return 缓存对象
     * @since 1.0.0
     */
    public AbstractFleaCache getCache(String name) {
        if (!cacheMap.containsKey(name)) {
            synchronized (cacheMap) {
                if (!cacheMap.containsKey(name)) {
                    Integer expiry = configMap.get(name);
                    if (ObjectUtils.isEmpty(expiry)) {
                        expiry = CommonConstants.NumeralConstants.INT_ZERO; // 表示永久
                        configMap.put(name, expiry);
                    }
                    cacheMap.put(name, newCache(name, expiry));
                }
            }
        }
        return cacheMap.get(name);
    }

    /**
     * 新创建一个缓存对象
     *
     * @param name   缓存名
     * @param expiry 有效期（单位：s  其中0：表示永久）
     * @return 新建的缓存对象
     * @since 1.0.0
     */
    protected abstract AbstractFleaCache newCache(String name, int expiry);

    /**
     * 设置各缓存有效期配置Map
     *
     * @param configMap 有效期配置Map
     * @since 1.0.0
     */
    public void setConfigMap(Map<String, Integer> configMap) {
        this.configMap = configMap;
    }

}
