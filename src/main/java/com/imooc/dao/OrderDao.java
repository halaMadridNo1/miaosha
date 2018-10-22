package com.imooc.dao;

import com.imooc.domain.MiaoshaOrder;
import com.imooc.domain.OrderInfo;
import org.apache.ibatis.annotations.*;

/**
 * Created by ${User} on 2018/10/11
 */
@Mapper
public interface OrderDao {
    @Select("selete * from miaosha_order where User_id=#{userId} and goods_id=#{goodsId}")
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("UserId")long userId,@Param("goodsId")long goodsId);

    @Insert("insert into order_info(user_id,goods_id,goods_name,goods_count,goods_price,order_price,order_channel,status," +
            "create_date)values(#{userId},#{goodsId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate})")
    @SelectKey(keyColumn = "id",keyProperty = "id",resultType = long.class,before = false,statement = "select last_insert_id()")
    public long insert(OrderInfo orderInfo);

    @Insert("insert into miaosha_order (user_id,goods_id,order_id)values(#{userId},#{goodsId},#{orderId})")
    public int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    @Select("select * from order_info where id =#{orderId}")
    public OrderInfo getOrderById(@Param("orderId")long orderId);

    @Delete("delete from order_info")
    public void deleteOrders();

    @Delete("delete form miaosha_order")
    public void deleteMiaoshaOrders();
}
