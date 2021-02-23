package com.huazie.frame.common.exception;

import com.huazie.frame.common.FleaFrameManager;
import com.huazie.frame.common.i18n.FleaI18nHelper;
import com.huazie.frame.common.i18n.FleaI18nResEnum;
import com.huazie.frame.common.util.ArrayUtils;
import com.huazie.frame.common.util.ObjectUtils;

import java.util.Locale;

/**
 * <p> Flea I18N 通用异常 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class CommonException extends Exception {

    private static final long serialVersionUID = 1746312829236028651L;

    private String key;                     // 国际化资源数据关键字

    private Locale locale;                  // 国际化区域标识

    private FleaI18nResEnum i18nResEnum;    // 国际化资源类型

    public CommonException(String mKey, FleaI18nResEnum mI18nResEnum) {
        // 使用服务器当前默认的国际化区域设置
        this(mKey, mI18nResEnum, FleaFrameManager.getManager().getLocale());
    }

    public CommonException(String mKey, FleaI18nResEnum mI18nResEnum, String... mValues) {
        // 使用服务器当前默认的国际化区域设置
        this(mKey, mI18nResEnum, FleaFrameManager.getManager().getLocale(), mValues);
    }

    public CommonException(String mKey, FleaI18nResEnum mI18nResEnum, Locale mLocale) {
        // 使用指定的国际化区域设置
        this(mKey, mI18nResEnum, mLocale, new String[]{});
    }

    public CommonException(String mKey, FleaI18nResEnum mI18nResEnum, Locale mLocale, String... mValues) {
        // 使用指定的国际化区域设置
        super(convert(mKey, mValues, mI18nResEnum, mLocale));
        key = mKey;
        locale = mLocale;
        i18nResEnum = mI18nResEnum;
    }

    public CommonException(String mKey, FleaI18nResEnum mI18nResEnum, Throwable cause) {
        // 使用服务器当前默认的国际化区域设置
        this(mKey, mI18nResEnum, FleaFrameManager.getManager().getLocale(), cause);
    }

    public CommonException(String mKey, FleaI18nResEnum mI18nResEnum, Throwable cause, String... mValues) {
        // 使用服务器当前默认的国际化区域设置
        this(mKey, mI18nResEnum, FleaFrameManager.getManager().getLocale(), cause, mValues);
    }

    public CommonException(String mKey, FleaI18nResEnum mI18nResEnum, Locale mLocale, Throwable cause) {
        // 使用指定的国际化区域设置
        this(mKey, mI18nResEnum, mLocale, cause, new String[]{});
    }

    public CommonException(String mKey, FleaI18nResEnum mI18nResEnum, Locale mLocale, Throwable cause, String... mValues) {
        // 使用指定的国际化区域设置
        super(convert(mKey, mValues, mI18nResEnum, mLocale), cause);
        key = mKey;
        locale = mLocale;
        i18nResEnum = mI18nResEnum;
    }

    private static String convert(String key, String[] values, FleaI18nResEnum i18nResEnum, Locale locale) {
        if (ObjectUtils.isEmpty(locale)) {
            locale = FleaFrameManager.getManager().getLocale(); // 使用服务器当前默认的国际化区域设置
        }
        if (ArrayUtils.isNotEmpty(values) && ObjectUtils.isNotEmpty(i18nResEnum)) {
            return FleaI18nHelper.i18n(key, values, i18nResEnum.getResName(), locale);
        } else {
            return FleaI18nHelper.i18n(key, i18nResEnum.getResName(), locale);
        }
    }

    public String getKey() {
        return key;
    }

    public Locale getLocale() {
        return locale;
    }

    public FleaI18nResEnum getI18nResEnum() {
        return i18nResEnum;
    }
}
