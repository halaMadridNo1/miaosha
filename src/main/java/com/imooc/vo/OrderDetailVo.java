package com.imooc.vo;

import com.imooc.domain.OrderInfo;

/**
 * Created by ${User} on 2018/10/21
 */
public class OrderDetailVo  {
    private GoodsVo goods;
    private OrderInfo order;

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }
}
