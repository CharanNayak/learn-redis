package com.example.learn_redis.basic;

public class RedisCacheProperties {

    private boolean isClusterMode = true;
    private String host;
    private int port;
    private long topologyRefreshSeconds = 60;

    public boolean isClusterMode() {
        return isClusterMode;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public long getTopologyRefreshSeconds() {
        return topologyRefreshSeconds;
    }
}
