package com.huazie.frame.common;

/**
 * <p> 实体状态枚举 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public enum EntityStateEnum {

    IN_USE(1, "在用状态"),
    BE_DELETED(0, "删除状态");

    private int state;

    private String desc;

    EntityStateEnum(int state, String desc) {
        this.state = state;
        this.desc = desc;
    }

    public int getState() {
        return state;
    }

    public String getDesc() {
        return desc;
    }
}
