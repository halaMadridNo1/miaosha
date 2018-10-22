package com.imooc.access;

import com.alibaba.fastjson.JSON;
import com.imooc.domain.MiaoshaUser;
import com.imooc.redis.AccessKey;
import com.imooc.redis.RedisService;
import com.imooc.result.CodeMsg;
import com.imooc.result.Result;
import com.imooc.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ${User} on 2018/10/21
 */
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter{

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    RedisService redisService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod){
            MiaoshaUser user = getUser(request, response);
            UserContext.setUser(user);
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit limit = hm.getMethodAnnotation(AccessLimit.class);
            if(limit == null){
                return true;
            }
            int seconds = limit.seconds();
            int maxCount = limit.maxCount();
            boolean login = limit.needLogin();
            String uri = request.getRequestURI();
            if (login) {
                if (user == null) {
                    render(response, CodeMsg.SERVER_ERROR);
                    return false;
                }
                uri+="_"+user.getId();
            }else {

            }
            AccessKey key = AccessKey.withExpire(seconds);
            Integer count = redisService.get(key, uri, Integer.class);
            if (count == null){
                redisService.get(key,uri,1);
            }else if (count < maxCount){
                    redisService.incr(key,uri);
            }else {
                render(response,CodeMsg.ACCESS_LIMIT_REACHED);
                return false;
            }

        }
        return true;
    }

    private void render(HttpServletResponse response, CodeMsg cm) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
    private MiaoshaUser getUser(HttpServletRequest request,HttpServletResponse response){
        String parameter = request.getParameter(MiaoshaUserService.COOKI_NAME_TOKEN);
        String cookieValue = getCookieValue(request, MiaoshaUserService.COOKI_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieValue) && StringUtils.isEmpty(parameter)){
            return null;
        }
        String token = StringUtils.isEmpty(parameter) ? cookieValue : parameter;
        return  miaoshaUserService.getByToken(response,token);
    }
    private String getCookieValue(HttpServletRequest request,String cookiName){
        Cookie[] cookies = request.getCookies();
        if(cookies == null || cookies.length <= 0){
            return null;
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals(cookiName)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
