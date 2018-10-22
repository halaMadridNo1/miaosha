package com.imooc.controller;

import com.imooc.access.AccessLimit;
import com.imooc.domain.MiaoshaUser;
import com.imooc.rabbitmq.MQSender;
import com.imooc.rabbitmq.MiaoshaMessage;
import com.imooc.redis.GoodsKey;
import com.imooc.redis.MiaoshaKey;
import com.imooc.redis.OrderKey;
import com.imooc.redis.RedisService;
import com.imooc.result.CodeMsg;
import com.imooc.result.Result;
import com.imooc.service.GoodsService;
import com.imooc.service.MiaoshaService;
import com.imooc.service.MiaoshaUserService;
import com.imooc.service.OrderService;
import com.imooc.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${User} on 2018/10/22
 */
@RestController
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
    @Autowired
    MiaoshaUserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoshaService miaoshaService;
    @Autowired
    MQSender sender;
    private HashMap<Long,Boolean> localOverMap = new HashMap<Long,Boolean>();

    /**
     * 系统初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        if (goodsVos == null){
            return;
        }
        for (GoodsVo goods : goodsVos) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goods.getId(),goods.getStockCount());
            localOverMap.put(goods.getId(),false);
        }
    }
    @GetMapping(value = "/reset")
    public Result<Boolean> reset(Model model){
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        for (GoodsVo goods : goodsVos) {
            goods.setStockCount(10);
            redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goods.getId(),10);
            localOverMap.put(goods.getId(),false);
        }
        redisService.delete(OrderKey.getMiaoshaOrderByUidGid);
        redisService.delete(MiaoshaKey.isGoodsOver);
        miaoshaService.reset(goodsVos);
        return Result.success(true);
    }
    @PostMapping(value = "/{path}/do_miaosha")
    public Result<Integer> miaosha(Model model, MiaoshaUser user,
                                   @RequestParam("goodsId")long goodsId,
                                   @PathVariable("path")String path){
            model.addAttribute("user",user);
            if (user == null){
                return Result.error(CodeMsg.SESSION_ERROR);
            }
            //验证path
        boolean check = miaoshaService.checkPath(user, goodsId, path);
            if (!check){
                return Result.error(CodeMsg.REQUEST_ILLEGAL);
            }
            //内存标记,减少redis访问
        Boolean over = localOverMap.get(goodsId);
        if (over){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //库存预减
        Long decr = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if (decr <0){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到
        MiaoshaUser order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //入队
        MiaoshaMessage message = new MiaoshaMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);
        sender.sendMiaoshaMessage(message);
        return Result.success(0);
    }

    /**
     * 秒杀是否成功
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @GetMapping(value = "/result")
    public Result<Long> miaoshaResult(Model model,MiaoshaUser user,
                                      @RequestParam("goodsId")long goodsId){
        model.addAttribute("user",user);
        if (user ==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }
    @AccessLimit(seconds = 5,maxCount = 5,needLogin = true)
    @GetMapping(value = "/psth")
    public Result<String> getMiaoshaPath(HttpServletRequest request,MiaoshaUser user,
                                         @RequestParam("goodsId")long goodsId,
                                         @RequestParam(value = "verifyCode",defaultValue = "0")int verifyCode){
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        boolean code = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if (!code){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path = miaoshaService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }
    @GetMapping(value = "/verifyCode")
    public Result<String> getMiaoshaVerifyCode(HttpServletResponse response, MiaoshaUser user,
                                               @RequestParam("goodsId")long goodsId){
        if (user ==null){
            return  Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage code = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream stream = response.getOutputStream();
            ImageIO.write(code,"JPEG",stream);
            stream.flush();
            stream.close();
            return  null;
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }

}
