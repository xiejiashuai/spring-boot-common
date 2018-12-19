package com.aihuishou.c2b.service.common.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author jiashuai.xie
 * @since 2018/12/19 18:47
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CustomizedAutoConfiguration.class)
public @interface EnableBoot2Customizing {
}
