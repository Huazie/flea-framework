package com.huazie.fleaframework.cache.common;

import com.huazie.fleaframework.cache.config.Cache;
import com.huazie.fleaframework.cache.config.CacheData;
import com.huazie.fleaframework.cache.config.CacheDatas;
import com.huazie.fleaframework.cache.config.CacheFile;
import com.huazie.fleaframework.cache.config.CacheFiles;
import com.huazie.fleaframework.cache.config.CacheGroup;
import com.huazie.fleaframework.cache.config.CacheGroups;
import com.huazie.fleaframework.cache.config.CacheItem;
import com.huazie.fleaframework.cache.config.CacheItems;
import com.huazie.fleaframework.cache.config.CacheParam;
import com.huazie.fleaframework.cache.config.CacheParams;
import com.huazie.fleaframework.cache.config.CacheServer;
import com.huazie.fleaframework.cache.config.CacheServers;
import com.huazie.fleaframework.cache.config.Caches;
import com.huazie.fleaframework.cache.config.FleaCache;
import com.huazie.fleaframework.cache.config.FleaCacheConfig;
import com.huazie.fleaframework.cache.exceptions.FleaCacheConfigException;
import com.huazie.fleaframework.common.slf4j.FleaLogger;
import com.huazie.fleaframework.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.fleaframework.common.util.CollectionUtils;
import com.huazie.fleaframework.common.util.ExceptionUtils;
import com.huazie.fleaframework.common.util.ObjectUtils;
import com.huazie.fleaframework.common.util.StringUtils;
import com.huazie.fleaframework.common.util.xml.Import;
import com.huazie.fleaframework.common.util.xml.XmlDigesterHelper;
import org.apache.commons.digester.Digester;

import java.util.List;

/**
 * Flea缓存配置文件XML解析类，用于读取并加载缓存定义文件
 * 【flea-cache.xml】和缓存配置文件【flea-cache-config.xml】
 *
 * @author huazie
 * @version 2.0.0
 * @since 1.0.0
 */
public class CacheXmlDigesterHelper {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(CacheXmlDigesterHelper.class);

    private static volatile CacheXmlDigesterHelper xmlDigester;

    private final Object fleaCacheInitLock = new Object();

    private final Object fleaCacheConfigInitLock = new Object();

    private FleaCache fleaCache;

    private FleaCacheConfig fleaCacheConfig;

    /**
     * 只允许通过getInstance()获取 XML解析类
     *
     * @since 1.0.0
     */
    private CacheXmlDigesterHelper() {
    }

    /**
     * 获取XML解析工具类
     *
     * @return XML解析工具类对象
     * @since 1.0.0
     */
    public static CacheXmlDigesterHelper getInstance() {
        if (ObjectUtils.isEmpty(xmlDigester)) {
            synchronized (CacheXmlDigesterHelper.class) {
                if (ObjectUtils.isEmpty(xmlDigester)) {
                    xmlDigester = new CacheXmlDigesterHelper();
                }
            }
        }
        return xmlDigester;
    }

    /**
     * 获取Flea缓存
     *
     * @return Flea缓存
     * @since 1.0.0
     */
    public FleaCache getFleaCache() {
        if (ObjectUtils.isEmpty(fleaCache)) {
            synchronized (fleaCacheInitLock) {
                if (ObjectUtils.isEmpty(fleaCache)) {
                    fleaCache = newFleaCache();
                }
            }
        }
        return fleaCache;
    }

    private FleaCache newFleaCache() {

        String fileName = CacheConstants.FleaCacheConfigConstants.FLEA_CACHE_FILE_NAME;
        if (StringUtils.isNotBlank(System.getProperty(CacheConstants.FleaCacheConfigConstants.FLEA_CACHE_FILE_SYSTEM_KEY))) {
            fileName = StringUtils.trim(System.getProperty(CacheConstants.FleaCacheConfigConstants.FLEA_CACHE_FILE_SYSTEM_KEY));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Use the specified flea-cache.xml : " + fileName);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Use the current flea-cache.xml : " + fileName);
            LOGGER.debug("Start to parse the flea-cache.xml");
        }

        Digester digester = newFleaCacheFileDigester();
        FleaCache fleaCache = XmlDigesterHelper.parse(fileName, digester, FleaCache.class);

        if (ObjectUtils.isNotEmpty(fleaCache)) {
            List<Import> importList = fleaCache.getImportList();
            if (CollectionUtils.isNotEmpty(importList)) {
                CacheFiles cacheFiles = new CacheFiles();
                for (Import mImport : importList) {
                    if (ObjectUtils.isEmpty(mImport)) continue;
                    String resource = mImport.getResource();
                    // 解析其他缓存定义配置文件
                    FleaCache other = XmlDigesterHelper.parse(resource, digester, FleaCache.class);
                    if (ObjectUtils.isEmpty(other)) continue;
                    CacheFile cacheFile = new CacheFile();
                    cacheFile.setLocation(resource);
                    Caches otherCaches = other.getCaches();
                    if (ObjectUtils.isNotEmpty(otherCaches)) {
                        // 添加Flea缓存至缓存文件对象中
                        cacheFile.setCacheList(otherCaches.getCacheList());
                    }
                    cacheFiles.addCacheFile(cacheFile);
                }
                fleaCache.setCacheFiles(cacheFiles);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("End to parse the flea-cache.xml");
        }

        return fleaCache;
    }

    /**
     * 解析flea-cache.xml的Digester对象
     *
     * @return Digester对象
     * @since 1.0.0
     */
    private Digester newFleaCacheFileDigester() {
        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("flea-cache", FleaCache.class.getName());
        digester.addSetProperties("flea-cache");

        // 缓存集
        digester.addObjectCreate("flea-cache/caches", Caches.class.getName());
        digester.addSetProperties("flea-cache/caches");

        digester.addObjectCreate("flea-cache/caches/cache", Cache.class.getName());
        digester.addSetProperties("flea-cache/caches/cache");

        digester.addSetNext("flea-cache/caches", "setCaches", Caches.class.getName());
        digester.addSetNext("flea-cache/caches/cache", "addFleaCache", Cache.class.getName());

        // 其他缓存定义文件资源导入
        digester.addObjectCreate("flea-cache/import", Import.class.getName());
        digester.addSetProperties("flea-cache/import");
        digester.addSetNext("flea-cache/import", "addImport", Import.class.getName());

        return digester;
    }

    /**
     * 获取Flea缓存配置
     *
     * @return Flea缓存配置
     * @since 1.0.0
     */
    public FleaCacheConfig getFleaCacheConfig() {
        if (ObjectUtils.isEmpty(fleaCacheConfig)) {
            synchronized (fleaCacheConfigInitLock) {
                if (ObjectUtils.isEmpty(fleaCacheConfig)) {
                    try {
                        fleaCacheConfig = newFleaCacheConfig();
                    } catch (Exception e) {
                        ExceptionUtils.throwFleaException(FleaCacheConfigException.class, e);
                    }
                }
            }
        }
        return fleaCacheConfig;
    }

    private FleaCacheConfig newFleaCacheConfig() {

        String fileName = CacheConstants.FleaCacheConfigConstants.FLEA_CACHE_CONFIG_FILE_NAME;
        if (StringUtils.isNotBlank(System.getProperty(CacheConstants.FleaCacheConfigConstants.FLEA_CACHE_CONFIG_FILE_SYSTEM_KEY))) {
            fileName = StringUtils.trim(System.getProperty(CacheConstants.FleaCacheConfigConstants.FLEA_CACHE_CONFIG_FILE_SYSTEM_KEY));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Use the specified flea-cache-config.xml : " + fileName);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Use the current flea-cache-config.xml : " + fileName);
            LOGGER.debug("Start to parse the flea-cache-config.xml");
        }

        Digester digester = newFleaCacheConfigFileDigester();
        FleaCacheConfig obj = XmlDigesterHelper.parse(fileName, digester, FleaCacheConfig.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("End to parse the flea-cache-config.xml");
        }

        return obj;
    }

    /**
     * 解析flea-cache-config.xml的Digester对象
     *
     * @return Digester对象
     * @since 1.0.0
     */
    private Digester newFleaCacheConfigFileDigester() {
        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("flea-cache-config", FleaCacheConfig.class.getName());
        digester.addSetProperties("flea-cache-config");

        // 缓存配置项集
        digester.addObjectCreate("flea-cache-config/cache-items", CacheItems.class.getName());
        digester.addSetProperties("flea-cache-config/cache-items");

        digester.addObjectCreate("flea-cache-config/cache-items/cache-item", CacheItem.class.getName());
        digester.addSetProperties("flea-cache-config/cache-items/cache-item");
        digester.addBeanPropertySetter("flea-cache-config/cache-items/cache-item", "value");

        // 缓存参数集
        digester.addObjectCreate("flea-cache-config/cache-params", CacheParams.class.getName());
        digester.addSetProperties("flea-cache-config/cache-params");

        digester.addObjectCreate("flea-cache-config/cache-params/cache-param", CacheParam.class.getName());
        digester.addSetProperties("flea-cache-config/cache-params/cache-param");
        digester.addBeanPropertySetter("flea-cache-config/cache-params/cache-param", "value");

        // 缓存数据集
        digester.addObjectCreate("flea-cache-config/cache-datas", CacheDatas.class.getName());
        digester.addSetProperties("flea-cache-config/cache-datas");

        digester.addObjectCreate("flea-cache-config/cache-datas/cache-data", CacheData.class.getName());
        digester.addSetProperties("flea-cache-config/cache-datas/cache-data");
        digester.addBeanPropertySetter("flea-cache-config/cache-datas/cache-data", "group");

        // 缓存组集
        digester.addObjectCreate("flea-cache-config/cache-groups", CacheGroups.class.getName());
        digester.addSetProperties("flea-cache-config/cache-groups");

        digester.addObjectCreate("flea-cache-config/cache-groups/cache-group", CacheGroup.class.getName());
        digester.addSetProperties("flea-cache-config/cache-groups/cache-group");
        digester.addBeanPropertySetter("flea-cache-config/cache-groups/cache-group", "cache");

        // 缓存服务器集
        digester.addObjectCreate("flea-cache-config/cache-servers", CacheServers.class.getName());
        digester.addSetProperties("flea-cache-config/cache-servers");

        digester.addObjectCreate("flea-cache-config/cache-servers/cache-server", CacheServer.class.getName());
        digester.addSetProperties("flea-cache-config/cache-servers/cache-server");
        digester.addBeanPropertySetter("flea-cache-config/cache-servers/cache-server", "server");

        digester.addSetNext("flea-cache-config/cache-items", "addCacheItems", CacheItems.class.getName());
        digester.addSetNext("flea-cache-config/cache-items/cache-item", "addCacheItem", CacheItem.class.getName());

        digester.addSetNext("flea-cache-config/cache-params", "setCacheParams", CacheParams.class.getName());
        digester.addSetNext("flea-cache-config/cache-params/cache-param", "addCacheParam", CacheParam.class.getName());

        digester.addSetNext("flea-cache-config/cache-datas", "setCacheDatas", CacheDatas.class.getName());
        digester.addSetNext("flea-cache-config/cache-datas/cache-data", "addCacheData", CacheData.class.getName());

        digester.addSetNext("flea-cache-config/cache-groups", "setCacheGroups", CacheGroups.class.getName());
        digester.addSetNext("flea-cache-config/cache-groups/cache-group", "addCacheGroup", CacheGroup.class.getName());

        digester.addSetNext("flea-cache-config/cache-servers", "setCacheServers", CacheServers.class.getName());
        digester.addSetNext("flea-cache-config/cache-servers/cache-server", "addCacheServer", CacheServer.class.getName());
        return digester;
    }

}
