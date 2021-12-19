package com.xiaotian.enums;


public enum ErrorEnum {
    PARAM_ERROR(4001, "参数不能为空"),
    ;

    public final Integer code;
    public final String desc;

    ErrorEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
