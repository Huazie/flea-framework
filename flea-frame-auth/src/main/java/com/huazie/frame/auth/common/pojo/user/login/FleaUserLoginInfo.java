package com.huazie.frame.auth.common.pojo.user.login;

import com.huazie.frame.common.pojo.FleaCommonPOJO;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p> Flea用户登录信息 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaUserLoginInfo implements FleaCommonPOJO {

    private static final long serialVersionUID = -4379336461725022815L;

    private String accountCode; // 账号

    private String accountPwd; // 密码

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountPwd() {
        return accountPwd;
    }

    public void setAccountPwd(String accountPwd) {
        this.accountPwd = accountPwd;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
