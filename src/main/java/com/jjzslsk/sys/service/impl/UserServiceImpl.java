package com.jjzslsk.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jjzslsk.sys.entity.User;
import com.jjzslsk.sys.mapper.UserMapper;
import com.jjzslsk.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 *  实现serve定义的接口
 * </p>
 *
 * @author baomidou
 * @since 2023-06-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> login(User user) {
        //登录逻辑
        //使用LambdaQueryWrapper 泛型操作 实体User
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        wrapper.eq(User::getPassword,user.getPassword());

        //将定义好的 wrapper条件传入 selectOne
        User loginUser = this.baseMapper.selectOne(wrapper);
        //结果不为空，则生成token，并将用户信息存入redis
        if(loginUser != null){
            //暂时用UUID,终究方案是JWT代替
            String key = "user:" + UUID.randomUUID();
            //存入redis
            loginUser.setPassword(null);
            //30分钟时效
            redisTemplate.opsForValue().set(key, loginUser, 30, TimeUnit.MINUTES);
            //返回数据
            Map<String,Object> data = new HashMap<>();
            data.put("token",key);
            return data;
        }

        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        //获取到的 obj 是 HASHCODE 字符串，需要toJSONString转化成json字符串
        Object obj = redisTemplate.opsForValue().get(token);
        if(obj != null){
            //然后再使用fastjson2的parseObject把json字符串反序列化成 能匹配User的对象
            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
            Map<String, Object> data = new HashMap<>();
            data.put("name",loginUser.getUsername());
            data.put("avatar",loginUser.getAvatar());
            System.out.println(data);
            return data;
        }
        return null;
    }
}
