package com.xiaotian.enums;


public enum ErrorEnum {
    PARAM_ERROR(4001, "参数不能为空"),
    ADDRESS_ID_PARAM_ERROR(4002, "修改地址错误：addressId不能为空"),
    ;

    public final Integer code;
    public final String desc;

    ErrorEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
