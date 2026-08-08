package lk.ijse.cartservice.config;

import lk.ijse.cartservice.entity.Cart;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Cart> redisTemplate(
            RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, Cart> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        // Key
        template.setKeySerializer(
                new StringRedisSerializer()
        );

        // Value
        template.setValueSerializer(
                new JacksonJsonRedisSerializer<>(Cart.class)
        );

        // Hash key
        template.setHashKeySerializer(
                new StringRedisSerializer()
        );

        // Hash value
        template.setHashValueSerializer(
                new JacksonJsonRedisSerializer<>(Cart.class)
        );

        template.afterPropertiesSet();

        return template;
    }
}