package com.huazie.fleaframework.cache;

import com.huazie.fleaframework.common.CommonConstants;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 抽象Spring缓存管理类，用于接入Spring框架管理缓存。
 *
 * <p> 同步集合类【{@code cacheMap}】, 存储的键为缓存数据主关键字，
 * 存储的值为具体的缓存实现类。<br/>
 * 如果是整合各类缓存接入，它的键对应缓存定义配置文件【flea-cache.xml】
 * 中的【{@code <cache key="缓存数据主关键字"></cache>}】；<br/>
 * 如果是单个缓存接入，它的键对应【applicationContext.xml】中
 * 【{@code <entry key="缓存数据主关键字" value="有效期（单位：s）"/>}】；
 *
 * <p> 抽象方法【{@code newCache}】，由子类实现具体的Spring缓存类创建。
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractSpringCacheManager extends AbstractTransactionSupportingCacheManager {

    private static final ConcurrentMap<String, AbstractSpringCache> cacheMap = new ConcurrentHashMap<>();

    private Map<String, Integer> configMap = new HashMap<>();   // 各缓存的时间Map

    @Override
    protected Collection<? extends AbstractSpringCache> loadCaches() {
        return cacheMap.values();
    }

    @Override
    public AbstractSpringCache getCache(String name) {
        if (!cacheMap.containsKey(name)) {
            synchronized (cacheMap) {
                if (!cacheMap.containsKey(name)) {
                    Integer expiry = configMap.get(name);
                    if (expiry == null) {
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
     * 由子类实现该方法，新创建一个抽象 Spring 缓存的子类。
     *
     * @param name   缓存名
     * @param expiry 有效期（单位：s  其中0：表示永久）
     * @return 新建的缓存对象
     * @since 1.0.0
     */
    protected abstract AbstractSpringCache newCache(String name, int expiry);

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
