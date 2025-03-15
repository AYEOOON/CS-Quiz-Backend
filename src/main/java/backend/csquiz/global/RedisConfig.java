package backend.csquiz.global;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;


/**
 * Redis 설정 클래스
 * Spring Boot 애플리케이션에서 Redis를 사용하기 위한 설정
 */
@Configuration
public class RedisConfig {
    /**
     * Redis 연결 팩토리 생성
     * Lettuce를 이용하여 Redis 서버와 연결하는 ConnectionFactory를 생성합니다.
     *
     * @return RedisConnectionFactory - Redis 연결을 위한 팩토리 객체
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory();
    }

    /**
     * RedisTemplate 설정
     * Redis와 데이터를 주고받기 위한 템플릿을 설정합니다.
     * Redis에서 key와 value를 String 타입으로 저장할 때 사용됩니다.
     *
     * @param redisConnectionFactory Redis 연결 팩토리
     * @return RedisTemplate<String, String> - Redis 연산을 위한 템플릿 객체
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    /**
     * StringRedisTemplate 설정
     * Redis에서 문자열 데이터를 다룰 때 편리하게 사용할 수 있는 템플릿을 생성합니다.
     *
     * @param redisConnectionFactory Redis 연결 팩토리
     * @return StringRedisTemplate - 문자열 기반 Redis 연산을 위한 템플릿 객체
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        return new StringRedisTemplate(redisConnectionFactory);
    }
}
