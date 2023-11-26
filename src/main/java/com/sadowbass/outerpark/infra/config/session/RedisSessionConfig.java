package com.sadowbass.outerpark.infra.config.session;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;

@Configuration
@RequiredArgsConstructor
public class RedisSessionConfig {

    @Value("${server.servlet.session.cookie.name}")
    private String headerName;

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean
    public HeaderHttpSessionIdResolver headerHttpSessionIdResolver() {
        return new HeaderHttpSessionIdResolver(headerName);
    }
}
