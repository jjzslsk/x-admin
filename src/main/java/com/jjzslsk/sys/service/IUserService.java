package com.jjzslsk.sys.service;

import com.jjzslsk.sys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 *  继承了MYbatis 的 IService
 *  定义接口，需要impl 实现该接口
 * </p>
 *
 * @author baomidou
 * @since 2023-06-30
 */
public interface IUserService extends IService<User> {

    Map<String, Object> login(User user);

    Map<String, Object> getUserInfo(String token);

    void logout(String token);
}
