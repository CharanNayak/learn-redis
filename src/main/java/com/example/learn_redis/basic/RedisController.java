package com.example.learn_redis.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    @PostMapping(path = "add-nested-object-to-cache")
    public boolean addNestedObjectToCache(@RequestBody User user) {
        return redisService.addNestedObjectToCache(user);
    }

    @GetMapping(path = "get-nested-object-from-cache")
    public User getNestedObjectFromCache() {
        return redisService.getNestedObjectFromCache();
    }

    @PostMapping(path = "add-list-object-to-cache")
    public boolean addListObjectToCache(@RequestBody List<User> users) {
        return redisService.addListObjectToCache(users);
    }

    @GetMapping(path = "get-list-object-from-cache")
    public List<User> getListObjectFromCache() {
        return redisService.getListObjectFromCache();
    }

    @PostMapping(path = "add-mb-text-to-cache")
    public boolean add10mbTextToCache(@RequestBody String value) throws IOException, URISyntaxException {
        String tenMBText = Files.readString(Path.of(ClassLoader.getSystemResource("10mb.txt").toURI()));
        return redisService.addStringToCache(tenMBText);
    }

    @GetMapping(path = "get-mb-text-from-cache")
    public String getmbTextToCache() {
        return redisService.getStringFromCache();
    }
}
