package com.liuhh.redisdemo.util;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.nosql.redis.RedisDS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: liuhh
 * @Date: 2022/9/6
 */
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //======================String==========================
    /**
     * 存入key
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, Object value){
        try{
            redisTemplate.opsForValue().set(key, value);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 取key
     * @return
     */
    public Object get(String key){
        return StrUtil.isBlank(key) ? null : redisTemplate.opsForValue().get(key);
    }


    /**
     * 设置缓存失效时间(s)
     */
    public boolean expire(String key, long time){
        try {
            if(time > 0){
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取过期时间
     */
    public long getExpire(String key){
        return StrUtil.isBlank(key) ? null : redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public boolean hasKey(String key){
        try {
            return redisTemplate.hasKey(key);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除一个key或多个
     * @param keys
     */
    public void del(String ... keys){
        if(ArrayUtil.isNotEmpty(keys) && (keys.length > 0)){
            if(keys.length == 1){
                redisTemplate.delete(keys[0]);
            }else{
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(keys));
            }
        }
    }

    /**
     * 存放key同时设置过期时间
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean set(String key, Object value, long time){
        try {
            if(time > 0){
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            }else{
                set(key, value);
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     * @param key
     * @param delta
     * @return
     */
    public long incr(String key, long delta){
        if(delta < 0){
            throw new RuntimeException("递增因子必须大于零");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key
     * @param delta
     * @return
     */
    public long decr(String key, long delta){
        if(delta < 0){
            throw new RuntimeException("递增因子必须大于零");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    //======================hash==========================

    /**
     * HashGet
     * @param key
     * @param item
     * @return
     */
    public Object hGet(String key, String item){
        return (StrUtil.isBlank(key) || StrUtil.isBlank(item)) ? null : redisTemplate.opsForHash().get(key, item);
    }

    /**
     * Hmset
     * @param key
     * @param map
     * @return
     */
    public boolean hMset(String key, Map<String, Object> map){
        try{
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * hmset 存放数据并设置过期时间
     * @param key
     * @param map
     * @return
     */
    public boolean hMset(String key, Map<String, Object> map, long time){
        try{
            redisTemplate.opsForHash().putAll(key, map);
            if(time > 0){
                expire(key, time);
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * hset
     * @param key
     * @param item
     * @param value
     * @return
     */
    public boolean hSet(String key, String item, Object value){
        try{
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * hset，并设置过期时间
     * @param key
     * @param item
     * @param value
     * @return
     */
    public boolean hExpire(String key, String item, Object value, long time){
        try{
            redisTemplate.opsForHash().put(key, item, value);
            if(time > 0){
                expire(key, time);
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除值
     */
    public void hDel(String key, Object... item){
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断是否存在key对应的属性值
     * @param key
     * @param item
     * @return
     */
    public boolean hHasKey(String key, String item){
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增
     * @param key
     * @param item
     * @param value
     * @return
     */
    public double hIncr(String key, String item, double value){
        return redisTemplate.opsForHash().increment(key, item, value);
    }

    /**
     * hash递减
     * @param key
     * @param item
     * @param value
     * @return
     */
    public double hDecr(String key, String item, double value){
        return redisTemplate.opsForHash().increment(key, item, -value);
    }

    //=====================set====================

    /**
     * set中放入元素
     * @param key
     * @param values
     * @return
     */
    public long sSet(String key, Object... values){
        try {
            return redisTemplate.opsForSet().add(key, values);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * set中放入元素
     * @param key
     * @param values
     * @return
     */
    public long sExpire(String key,long time, Object... values){
        try {
            Long add = redisTemplate.opsForSet().add(key, values);
            if(time > 0){
                expire(key, time);
            }
            return add;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * 判断是否是set中的元素
     * @param key
     * @param value
     * @return
     */
    public boolean sIsMember(String key, Object value){
        try{
            return redisTemplate.opsForSet().isMember(key, value);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 获取key对应所有值
     * @param key
     * @return
     */
    public Set<Object> sMembers(String key){
        try {
            return redisTemplate.opsForSet().members(key);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取set的长度
     * @param key
     * @return
     */
    public long sCard(String key){
        try {
            return redisTemplate.opsForSet().size(key);
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * 移除set中的元素
     * @param key
     * @param values
     * @return
     */
    public long sRem(String key, Object... values){
        try{
            return redisTemplate.opsForSet().remove(key, values);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    //============================list==========================

    /**
     * 获取list长度
     * @param key
     * @return
     */
    public long lLen(String key){
        try {
            return redisTemplate.opsForList().size(key);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据索引获取list中的元素
     * @param key
     * @param index
     * @return
     */
    public Object lIndex(String key, long index){
        try{
            return redisTemplate.opsForList().index(key, index);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * list中放入元素，头插法
     * @param key
     * @param values
     * @return
     */
     public long lPush(String key, Object values){
        try {
            return redisTemplate.opsForList().leftPush(key, values);
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
     }

    /**
     * list中放入元素，尾插法
     * @param key
     * @param values
     * @return
     */
    public long rPush(String key, Object values){
        try {
            return redisTemplate.opsForList().rightPush(key, values);
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * list中放入元素，头插法
     * @param key
     * @param values
     * @return
     */
    public long lExpire(String key,long time, Object values){
        try {
            Long aLong = redisTemplate.opsForList().leftPush(key, values);
            if(time > 0){
                expire(key, time);
            }
            return aLong;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 放入集合对象
     * @param key
     * @param listValues
     * @return
     */
    public long lAddList(String key, List<Object> listValues){
        try {
            Long aLong = redisTemplate.opsForList().rightPushAll(key, listValues);
            return aLong;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 放入集合对象，并设置过期时间
     * @param key
     * @param listValues
     * @return
     */
    public long lListExpire(String key,long time, List<Object> listValues){
        try {
            Long aLong = redisTemplate.opsForList().rightPushAll(key, listValues);
            if(time > 0){
                expire(key, time);
            }
            return aLong;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 更新list指定索引元素
     * @param key
     * @param index
     * @param value
     * @return
     */
    public boolean lSet(String key, long index, Object value){
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除list中count个值为value的元素
     * @param key
     * @param count
     * @param value
     * @return
     */
    public long lRem(String key, long count, Object value){
        try{
            return redisTemplate.opsForList().remove(key, count, value);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }


    //=======================pub/sub==================
    public void publishMessage(String channel, String message){
        redisTemplate.convertAndSend(channel, message);
    }
}
