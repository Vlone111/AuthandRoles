package com.example.authmicroservice.Service;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.UnifiedJedis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    JedisPool jedisPool = new JedisPool("localhost", 6379);

    public String getOtp(String email, String otp) {
        Jedis jedis = jedisPool.getResource();
        String key = email + ":" + otp;
        String value = jedis.get(key);
        if (value != null) {
            return value;
        }
        else{
            return "Wrong email or password";
        }
    }

    public void setOtp(String email, String otp) {
        Jedis jedis = jedisPool.getResource();
        jedis.setex(email + ":" + otp, 300, otp);

    }
}


