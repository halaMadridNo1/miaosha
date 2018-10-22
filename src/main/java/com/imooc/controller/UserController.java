package com.imooc.controller;

import com.imooc.domain.MiaoshaUser;
import com.imooc.redis.RedisService;
import com.imooc.result.Result;
import com.imooc.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ${User} on 2018/10/12
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private MiaoshaUserService miaoshaUserService;
    @Autowired
    RedisService redisService;
    @RequestMapping("/info")
    public Result<MiaoshaUser> info(Model model,MiaoshaUser user){
        return Result.success(user);
    }
}
