package com.xiaotian.service;

import com.xiaotian.pojo.UserAddress;
import com.xiaotian.pojo.bo.AddressBO;

import java.util.List;

/**
 * @description: 地址信息接口
 * @author: yifan.tian
 * @date: 2021-12-03 10:49
 **/
public interface AddressService {

    /**
     * 获取所有的收货地址
     *
     * @param userId
     * @return
     */
    List<UserAddress> list(String userId);

    /**
     * 增加收货地址
     * @param addressBO
     */
    void addAddress(AddressBO addressBO);

    /**
     * 更新收货地址
     * @param addressBO
     */
    void updateAddress(AddressBO addressBO);

    /**
     * 修改收货地址
     * @param addressId
     */
    void deleteAddress(String userId, String addressId);

    /**
     * 修改收货地址
     * @param userId,addressId
     */
    void setDefault(String userId, String addressId);
}
