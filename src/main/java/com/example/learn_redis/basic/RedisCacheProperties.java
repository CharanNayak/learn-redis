package com.example.learn_redis.basic;

import lombok.Data;

@Data
public class RedisCacheProperties {

    private boolean isClusterMode = true;
    private String host;
    private int port;
    private long topologyRefreshSeconds = 60;
}
