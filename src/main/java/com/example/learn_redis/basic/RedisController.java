package com.example.learn_redis.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

    @Autowired
    private RedisService redisService;

    @PostMapping(path = "add-string-to-cache")
    public boolean addToCache(@RequestBody String value) {
        return redisService.addStringToCache(value);
    }

    @PostMapping(path = "add-object-to-cache", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean addObjectToCache(@RequestBody Person person) {
        return redisService.addObjectToCache(person);
    }

    @GetMapping(path = "get-object-from-cache")
    public Person getObjectFromCache() {
        return redisService.getObjectFromCache();
    }
}
