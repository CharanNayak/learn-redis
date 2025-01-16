package com.example.learn_redis.basic;

import io.lettuce.core.SocketOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.internal.HostAndPort;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DnsResolver;
import io.lettuce.core.resource.MappingSocketAddressResolver;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.function.UnaryOperator;

/**
 * This class is not used and kept for RedisCluster connection factory reference
 */

//@EnableConfigurationProperties(RedisCacheProperties.class)
//@Configuration
public class RedisClusterConfig {

//    @Bean
//    @Primary
    public RedisConnectionFactory redisConnectionFactory(RedisCacheProperties redisCacheProperties) {
        RedisConnectionFactory redisConnectionFactory = null;

        if(redisCacheProperties.isClusterMode()) {
            String host = redisCacheProperties.getHost();
            int port = redisCacheProperties.getPort();
            RedisNode.newRedisNode()
                    .listeningAt(host, port)
                    .build();

            final RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();

            UnaryOperator<HostAndPort> mappingFunction = new UnaryOperator<>() {

                @Override
                public HostAndPort apply(HostAndPort hostAndPort) {
                    InetAddress[] inetAddresses = new InetAddress[0];
                    try {
                        inetAddresses = DnsResolver.jvmDefault().resolve(host);
                    } catch (UnknownHostException e) {
                        return hostAndPort;
                    }

                    String cacheIP =  inetAddresses[0].getHostAddress();
                    HostAndPort finalAddress = hostAndPort;

                    if(hostAndPort.getHostText().equals(cacheIP)) {
                        finalAddress = HostAndPort.of(host, hostAndPort.getPort());
                    }

                    return finalAddress;
                }
            };

            MappingSocketAddressResolver addressResolver = MappingSocketAddressResolver.create(DnsResolver.jvmDefault(), mappingFunction);

            //Following are cluster connectivity reliability config
            ClientResources clientResources = ClientResources.builder()
                    .socketAddressResolver(addressResolver)
                    .build();

            SocketOptions socketOptions = SocketOptions.builder()
                    .keepAlive(true)
                    .build();

            ClusterTopologyRefreshOptions.Builder refreshBuilder = ClusterTopologyRefreshOptions.builder();
            refreshBuilder.enablePeriodicRefresh(Duration.ofSeconds(redisCacheProperties.getTopologyRefreshSeconds()));
            refreshBuilder.enableAllAdaptiveRefreshTriggers();

            ClusterClientOptions clientOptions = ClusterClientOptions.builder()
                    .topologyRefreshOptions(refreshBuilder.build())
                    .autoReconnect(true)
                    .socketOptions(socketOptions)
                    .build();

            final LettuceClientConfiguration.LettuceSslClientConfigurationBuilder lettuceClientConfigurationBuilder = LettuceClientConfiguration.builder().useSsl();
            final LettuceClientConfiguration lettuceClientConfiguration = lettuceClientConfigurationBuilder
                    .and().clientResources(clientResources)
                    .clientOptions(clientOptions)
                    .build();

            LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
            lettuceConnectionFactory.afterPropertiesSet();
            redisConnectionFactory = lettuceConnectionFactory;
        }

        return redisConnectionFactory;
    }

}
