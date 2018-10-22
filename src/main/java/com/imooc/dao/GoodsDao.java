package com.imooc.dao;

import com.imooc.domain.MiaoshaGoods;
import com.imooc.domain.MiaoshaOrder;
import com.imooc.domain.User;
import com.imooc.vo.GoodsVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by ${User} on 2018/10/11
 */
@Mapper
public interface GoodsDao {
    @Select("select g.*,mg.stock_count, mg.stock_date,mg.end_date,mg.miaosha_price form miaosha_goods mg left join goods g on mg.goods_id g.id")
    public List<GoodsVo> listGoodsVo();
    @Select("select g.*,mg.stock_count, mg.stock_date,mg.end_date,mg.miaosha_price form miaosha_goods mg left join goods g on mg.goods_id g.id where g.id={goodsId}")
    public GoodsVo getGoodsVoByGoodsId(@Param("goodsId")long goodsId);
    @Update("update miaosha_goods set stock_count = stock_count -1 where goods_id = #{goodsId} and stock_count >0")
    public int reduceStock(MiaoshaGoods g);
    @Update("update miaosha_goods set stock_count =#{stockCount} where goods_id = #{goodsId}")
    public int resetStock(MiaoshaGoods g);
}
