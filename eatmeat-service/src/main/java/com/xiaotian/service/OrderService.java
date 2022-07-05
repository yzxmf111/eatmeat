package com.xiaotian.service;

import com.xiaotian.pojo.OrderStatus;
import com.xiaotian.pojo.bo.ShopCatBO;
import com.xiaotian.pojo.bo.SubmitOrderBO;
import com.xiaotian.pojo.vo.OrderVO;

import java.util.List;

/**
 * @author xiaotian
 * @description
 * @date 2022-01-23 11:30
 */
public interface OrderService {

    /**
     * 用于创建订单相关信息
     * @param shopCatBOList
     * @param submitOrderBO
     */
    public OrderVO createOrder(List<ShopCatBO> shopCatBOList, SubmitOrderBO submitOrderBO);

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    public void updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
    public OrderStatus queryOrderStatusInfo(String orderId);

    /**
     * 关闭超时未支付订单
     */
    public void closeOrder();
}
