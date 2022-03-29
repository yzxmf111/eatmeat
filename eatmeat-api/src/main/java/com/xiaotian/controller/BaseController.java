package com.xiaotian.controller;

import java.io.File;

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
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";        // produce

    // 微信支付成功 -> 支付中心 -> 天天吃货平台
    //                       |-> 回调通知的url -->修改订单状态
    String payReturnUrl = "http://qkihbq.natappfree.cc/orders/notifyMerchantOrderPaid";
    //为了兼容win、mac、linux的"/" ， 但是最好改成可配置的
    public static final String USER_FACE_FILE_PATH = File.separator + "Users" + File.separator + "xiaotian" + File.separator
                                                    + "Downloads" + File.separator + "日常文档" + File.separator + "image"
                                                    + File.separator + "userface";

}

