package com.aihuishou.c2b.service.common.config;

import com.aihuishou.c2b.service.common.config.feign.CustomizedLogger;
import com.aihuishou.c2b.service.common.config.sentinel.SentinelConfiguration;
import com.aihuishou.c2b.service.common.config.validation.CustomizedValidationConfiguration;
import com.aihuishou.c2b.service.common.config.web.SpeedUpWebConfiguration;
import feign.Logger;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author jiashuai.xie
 * @since 2018/12/19 18:45
 */
@Configuration
@AutoConfigureBefore(FeignAutoConfiguration.class)
@Import({SentinelConfiguration.class, CustomizedValidationConfiguration.class,SpeedUpWebConfiguration.class})
public class CustomizedAutoConfiguration {

    @Bean
    public Logger logger() {
        return new CustomizedLogger();
    }

}
