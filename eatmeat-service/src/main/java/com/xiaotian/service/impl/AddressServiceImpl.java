package com.xiaotian.service.impl;

import com.xiaotian.enums.YesOrNo;
import com.xiaotian.mapper.UserAddressMapper;
import com.xiaotian.pojo.UserAddress;
import com.xiaotian.pojo.bo.AddressBO;
import com.xiaotian.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @description: 地址信息接口
 * @author: yifan.tian
 * @date: 2021-12-03 10:49
 **/
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> list(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        List<UserAddress> list = userAddressMapper.select(userAddress);
        return list;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addAddress(AddressBO addressBO) {
        // 1. 判断当前用户是否存在地址，如果没有，则新增为‘默认地址’
        Integer count = this.selectAddress(addressBO.getUserId());
        Integer isDefault = 0;
        if (count == 0) {
            isDefault = 1;
        }
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, userAddress);
        String id = sid.nextShort();
        userAddress.setId(id);
        userAddress.setExtand("");
        userAddress.setIsDefault(isDefault);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());
        userAddressMapper.insertSelective(userAddress);
    }

    private int selectAddress(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.selectCount(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void updateAddress(AddressBO addressBO) {
        String addressId = addressBO.getAddressId();
        UserAddress pendingAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, pendingAddress);
        pendingAddress.setId(addressId);
        pendingAddress.setUpdatedTime(new Date());
        userAddressMapper.updateByPrimaryKeySelective(pendingAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void deleteAddress(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(addressId);
        userAddress.setUserId(userId);
         userAddressMapper.deleteByPrimaryKey(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void setDefault(String userId, String addressId) {
        //1：修改老的
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        //可能有多个默认地址
        userAddress.setIsDefault(YesOrNo.YES.type);
        List<UserAddress> oldAddress = userAddressMapper.select(userAddress);
        for (UserAddress address : oldAddress) {
            if (address.getIsDefault() == 1) {
                address.setIsDefault(YesOrNo.NO.type);
                userAddressMapper.updateByPrimaryKeySelective(address);
            }
        }

        //2：设置新的
        UserAddress newUserAddress = new UserAddress();
        newUserAddress.setUserId(userId);
        newUserAddress.setId(addressId);
        newUserAddress.setIsDefault(YesOrNo.YES.type);
        userAddressMapper.updateByPrimaryKeySelective(newUserAddress);
    }
}
