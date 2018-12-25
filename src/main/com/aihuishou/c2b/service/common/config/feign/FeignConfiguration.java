package com.aihuishou.c2b.service.common.config.feign;

import feign.Contract;
import feign.Logger;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign 定制化配置
 *
 * @author jiashuai.xie
 * @since 2018/12/24 11:15
 */
@Configuration
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignConfiguration {

    @Bean
    public Logger logger() {
        return new CustomizedLogger();
    }

    @Bean
    public Contract extendedSpringMvcContract() {
        return new ExtendedSpringMvcContract();
    }

}
