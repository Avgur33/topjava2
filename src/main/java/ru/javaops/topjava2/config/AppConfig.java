package ru.javaops.topjava2.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.javaops.topjava2.util.JsonUtil;
import ru.javaops.topjava2.web.AuthUser;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.javaops.topjava2.util.DateUtil.endDateUtil;
import static ru.javaops.topjava2.util.DateUtil.startDateUtil;

@Configuration
@Slf4j
@EnableCaching
public class AppConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    @Profile("!test")
    Server h2Server() throws SQLException {
        log.info("Start H2 TCP server");
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }

    //    https://stackoverflow.com/a/46947975/548473
    @Bean
    Module module() {
        return new Hibernate5Module();
    }

    @Autowired
    public void storeObjectMapper(ObjectMapper objectMapper) {
        JsonUtil.setMapper(objectMapper);
    }

    @Bean
    public CaffeineCache restaurants() {
        return new CaffeineCache("restaurants",
                Caffeine.newBuilder()
                        .maximumSize(1)
                        .expireAfterAccess(300, TimeUnit.SECONDS)
                        .build());
    }

    @Bean
    public CaffeineCache dishes() {
        return new CaffeineCache("dishes",
                Caffeine.newBuilder()
                        .maximumSize(500)
                        .expireAfterAccess(300, TimeUnit.SECONDS)
                        .build());
    }
    @Bean
    public CaffeineCache votesUser () {
        return new CaffeineCache("votes_user",
                Caffeine.newBuilder()
                        .maximumSize(1)
                        .expireAfterAccess(300, TimeUnit.SECONDS)
                        .build());
    }


    @Bean(name = "votesUserKeyGenerator")
    public CacheKeyGenerator votesUserKeyGenerator(){
        return new CacheKeyGenerator();
    }


    public static class CacheKeyGenerator
            implements org.springframework.cache.interceptor.KeyGenerator {

        @NotNull
        @Override
        public Object generate( Object target,  Method method,
                                Object... params) {

            final List<Object> key = new ArrayList<>();
            key.add(method.getDeclaringClass().getName());
            key.add(method.getName());
            key.add(startDateUtil((LocalDate)params[0]));
            key.add(endDateUtil((LocalDate)params[1]));
            key.add(params[2]!=null? ((AuthUser)params[2]).id():null);
            return key;
        }

    }
}