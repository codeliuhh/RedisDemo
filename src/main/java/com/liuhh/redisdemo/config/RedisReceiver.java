package com.liuhh.redisdemo.config;

import org.springframework.stereotype.Component;

/**
 * @Author: liuhh
 * @Date: 2022/9/12
 */
@Component
public class RedisReceiver {
    public void receiverMessage(Object object){
        System.out.println(object.toString());
    }
}
