package com.imooc.util;

import java.util.UUID;

/**
 * Created by ${User} on 2018/10/21
 */
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
