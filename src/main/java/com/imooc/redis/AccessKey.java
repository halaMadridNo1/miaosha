package com.imooc.redis;

/**
 * Created by ${User} on 2018/10/21
 */
public class AccessKey extends BasePrefix {
    public static AccessKey withExpire(int expireSeconds) {
        return new AccessKey(expireSeconds,"access");
    }

    private AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
