package com.huazie.frame.common.i18n;

import com.huazie.frame.common.i18n.config.FleaI18nConfig;
import com.huazie.frame.common.util.StringUtils;
import com.huazie.frame.common.util.UnicodeUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * @author huazie
 * @version 1.0.0
 */
public class FleaI18NTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(FleaI18NTest.class);

    @Test
    public void fleaI18NConfigTest() {
        FleaI18nConfig config = FleaI18nConfig.getConfig();
        try {
            config.getI18NData("ERROR0000000001", "error", Locale.US);
        } catch (Exception e) {
            LOGGER.error("Exception={}", e);
        }
    }

    @Test
    public void fleaI18NHelperTest() {
        try {
            FleaI18nHelper.i18n("ERROR0000000001", "error", Locale.US);
        } catch (Exception e) {
            LOGGER.error("Exception={}", e);
        }
    }

    @Test
    public void nativeToUnicodeTest() {
        try {
            LOGGER.debug(UnicodeUtils.nativeToUnicode("你好，我是huazie"));
        } catch (Exception e) {
            LOGGER.error("Exception={}", e);
        }
    }

    @Test
    public void unicodeToNativeTest() {
        try {
            LOGGER.debug(UnicodeUtils.unicodeToNative("\\u68\\u75\\u61\\u7a\\u69\\u65"));
        } catch (Exception e) {
            LOGGER.error("Exception={}", e);
        }
    }

    @Test
    public void i18nTest() {
        String str = "hello, My name is {0}, I am from {1}";
        StringBuilder strBuilder = new StringBuilder(str);
        try {
            StringUtils.replace(strBuilder, "{" + 0 + "}", "huazie");
            StringUtils.replace(strBuilder, "{" + 1 + "}", "china");
            LOGGER.debug(strBuilder.toString());
        } catch (Exception e) {
            LOGGER.error("Exception={}", e);
        }
    }
}
