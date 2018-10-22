package com.imooc.rabbitmq;

import com.imooc.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ${User} on 2018/10/23
 */
@Service
public class MQSender {
    private static Logger log= LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendMiaoshaMessage(MiaoshaMessage mm){
        String str = RedisService.beanToString(mm);
        log.info(str);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE,str);

    }
}
