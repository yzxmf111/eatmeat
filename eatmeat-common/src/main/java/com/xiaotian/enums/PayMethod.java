package com.xiaotian.enums;

/**
 * @author xiaotian
 * @description
 * @date 2022-01-23 11:49
 */
public enum PayMethod {

    WEIXIN(1, "微信"),
    ALIPAY(2, "支付宝");

    public final Integer type;
    public final String value;

    PayMethod(Integer type, String value){
        this.type = type;
        this.value = value;
    }
}
