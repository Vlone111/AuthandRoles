package com.example.authmicroservice.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPool;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool("localhost", 6379);
    }
}

