package com.lrnews.utils;

import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisOperator {

    private final StringRedisTemplate redisTemplate;

    public RedisOperator(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Boolean keyExist(String key) {
        return redisTemplate.hasKey(key);
    }

    public Optional<Long> getExpire(String key) {
        return Optional.ofNullable(redisTemplate.getExpire(key));
    }

    public void setExpire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    public Long increase(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public Long decrease(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void set(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    public void setNx60s(String key, String value) {
        redisTemplate.opsForValue().setIfAbsent(key, value, 60, TimeUnit.SECONDS);
    }

    public void setIfAbsent(String key, String value) {
        redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public List<String> mget(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    public List<Object> batchGet(List<String> keys) {
        List<Object> result = redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            StringRedisConnection src = (StringRedisConnection)connection;
            keys.forEach(src::get);
            return null;
        });
        return result;
    }

    public void hset(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public String hget(String key, String field) {
        return (String) redisTemplate.opsForHash().get(key, field);
    }

    public void hdel(String key, Object... fields) {
        redisTemplate.opsForHash().delete(key, fields);
    }

    public Map<Object, Object> hgetall(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public Long lpush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    public String lpop(String key) {
        return (String)redisTemplate.opsForList().leftPop(key);
    }

    public Long rpush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }
}
