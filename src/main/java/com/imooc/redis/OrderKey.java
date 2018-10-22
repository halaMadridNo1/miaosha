package com.imooc.redis;

/**
 * Created by ${User} on 2018/10/21
 */
public class OrderKey extends BasePrefix {
    public OrderKey(String prefix) {
        super( prefix);
    }
    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey("moug");
}
