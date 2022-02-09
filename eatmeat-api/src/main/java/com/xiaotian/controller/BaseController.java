package com.xiaotian.controller;

/**
 * @author xiaotian
 * @description
 * @date 2022-01-23 11:47
 */
public class BaseController {


    public static final String FOODIE_SHOPCART = "shopcart";

    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    // 支付中心的调用地址
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";		// produce

    // 微信支付成功 -> 支付中心 -> 天天吃货平台
    //                       |-> 回调通知的url -->修改订单状态
    String payReturnUrl = "http://qkihbq.natappfree.cc/orders/notifyMerchantOrderPaid";

}
