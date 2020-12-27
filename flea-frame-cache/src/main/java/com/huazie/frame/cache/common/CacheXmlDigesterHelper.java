package com.huazie.frame.cache.common;

import com.huazie.frame.cache.config.Cache;
import com.huazie.frame.cache.config.CacheData;
import com.huazie.frame.cache.config.CacheDatas;
import com.huazie.frame.cache.config.CacheFile;
import com.huazie.frame.cache.config.CacheFiles;
import com.huazie.frame.cache.config.CacheGroup;
import com.huazie.frame.cache.config.CacheGroups;
import com.huazie.frame.cache.config.CacheItem;
import com.huazie.frame.cache.config.CacheItems;
import com.huazie.frame.cache.config.CacheParam;
import com.huazie.frame.cache.config.CacheParams;
import com.huazie.frame.cache.config.CacheServer;
import com.huazie.frame.cache.config.CacheServers;
import com.huazie.frame.cache.config.Caches;
import com.huazie.frame.cache.config.FleaCache;
import com.huazie.frame.cache.config.FleaCacheConfig;
import com.huazie.frame.common.XmlDigesterHelper;
import com.huazie.frame.common.slf4j.FleaLogger;
import com.huazie.frame.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.frame.common.util.CollectionUtils;
import com.huazie.frame.common.util.ObjectUtils;
import com.huazie.frame.common.util.StringUtils;
import org.apache.commons.digester.Digester;

import java.util.List;

/**
 * <p> Flea缓存配置文件XML解析类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class CacheXmlDigesterHelper {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(CacheXmlDigesterHelper.class);

    private static volatile CacheXmlDigesterHelper xmlDigester;

    private static Boolean isInit = Boolean.FALSE;
    private static Boolean isFleaCacheInit = Boolean.FALSE;
    private static Boolean isFleaCacheConfigInit = Boolean.FALSE;

    private static FleaCache fleaCache;
    private static FleaCacheConfig fleaCacheConfig;

    /**
     * <p> 只允许通过getInstance()获取 XML解析类 </p>
     */
    private CacheXmlDigesterHelper() {
    }

    /**
     * <p> 获取XML解析工具类 </p>
     *
     * @return XML解析工具类对象
     * @since 1.0.0
     */
    public static CacheXmlDigesterHelper getInstance() {
        if (isInit.equals(Boolean.FALSE)) {
            synchronized (isInit) {
                if (isInit.equals(Boolean.FALSE)) {
                    xmlDigester = new CacheXmlDigesterHelper();
                    isInit = Boolean.TRUE;
                }
            }
        }
        return xmlDigester;
    }

    /**
     * <p> 获取Flea缓存 </p>
     *
     * @return Flea缓存
     * @since 1.0.0
     */
    public FleaCache getFleaCache() {
        if (isFleaCacheInit.equals(Boolean.FALSE)) {
            synchronized (isFleaCacheInit) {
                if (isFleaCacheInit.equals(Boolean.FALSE)) {
                    try {
                        fleaCache = newFleaCache();
                        isFleaCacheInit = Boolean.TRUE;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
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
        FleaCache obj = XmlDigesterHelper.parse(fileName, digester, FleaCache.class);

        if (ObjectUtils.isNotEmpty(obj)) {
            CacheFiles cacheFiles = obj.getCacheFiles();
            if (ObjectUtils.isNotEmpty(cacheFiles)) {
                List<CacheFile> cacheFileList = cacheFiles.getCacheFiles();
                if (CollectionUtils.isNotEmpty(cacheFileList)) {
                    for (CacheFile cacheFile : cacheFileList) {
                        if (ObjectUtils.isNotEmpty(cacheFile)) {
                            // 解析其他缓存定义配置文件
                            FleaCache other = XmlDigesterHelper.parse(cacheFile.getLocation(), digester, FleaCache.class);
                            if (ObjectUtils.isNotEmpty(other)) {
                                Caches otherCaches = other.getCaches();
                                if (ObjectUtils.isNotEmpty(otherCaches)) {
                                    // 添加Flea缓存至缓存文件对象中
                                    cacheFile.setCacheList(otherCaches.getCacheList());
                                }
                            }
                        }
                    }
                }
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Config = {}", obj);
            LOGGER.debug("End to parse the flea-cache.xml");
        }

        return obj;
    }

    /**
     * <p> 解析flea-cache.xml的Digester对象 </p>
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

        // 其他缓存定义配置文件集
        digester.addObjectCreate("flea-cache/cache-files", CacheFiles.class.getName());
        digester.addSetProperties("flea-cache/cache-files");

        digester.addObjectCreate("flea-cache/cache-files/cache-file", CacheFile.class.getName());
        digester.addSetProperties("flea-cache/cache-files/cache-file");

        digester.addSetNext("flea-cache/cache-files", "setCacheFiles", CacheFiles.class.getName());
        digester.addSetNext("flea-cache/cache-files/cache-file", "addCacheFile", CacheFile.class.getName());
        digester.addCallMethod("flea-cache/cache-files/cache-file/location", "setLocation", 0);
        digester.addCallMethod("flea-cache/cache-files/cache-file/executions/execution", "addExecution", 0);
        return digester;
    }

    /**
     * <p> 获取Flea缓存配置 </p>
     *
     * @return Flea缓存配置
     * @since 1.0.0
     */
    public FleaCacheConfig getFleaCacheConfig() {
        if (isFleaCacheConfigInit.equals(Boolean.FALSE)) {
            synchronized (isFleaCacheConfigInit) {
                if (isFleaCacheConfigInit.equals(Boolean.FALSE)) {
                    try {
                        fleaCacheConfig = newFleaCacheConfig();
                        isFleaCacheConfigInit = Boolean.TRUE;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
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
            LOGGER.debug("Config = {}", obj);
            LOGGER.debug("End to parse the flea-cache.xml");
        }

        return obj;
    }

    /**
     * <p> 解析flea-cache-config.xml的Digester对象 </p>
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

        digester.addSetNext("flea-cache-config/cache-params", "addCacheParams", CacheParams.class.getName());
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
