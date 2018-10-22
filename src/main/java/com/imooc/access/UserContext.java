package com.imooc.access;

import com.imooc.domain.MiaoshaUser;

/**
 * Created by ${User} on 2018/10/21
 */
public class UserContext {
    private static ThreadLocal<MiaoshaUser> userHolder = new ThreadLocal<MiaoshaUser>();

    public static void setUser(MiaoshaUser user){
        userHolder.set(user);
    }
    public static MiaoshaUser getUser(){
        return userHolder.get();
    }
}
