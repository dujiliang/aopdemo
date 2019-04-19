package com.yunbao.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;

@Configuration
public class JedisConfig {

    private static final Logger logger = LoggerFactory.getLogger(JedisConfig.class);
    @Value("${spring.redis.jedis.pool.max-active}")
    private Integer maxTotal;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private Integer port;

    @Bean
    public ShardedJedisPool shardedJedisPool() {
        //配置连接池
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        ArrayList<JedisShardInfo> arrayList = new ArrayList<>();
        arrayList.add(new JedisShardInfo(host, port));
        return new ShardedJedisPool(jedisPoolConfig, arrayList);
    }

}
