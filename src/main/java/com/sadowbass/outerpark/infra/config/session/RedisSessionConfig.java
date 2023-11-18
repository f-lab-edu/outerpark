package com.sadowbass.outerpark.infra.config.session;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;

@Configuration
@RequiredArgsConstructor
public class RedisSessionConfig {

    private final RedisConnectionFactory redisConnectionFactory;

    @Value("${server.servlet.session.cookie.name}")
    private String headerName;

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());

        return redisTemplate;
    }

    @Bean
    public HeaderHttpSessionIdResolver headerHttpSessionIdResolver() {
        return new HeaderHttpSessionIdResolver(headerName);
    }
}
