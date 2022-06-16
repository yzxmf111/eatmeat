package com.xiaotian.service.impl.usercenter;

import com.xiaotian.mapper.UserMapper;
import com.xiaotian.pojo.User;
import com.xiaotian.service.usercenter.UserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @author xiaotian
 * @description
 * @date 2022-02-28 23:17
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserMapper userMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User queryUserInfo(String userId) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", userId);
        User user = userMapper.selectOneByExample(example);
        user.setId("");
        user.setPassword("");
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public User updateUserInfo(User user, String id) {
        User updateUser = new User();
        BeanUtils.copyProperties(user, updateUser);
        updateUser.setId(id);
        updateUser.setUpdatedTime(new Date());
        userMapper.updateByPrimaryKeySelective(updateUser);
        return queryUserInfo(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public User updateUserFace(String userId, String userFaceUrl) {
        User user = queryUserInfo(userId);
        user.setUpdatedTime(new Date());
        user.setFace(userFaceUrl);
        userMapper.updateByPrimaryKeySelective(user);
        return queryUserInfo(userId);
    }
}
