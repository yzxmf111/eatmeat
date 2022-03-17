package com.xiaotian.enums;

/**
 * @author xiaotian
 * @description
 * @date 2022-02-28 23:29
 */
public enum ErrorMessage {

    PARAM_ERROR(5001,"参数错误"),
    ;

    private Integer code;
    private String message;

    ErrorMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
