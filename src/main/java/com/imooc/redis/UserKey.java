package com.imooc.redis;

/**
 * Created by ${User} on 2018/10/21
 */
public class UserKey extends BasePrefix {
    public UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
