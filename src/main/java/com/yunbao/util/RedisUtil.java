package com.yunbao.util;


import com.yunbao.Exception.RedisConnectionFailureError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Map;

/**
 * Redis工具类(Jedis版)
 */
@Component
public class RedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private ShardedJedisPool shardedJedisPool;



    public <T> T excute(Function<ShardedJedis, T> fun)  throws Exception{
        ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();

            return fun.callback(shardedJedis);

        } catch (Exception e) {
            logger.error("redis error");
            throw new RedisConnectionFailureError("redis服务连接失败");
        } finally {
            if (null != shardedJedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
    }

    public String set(final String key,final String value) throws Exception{
        return excute(new Function<ShardedJedis, String>() {
            public String callback(ShardedJedis e) {
                return e.set( key, value);
            }
        });
    }

    public String get(final String key) throws Exception{
        return excute(new Function<ShardedJedis, String>() {

            public String callback(ShardedJedis e) {
                return e.get(key);
            }
        });
    }

    public Long del(final String key) throws Exception{
        return excute(new Function<ShardedJedis, Long>() {

            public Long callback(ShardedJedis e) {
                return e.del(key);
            }
        });
    }

    public Long expire(final String key,final Integer seconds) throws Exception{
        return excute(new Function<ShardedJedis, Long>() {
            public Long callback(ShardedJedis e) {
                return e.expire(key, seconds);
            }
        });
    }

    public Long set(final String key,final String value,final Integer seconds) throws Exception{
        return  excute(new Function<ShardedJedis, Long>() {
            public Long callback(ShardedJedis e) {
                e.set(key, value);
                return e.expire(key, seconds);
            }
        });
    }

    public Long incr(final String key) throws Exception{
        return excute(new Function<ShardedJedis, Long>() {
            public Long callback(ShardedJedis e) {
                return e.incr(key);
            }
        });
    }

    public Boolean hasKey(final String key) throws Exception{
        return excute(new Function<ShardedJedis, Boolean>() {
            public Boolean callback(ShardedJedis e) {
                return e.exists(key);
            }
        });
    }

    public Long hSet(final String key, final String field,final String value) throws Exception{
        return excute(new Function<ShardedJedis, Long>() {
            public Long callback(ShardedJedis e) {
                return e.hset(key, field, value);
            }
        });
    }

    public String hGet(final String key, final String field) throws Exception{
        return excute(new Function<ShardedJedis, String>() {
            public String callback(ShardedJedis e) {
                return e.hget(key, field);
            }
        });
    }

    public Map<String,String> hGetAll(final String key) throws Exception{
        return excute(new Function<ShardedJedis,Map<String,String>>() {
            public Map<String, String> callback(ShardedJedis e) {
                return e.hgetAll(key);
            }
        });
    }

    //链表左插入
    public Long lPush(String key, String... values) throws Exception{
        return excute(new Function<ShardedJedis,Long>() {
            public Long callback(ShardedJedis e) {
                return e.lpush(key, values);
            }
        });
    }

    //链表右插入
    public Long rPush(String key, String... values) throws Exception{
        return excute(new Function<ShardedJedis,Long>() {
            public Long callback(ShardedJedis e) {
                return e.rpush(key, values);
            }
        });
    }

    //链表左弹出
    public String lPop(final String key) throws Exception{
        return excute(new Function<ShardedJedis,String>() {
            public String callback(ShardedJedis e) {
                return e.lpop(key);
            }
        });
    }

    //链表左流出
    public String rPop(String key) throws Exception{
        return excute(new Function<ShardedJedis,String>() {
            public String callback(ShardedJedis e) {
                return e.rpop(key);
            }
        });
    }

    //返回链表当前长度
    public Long lLen(String key) throws Exception{
        return excute(new Function<ShardedJedis,Long>(){
            public Long callback(ShardedJedis e) {
                return e.llen(key);
            }
        });
    }

}
