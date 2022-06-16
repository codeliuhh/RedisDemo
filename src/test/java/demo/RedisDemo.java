package demo;

import com.liuhh.redisdemo.RedisDemoApplication;
import com.liuhh.redisdemo.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.swing.*;

/**
 * @Author: liuhh
 * @Date: 2022/5/10
 */
@SpringBootTest(classes = RedisDemoApplication.class)
public class RedisDemo {

    @Autowired
    private RedisUtil redisUtil;

    private final String key = "COM:LIUHH:USERNAME:";

    @Test
    public void test1(){
        Jedis jedis = new Jedis(redisUtil.getIp(), redisUtil.getPort());
        jedis.select(1);
        jedis.set("name", "liuhh");
        System.out.println(jedis.get("name"));
        jedis.close();
    }

    @Test
    public void pool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisUtil.getMaxActive());
        config.setMaxIdle(redisUtil.getMaxIdle());
        JedisPool jedisPool = new JedisPool(config, redisUtil.getIp(), redisUtil.getPort());
        Jedis jedis = null;
        try {
           jedis = jedisPool.getResource();
           jedis.set(key, "liucc");
           jedis.select(1);
           String valueStr = jedis.get("name");
            System.out.println("============>" + valueStr);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
            if(jedisPool != null){
                jedisPool.close();
            }
        }


    }
}
