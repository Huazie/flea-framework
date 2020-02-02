package com.huazie.frame.jersey.client.core;

import com.huazie.frame.common.FleaConfigManager;
import com.huazie.frame.jersey.common.FleaJerseyConstants;

/**
 * <p> Flea Jersey 客户端配置工具类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaJerseyClientConfig {

    /**
     * <p> 获取系统账户编号 </p>
     *
     * @return 系统账户编号
     * @since 1.0.0
     */
    public static String getSystemAcctId() {
        return FleaConfigManager.getConfigItemValue(
                FleaJerseyConstants.JerseyClientConstants.CONFIG_ITEMS_KEY,
                FleaJerseyConstants.JerseyClientConstants.CONFIG_ITEM_SYSTEM_ACCT_ID);
    }

    /**
     * <p> 获取系统账户密码 </p>
     *
     * @return 系统账户密码
     * @since 1.0.0
     */
    public static String getSystemAcctPwd() {
        return FleaConfigManager.getConfigItemValue(
                FleaJerseyConstants.JerseyClientConstants.CONFIG_ITEMS_KEY,
                FleaJerseyConstants.JerseyClientConstants.CONFIG_ITEM_SYSTEM_ACCT_PWD);
    }

}
