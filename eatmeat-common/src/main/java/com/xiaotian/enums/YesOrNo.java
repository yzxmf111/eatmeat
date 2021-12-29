package com.xiaotian.enums;

/**
 * @Desc: 性别 枚举
 */
public enum YesOrNo {
    YES(1, "是"),
    NO(0, "否");

    public final Integer type;
    public final String value;

    YesOrNo(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
