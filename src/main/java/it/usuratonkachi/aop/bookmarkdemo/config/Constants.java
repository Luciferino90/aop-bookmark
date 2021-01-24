package it.usuratonkachi.aop.bookmarkdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

    public static Long REDIS_TTL = 60L;

    @Value("${spring.redis.ttl}")
    public void setRedisTtl(Long springRedisTtl) {
        REDIS_TTL = springRedisTtl;
    }

}
