package com.xiaotian.service;

import com.xiaotian.pojo.User;
import com.xiaotian.pojo.bo.UserBO;

/**
 * @description:
 * @author: yifan.tian
 * @date: 2021-12-02 22:48
 **/
public interface UserService {

    /**
     * 判断用户名是否存在
     */
    public boolean queryUsernameIsExist(String username);

    /**
     * 判断用户名是否存在
     */
    public User createUser(UserBO userBO);

    /**
     * 检索用户名和密码是否匹配，用于登录
     */
    public User queryUserForLogin(String username, String password);

}
