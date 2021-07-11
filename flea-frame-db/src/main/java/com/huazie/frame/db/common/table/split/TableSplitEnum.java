package com.huazie.frame.db.common.table.split;

/**
 * <p> 分表类型枚举 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public enum TableSplitEnum {

    TWOHEX("twohex", "com.huazie.frame.db.common.table.split.impl.TwoHexTableSplitImpl", "按两位十六进制分表，截取分表字段后2位字符，并以小写返回"),
    TWOHEXB("twohexb", "com.huazie.frame.db.common.table.split.impl.TwoHexBeforeTableSplitImpl", "按两位十六进制分表，截取分表字段前2位字符，并以小写返回"),
    TWOHEXU("twohexu", "com.huazie.frame.db.common.table.split.impl.TwoHexUpperTableSplitImpl", "按两位十六进制分表，截取分表字段后2位字符，并以大写返回"),
    TWOHEXBU("twohexbu", "com.huazie.frame.db.common.table.split.impl.TwoHexBeforeUpperTableSplitImpl", "按两位十六进制分表，截取分表字段前2位字符，并以大写返回"),
    YYYY("yyyy", "com.huazie.frame.db.common.table.split.impl.YYYYTableSplitImpl", "按年分表"),
    YYYYMM("yyyymm", "com.huazie.frame.db.common.table.split.impl.YYYYMMTableSplitImpl", "按年月分表"),
    YYYYMMDD("yyyymmdd", "com.huazie.frame.db.common.table.split.impl.YYYYMMDDTableSplitImpl", "按年月日分表");

    private String key;         // 分表类型关键字
    private String implClass;   // 分表类型实现类
    private String desc;        // 分表类型描述

    TableSplitEnum(String key, String implClass, String desc) {
        this.key = key;
        this.implClass = implClass;
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public String getImplClass() {
        return implClass;
    }

    public String getDesc() {
        return desc;
    }

}
