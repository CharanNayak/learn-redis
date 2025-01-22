package com.example.learn_redis.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean addStringToCache(String value) {
        redisTemplate.opsForValue().set("name", value);
        return true;
    }

    public boolean addStringToCacheForKey(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        return true;
    }

    public String getStringFromCache() {
        long start = System.currentTimeMillis();
        String value = (String) redisTemplate.opsForValue().get("name");
        long end = System.currentTimeMillis();
//        System.out.println("timeTaken=".concat(String.valueOf(end - start)));
        return "";
    }

    public String getStringFromCacheForKey(String key) {
        long start = System.currentTimeMillis();
        String value = (String) redisTemplate.opsForValue().get(key);
        long end = System.currentTimeMillis();
//        System.out.println("timeTaken=".concat(String.valueOf(end - start)));
        return "";
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

    public boolean addListObjectToCache(List<User> users) {
        redisTemplate.opsForValue().set("userlist", users                               );
        return true;
    }

    public List<User> getListObjectFromCache() {
        return (List<User>) redisTemplate.opsForValue().get("userlist");
    }
}
