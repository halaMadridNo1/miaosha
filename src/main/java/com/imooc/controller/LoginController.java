package com.imooc.controller;

import com.imooc.redis.RedisService;
import com.imooc.result.Result;
import com.imooc.service.MiaoshaUserService;
import com.imooc.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by ${User} on 2018/10/22
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoshaUserService userService;
    @Autowired
    RedisService redisService;
    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }
    @RequestMapping("/do_login")
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
        logger.info(loginVo.toString());
        //登录
        String token = userService.login(response, loginVo);
        return Result.success(token);
    }
}
