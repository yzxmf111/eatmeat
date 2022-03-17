package com.xiaotian.service.usercenter;

import com.xiaotian.pojo.User;

/**
 * @author xiaotian
 * @description 用户信息相关接口
 * @date 2022-02-28 23:14
 */
public interface UserInfoService {

    User queryUserInfo(String userId);

    Boolean updateUserInfo(User user, String id);
}
