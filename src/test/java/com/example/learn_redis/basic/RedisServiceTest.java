package com.example.learn_redis.basic;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisServiceTest {

    private static final String REDIS_IMAGE_NAME = "redis:latest";
    private static final int REDIS_PORT = 6379;

    private static GenericContainer<?> redis;

    @BeforeAll
    static void beforeAll() {
        redis = new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE_NAME)).withExposedPorts(REDIS_PORT);
        redis.start();
        System.setProperty("spring.data.redis.host", redis.getHost());
        System.setProperty("spring.data.redis.port", redis.getMappedPort(REDIS_PORT).toString());
    }

    @AfterAll
    static void afterAll() {
        redis.stop();
    }

    @Autowired
    private RedisService redisService;

    @Test
    void testParallelismForSameKey() throws URISyntaxException, IOException {
        System.out.println("testParallelismForSameKey - 1 large key - slower");
        String tenMBText = Files.readString(Path.of(ClassLoader.getSystemResource("10mb.txt").toURI()));
        redisService.addStringToCache(tenMBText);

        IntStream.range(1, 10).asLongStream()
                .parallel()
                .forEach(e -> {
                        long start = System.currentTimeMillis();
                        redisService.getStringFromCache();
                        System.out.println("thread " + e + " name " + Thread.currentThread().getName() + " " + (System.currentTimeMillis() - start));
                });
    }

    @Test
    void testParallelismForDiffSizeKey() throws URISyntaxException, IOException {
        System.out.println("testParallelismForDiffSizeKey - 1 small & 1 large - large key slows down small key inquiries");
        String tenMBText = Files.readString(Path.of(ClassLoader.getSystemResource("10mb.txt").toURI()));
        redisService.addStringToCacheForKey("small", "val1");
        redisService.addStringToCacheForKey("large", tenMBText);


        IntStream.range(1, 10).asLongStream()
                .parallel()
                .forEach(e -> {
                    if(ThreadLocalRandom.current().nextBoolean()) {
                        long start = System.currentTimeMillis();
                        redisService.getStringFromCacheForKey("large");
                        System.out.println("large " + e + " name " + Thread.currentThread().getName() + " " + (System.currentTimeMillis() - start));
                    } else {
                        long start = System.currentTimeMillis();
                        redisService.getStringFromCacheForKey("small");
                        System.out.println("small " + e + " name " + Thread.currentThread().getName() + " " + (System.currentTimeMillis() - start));
                    }

                });
    }

    @Test
    void testParallelismForDiffLargeSizeKey() throws URISyntaxException, IOException {
        System.out.println("testParallelismForDiffLargeSizeKey - 2 large key - slower");
        String tenMBText = Files.readString(Path.of(ClassLoader.getSystemResource("10mb.txt").toURI()));
        redisService.addStringToCacheForKey("large 1", tenMBText);
        redisService.addStringToCacheForKey("large 2", tenMBText);


        IntStream.range(1, 10).asLongStream()
                .parallel()
                .forEach(e -> {
                    if(ThreadLocalRandom.current().nextBoolean()) {
                        long start = System.currentTimeMillis();
                        redisService.getStringFromCacheForKey("large 1");
                        System.out.println("large 1 " + e + " name " + Thread.currentThread().getName() + " " + (System.currentTimeMillis() - start));
                    } else {
                        long start = System.currentTimeMillis();
                        redisService.getStringFromCacheForKey("large 2");
                        System.out.println("large 2 " + e + " name " + Thread.currentThread().getName() + " " + (System.currentTimeMillis() - start));
                    }

                });
    }

    @Test
    void testParallelismForDiffSmallSizeKey() throws URISyntaxException, IOException {
        System.out.println("testParallelismForDiffSmallSizeKey - 2 small keys - lot faster");
        String tenMBText = Files.readString(Path.of(ClassLoader.getSystemResource("10mb.txt").toURI()));
        redisService.addStringToCacheForKey("small 1", "val1");
        redisService.addStringToCacheForKey("small 2", "val2");


        IntStream.range(1, 10).asLongStream()
                .parallel()
                .forEach(e -> {
                    if(ThreadLocalRandom.current().nextBoolean()) {
                        long start = System.currentTimeMillis();
                        redisService.getStringFromCacheForKey("small 1");
                        System.out.println("small 1 " + e + " name " + Thread.currentThread().getName() + " " + (System.currentTimeMillis() - start));
                    } else {
                        long start = System.currentTimeMillis();
                        redisService.getStringFromCacheForKey("small 2");
                        System.out.println("small 2" + e + " name " + Thread.currentThread().getName() + " " + (System.currentTimeMillis() - start));
                    }

                });
    }
}