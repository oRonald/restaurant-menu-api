package restaurant.menu.api.app.infrastructure.redis;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import restaurant.menu.api.app.domain.dto.ItemsDetails;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.jsontype.PolymorphicTypeValidator;

import java.time.Duration;
import java.util.List;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        GenericJacksonJsonRedisSerializer serializer =
                new GenericJacksonJsonRedisSerializer(buildRedisObjectMapper());

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        return template;
    }

    @Bean
    public CacheManager menuCacheManager(RedisConnectionFactory connectionFactory) {
        ObjectMapper mapper = JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .findAndAddModules()
                .build();

        JavaType listType = mapper.getTypeFactory()
                .constructCollectionType(List.class, ItemsDetails.class);

        RedisSerializer<Object> serializer = new RedisSerializer<>() {
            @Override
            public byte[] serialize(Object value) throws SerializationException {
                try {
                    return mapper.writeValueAsBytes(value);
                } catch (Exception e) {
                    throw new SerializationException("Erro ao serializar", e);
                }
            }

            @Override
            public Object deserialize(byte[] bytes) throws SerializationException {
                if (bytes == null) return null;
                try {
                    return mapper.readValue(bytes, listType);
                } catch (Exception e) {
                    throw new SerializationException("Erro ao deserializar", e);
                }
            }
        };

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(serializer)
                );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }

    private ObjectMapper buildRedisObjectMapper() {
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator
                .builder()
                .allowIfSubType(Object.class)
                .build();

        return JsonMapper.builder()
                .activateDefaultTyping(ptv, DefaultTyping.NON_FINAL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();
    }
}
