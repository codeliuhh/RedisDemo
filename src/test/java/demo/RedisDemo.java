package demo;

import com.liuhh.redisdemo.RedisDemoApplication;
import com.liuhh.redisdemo.RedisUtil;
import com.liuhh.redisdemo.util.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import javax.swing.*;
import java.util.List;
import java.util.Set;

/**
 * @Author: liuhh
 * @Date: 2022/5/10
 */
@SpringBootTest(classes = RedisDemoApplication.class)
public class RedisDemo {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    RedisUtils redisUtils;

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


    @Test
    public void redisTest(){
        Jedis jedis = new Jedis("127.0.0.1",6379);
        //清库
        jedis.flushDB();

        //开启事务
        Transaction multi = jedis.multi();

        try {
            //事务操作
            multi.set("liuhh", "I LOVE YOU");
            int i = 1/0;
            multi.exec();
        }catch (Exception e){
            System.out.println(e);
            multi.discard();
        }finally {
            //关闭事务
            multi.close();
        }
        System.out.println(jedis.get("liuhh"));

    }

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void testRedistemplet(){
        System.out.println("-----------String------------");
        redisTemplate.opsForValue().set("liuhh", "i love you");
        Object liuhh = redisTemplate.opsForValue().get("liuhh");
        System.out.println(liuhh);
        System.out.println("------------list------------");
        redisTemplate.opsForList().leftPushAll("liuxx","huhu","haha", "heihei");

        List liuxx = redisTemplate.opsForList().range("liuxx", 0, -1);
        System.out.println(liuxx);


        System.out.println("------------set------------");
        redisTemplate.opsForSet().add("setTest", "liuhh","liucc");
        Set setTest = redisTemplate.opsForSet().members("setTest");
        System.out.println(setTest);


        System.out.println("------------hash------------");
        redisTemplate.opsForHash().put("liuMap","name","liuhh");
        Object o = redisTemplate.opsForHash().get("liuMap", "name");
        System.out.println(o);


        System.out.println("------------zset------------");
        redisTemplate.opsForZSet().add("salary","liuww", 2500);
        redisTemplate.opsForZSet().add("salary","liuaa", 2200);
        redisTemplate.opsForZSet().add("salary","liupp", 2700);
        Set salary = redisTemplate.opsForZSet().range("salary", 0, -1);
        System.out.println(salary);

        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    public void testPub(){
        redisUtils.publishMessage("liuhh", "I LOVE YOU");
    }
}
