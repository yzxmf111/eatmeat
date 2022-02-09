package com.xiaotian.service.impl;

import com.xiaotian.enums.OrderStatusEnum;
import com.xiaotian.mapper.OrderStatusMapper;
import com.xiaotian.pojo.OrderStatus;
import com.xiaotian.service.OrderTaskService;
import com.xiaotian.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: Tian
 * @time: 2022/2/9 23:44
 */

@Service
public class OrderTaskServiceImpl implements OrderTaskService {

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Override
    public void closeOrder() {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> list = orderStatusMapper.select(orderStatus);
        list.forEach(
                status -> {
                    int days = DateUtil.daysBetween(DateUtil.getCurrentDateTime(), status.getCreatedTime());
                    if (days > 1) {
                        //将该订单的状态改为关闭状态(50)
                        closeOrderStatus(status);
                    }
                });
    }

    private void closeOrderStatus(OrderStatus status) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(status.getOrderId());
        orderStatus.setOrderStatus(OrderStatusEnum.CLOSE.type);
        orderStatus.setCloseTime(DateUtil.getCurrentDateTime());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }
}
