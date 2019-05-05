package com.huazie.frame.cache;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huazie.frame.cache.memcached.MemcachedFleaCacheManager;
import com.huazie.frame.cache.memcached.config.MemcachedConfig;

import java.util.Date;

/**
 * @author huazie
 * @version v1.0.0
 * @date 2018年1月22日
 */
public class FleaCacheTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(FleaCacheTest.class);

    @Test
    public void testFleaCache() {

        MemcachedFleaCacheManager manager = MemcachedFleaCacheManager.getInstance();

        AbstractFleaCache cache = manager.getCache("fleaparadetail");
        LOGGER.debug("Cache={}", cache);

        //#### 1.  简单字符串
//		cache.put("menu1", "huazie");
        cache.get("menu1");
//        cache.delete("menu1");
        LOGGER.debug(cache.getCacheName() + ">>>" + cache.getCacheDesc());
    }

    @Test
    public void testProperties() throws Exception {
        MemcachedConfig config = MemcachedConfig.getConfig();
        LOGGER.debug(config.toString());
    }

    @Test
    public void testMemcachedExpire() throws Exception {
        Date date = new Date(1000);
        long aa = date.getTime() / 1000;
        LOGGER.debug("huazie:{}", aa);
    }
}
