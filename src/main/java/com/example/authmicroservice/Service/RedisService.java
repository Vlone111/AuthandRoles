package com.example.authmicroservice.Service;


import org.springframework.security.authentication.BadCredentialsException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    JedisPool jedisPool = new  JedisPool("localhost", 6379);

    public String getOtp(String email, String otp) {
        try(Jedis jedis = jedisPool.getResource()) {
            String key = email + ":" + otp;
            String value = jedis.get(key);
            if (value != null) {
                return value;
            } else {
                throw new BadCredentialsException("Password is expired");
            }
        }
        catch (Exception e) {
            throw new BadCredentialsException("Can't save otp");
        }
    }

    public void setOtp(String email, String otp) {
        try(Jedis jedis = jedisPool.getResource()){
            jedis.setex(email + ":" + otp, 300, otp);
        }
        catch (Exception e){
            throw new BadCredentialsException("Cant't give otp");
        }
    }
}


