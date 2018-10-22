package com.imooc.redis;

/**
 * Created by ${User} on 2018/10/19
 */
public interface KeyPrefix {
    public int expireSeconds();
    public String getPrefix();
}
