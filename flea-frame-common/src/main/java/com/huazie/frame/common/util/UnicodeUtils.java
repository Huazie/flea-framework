package com.huazie.frame.common.util;

/**
 * <p> Unicode工具类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class UnicodeUtils {

    private static final int LENGTH_OF_SINGLE_UNICODE = 4;
    private static final String COMPLEMENT_UNICODE_STRING = "0";

    /**
     * <p> 本地字符串转换为Unicode字符串 </p>
     *
     * @param nativeStr 本地字符串
     * @return Unicode字符串
     * @since 1.0.0
     */
    public static String nativeToUnicode(String nativeStr) {
        StringBuilder strBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(nativeStr)) {
            for (int i = 0; i < nativeStr.length(); i++) {
                char ch = nativeStr.charAt(i);
                String hex = Integer.toHexString(ch);
                if (StringUtils.isNotBlank(hex)) {
                    int len = hex.length();
                    if (len < LENGTH_OF_SINGLE_UNICODE) {
                        for (int j = 0; j < LENGTH_OF_SINGLE_UNICODE - len; j++) {
                            hex = COMPLEMENT_UNICODE_STRING + hex;
                        }
                    }
                    strBuilder.append("\\u").append(hex);
                }
            }
        }
        return strBuilder.toString();
    }

    /**
     * <p> Unicode字符串转换为本地字符串 </p>
     *
     * @param unicodeStr unicode字符串
     * @return 本地字符串
     * @since 1.0.0
     */
    public static String unicodeToNative(String unicodeStr) {
        StringBuilder strBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(unicodeStr)) {
            String[] unicodeArr = unicodeStr.split("\\\\u");
            if (ArrayUtils.isNotEmpty(unicodeArr)) {
                for (int i = 0; i < unicodeArr.length; i++) {
                    String unicode = unicodeArr[i];
                    if (StringUtils.isNotBlank(unicode)) {
                        int unicodeInt = Integer.valueOf(unicodeArr[i], 16);
                        strBuilder.append((char) unicodeInt);
                    }
                }
            }
        }
        return strBuilder.toString();
    }

    /**
     * <p> 本地文件转成Unicode文件 </p>
     *
     * @param exePath 可执行文件路径（native2ascii.exe）
     * @param nativeFilePath  本地文件路径
     * @param unicodeFilePath Unicode文件路径
     * @throws Exception
     * @since 1.0.0
     */
    public static void fileNativeToUnicode(String exePath, String charset, String nativeFilePath, String unicodeFilePath) throws Exception {
        String[] cmd = {exePath, "-encoding", charset, nativeFilePath, unicodeFilePath};
        Runtime.getRuntime().exec(cmd);
    }

    /**
     * <p> Unicode文件转成本地文件 </p>
     *
     * @param exePath 可执行文件路径（native2ascii.exe）
     * @param nativeFilePath  本地文件路径
     * @param unicodeFilePath Unicode文件路径
     * @throws Exception
     * @since 1.0.0
     */
    public static void fileUnicodeToNative(String exePath, String unicodeFilePath, String nativeFilePath) throws Exception {
        String[] cmd = {exePath, "-reverse", unicodeFilePath, nativeFilePath};
        Runtime.getRuntime().exec(cmd);
    }

}
