package com.imooc.service;

import com.imooc.dao.GoodsDao;
import com.imooc.domain.Goods;
import com.imooc.domain.MiaoshaGoods;
import com.imooc.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ${User} on 2018/10/21
 */
@Service
public class GoodsService {
    @Autowired
    private GoodsDao goodsDao;
    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }
    public GoodsVo getGoodsVoByGoodsId(long goodsId){
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }
    public boolean reduceStock(GoodsVo goods){
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        int i = goodsDao.reduceStock(g);
        return i > 0;
    }

    public void resetStock(List<GoodsVo> goodsList){
        for (GoodsVo goods : goodsList) {
            MiaoshaGoods g = new MiaoshaGoods();
            g.setGoodsId(goods.getId());
            g.setStockCount(goods.getStockCount());
            goodsDao.resetStock(g);
        }
    }
}
