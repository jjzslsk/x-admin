package com.jjzslsk.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjzslsk.common.vo.Result;
import com.jjzslsk.sys.entity.User;
import com.jjzslsk.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-06-30
 */
//Controller 返回的是视图 适用于springMVC，直接访问网页会报404
//@Controller
//有别于
//RestController 返回的是json数据
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    //注入service层
    private IUserService userService;

    @GetMapping("/all")
    private Result<List <User>> getAllUser(){
        List<User> list = userService.list();
        return Result.success("查询成功",list);
    }

    @PostMapping("/login")
    //@RequestBody 转换前端传过来的json字符串，来适配User user 这个类
    private Result<Map<String,Object>> login(@RequestBody User user){
        //登录逻辑在Serve里, ctrl + ent 增加login 接口方法 并去impl实现该方法
        Map<String,Object> data = userService.login(user);
        if (data != null){
            return Result.success("登录成功",data);
        }
        return Result.fail(201,"用户名或者密码错误");
    }

    @GetMapping("/info")
    //@RequestParam 可以直接拿到url 参数字符串
    public Result<Map<String, Object>> getUserInfo(@RequestParam("token") String token){
        //根据token 获取用户信息。redis
        Map<String, Object> data = userService.getUserInfo(token);
        System.out.println(data);
        if (data != null){
            return Result.success("查询成功",data);
        }
        return Result.fail(201,"登录信息无效，请重新登录");
    }

    @PostMapping("logout")
    public Result<?> logout(@RequestHeader("X-Token") String token){
        userService.logout(token);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<Map<String,Object>> getUserList(@RequestParam(value = "username",required = false) String username,
                                                  @RequestParam(value = "phone",required = false) String phone,
                                                  @RequestParam(value = "pageNo") Long pageNo,
                                                  @RequestParam(value = "pageSize") Long pageSize){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(username),User::getUsername,username);
        wrapper.eq(StringUtils.hasLength(phone),User::getPhone,phone);
        wrapper.orderByDesc(User::getId);

        Page<User> page = new Page<>(pageNo,pageSize);
        userService.page(page,wrapper);

        Map<String,Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());

        return Result.success(data);

    }
}
