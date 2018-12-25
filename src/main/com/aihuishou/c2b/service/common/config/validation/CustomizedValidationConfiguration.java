package com.aihuishou.c2b.service.common.config.validation;

import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.BeanValidationPostProcessor;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Customize JSR-303  JSR-349 Interface {@link Validator}
 *
 * @author jiashuai.xie
 * @see ValidationAutoConfiguration
 * @see MethodValidationPostProcessor
 * @see BeanValidationPostProcessor
 */
@Configuration
@AutoConfigureBefore(ValidationAutoConfiguration.class)
public class CustomizedValidationConfiguration {

    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                // fail fast override default configuration
                .failFast(true)
                .buildValidatorFactory();

        Validator validator = validatorFactory.getValidator();

        return validator;
    }


    @Bean
    public static BeanValidationPostProcessor beanValidationPostProcessor() {
        return new BeanValidationPostProcessor();
    }

}
