package com.zrd.ss.springbootsssubdbtable.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.math.BigInteger;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 类型转换，解决spring boot 返回json数据long型精度丢失问题
 * @Author ZRD
 * @Date 2020/11/30
 */
@Configuration
public class JacksonConfig {
    /**
     * Jackson全局转化long类型为String，解决jackson序列化时传入前端Long类型缺失精度问题
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.serializerByType(BigInteger.class, ToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
        };
    }
}