package com.imooc.rabbitmq;

import com.imooc.domain.Goods;
import com.imooc.domain.MiaoshaUser;

/**
 * Created by ${User} on 2018/10/22
 */
public class MiaoshaMessage {
    private MiaoshaUser user;
    private long goodsId;

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
