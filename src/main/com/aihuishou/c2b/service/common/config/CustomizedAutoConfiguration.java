package com.aihuishou.c2b.service.common.config;

import com.aihuishou.c2b.service.common.config.feign.FeignConfiguration;
import com.aihuishou.c2b.service.common.config.sentinel.SentinelConfiguration;
import com.aihuishou.c2b.service.common.config.validation.CustomizedValidationConfiguration;
import com.aihuishou.c2b.service.common.config.web.servlet.SpeedUpWebConfiguration;
import com.aihuishou.c2b.service.common.config.web.servlet.WebMvcConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author jiashuai.xie
 * @since 2018/12/19 18:45
 */
@Configuration
@AutoConfigureBefore(FeignAutoConfiguration.class)
@Import({SentinelConfiguration.class,
        CustomizedValidationConfiguration.class,
        SpeedUpWebConfiguration.class,
        FeignConfiguration.class,
        WebMvcConfiguration.class})
public class CustomizedAutoConfiguration {


}
