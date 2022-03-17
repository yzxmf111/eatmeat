package com.xiaotian.service.impl.usercenter;

import com.xiaotian.mapper.UserMapper;
import com.xiaotian.pojo.User;
import com.xiaotian.service.usercenter.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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
    public Boolean updateUserInfo(User user, String id) {
        user.setId(id);
        userMapper.updateByPrimaryKey(user);
        return true;
    }
}
