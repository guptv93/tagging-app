package edu.nyu.hml.tagging.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Getter
@Configuration
public class RedisConfig {

    @Value("${redis.hostname}")
    private String hostName;

    @Bean
    public Jedis redisClient() {
        return new Jedis(hostName);
    }

}
