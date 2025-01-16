package com.example.learn_redis.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean addStringToCache(String value) {
        return redisTemplate.opsForValue().setIfAbsent("name", value);
    }

    public boolean addObjectToCache(Person person) {
        redisTemplate.opsForValue().set("person", person);
        return true;
    }

    public Person getObjectFromCache() {
        return (Person) redisTemplate.opsForValue().get("person");
    }

    public boolean addNestedObjectToCache(User user) {
        redisTemplate.opsForValue().set("user", user);
        return true;
    }

    public User getNestedObjectFromCache() {
        return (User) redisTemplate.opsForValue().get("user");
    }
}
