package com.huazie.frame.db.jpa.common;

import com.huazie.frame.common.FleaConfigManager;
import com.huazie.frame.common.config.ConfigItem;
import com.huazie.frame.common.config.ConfigItems;
import com.huazie.frame.common.pool.FleaObjectPoolConfig;
import com.huazie.frame.common.util.ObjectUtils;
import com.huazie.frame.db.common.DBConstants;

/**
 * <p> Flea JPA查询对象池配置 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaJPAQueryPoolConfig extends FleaObjectPoolConfig {

    private static volatile FleaJPAQueryPoolConfig config;

    /**
     * <p> 无参构造方法，初始化部分默认配置 </p>
     *
     * @since 1.0.0
     */
    private FleaJPAQueryPoolConfig() {
        super();
    }

    /**
     * <p> 获取Flea JPA查询对象池配置实例 </p>
     *
     * @return Flea JPA查询对象池配置实例
     * @since 1.0.0
     */
    public static FleaJPAQueryPoolConfig getConfig() {
        if (ObjectUtils.isEmpty(config)) {
            synchronized (FleaJPAQueryPoolConfig.class) {
                if (ObjectUtils.isEmpty(config)) {
                    config = new FleaJPAQueryPoolConfig();
                    initConfig();
                }
            }
        }
        return config;
    }

    /**
     * <p> 初始化对象池配置 </p>
     *
     * @since 1.0.0
     */
    private static void initConfig() {
        // 从flea-config.xml配置中获取对象池配置信息
        ConfigItems configItems = FleaConfigManager.getConfigItems(DBConstants.JPAQueryPoolConfigConstants.FLEA_JPA_QUERY);
        // Flea JPA查询对象池最大连接数
        ConfigItem maxTotalConfigItem = FleaConfigManager.getConfigItem(DBConstants.JPAQueryPoolConfigConstants.JPA_QUERY_POOL_MAXTOTAL, configItems);
        if (ObjectUtils.isNotEmpty(maxTotalConfigItem)) {
            config.setMaxTotal(Integer.parseInt(maxTotalConfigItem.getValue()));
        }
        // Flea JPA查询对象池最大空闲连接数
        ConfigItem maxIdleConfigItem = FleaConfigManager.getConfigItem(DBConstants.JPAQueryPoolConfigConstants.JPA_QUERY_POOL_MAXIDLE, configItems);
        if (ObjectUtils.isNotEmpty(maxIdleConfigItem)) {
            config.setMaxIdle(Integer.parseInt(maxIdleConfigItem.getValue()));
        }
        // Flea JPA查询对象池最小空闲连接数
        ConfigItem minIdleConfigItem = FleaConfigManager.getConfigItem(DBConstants.JPAQueryPoolConfigConstants.JPA_QUERY_POOL_MINIDLE, configItems);
        if (ObjectUtils.isNotEmpty(minIdleConfigItem)) {
            config.setMinIdle(Integer.parseInt(minIdleConfigItem.getValue()));
        }
        // Flea JPA查询对象池获取连接时的最大等待毫秒数
        ConfigItem maxWaitMillisConfigItem = FleaConfigManager.getConfigItem(DBConstants.JPAQueryPoolConfigConstants.JPA_QUERY_POOL_MAXWAITMILLIS, configItems);
        if (ObjectUtils.isNotEmpty(maxWaitMillisConfigItem)) {
            config.setMaxWaitMillis(Integer.parseInt(maxWaitMillisConfigItem.getValue()));
        }
    }
}
