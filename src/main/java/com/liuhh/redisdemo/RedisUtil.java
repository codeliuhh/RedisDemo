package com.liuhh.redisdemo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Author: liuhh
 * @Date: 2022/5/10
 */
@Data
@Component
public class RedisUtil {
    @Value("${spring.redis.host}")
    private String ip;
    @Value("${spring.redis.port}")
    private Integer port;
    @Value("${spring.redis.jedis.pool.max-active}")
    private Integer maxActive;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private Integer maxIdle;

}
