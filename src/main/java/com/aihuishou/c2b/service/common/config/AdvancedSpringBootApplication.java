package com.aihuishou.c2b.service.common.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 *
 * @author jiashuai.xie
 * @since 2018/12/28 19:56 1.0.0.RELEASE
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication
@EnableBoot2Customizing
@EnableFeignClients
public @interface AdvancedSpringBootApplication {

    /**
     * Exclude specific auto-configuration classes such that they will never be applied.
     *
     * @return the classes to exclude
     */
    @AliasFor(annotation = SpringBootApplication.class)
    Class<?>[] exclude() default {};

    /**
     * Exclude specific auto-configuration class names such that they will never be
     * applied.
     *
     * @return the class names to exclude
     * @since 1.3.0
     */
    @AliasFor(annotation = SpringBootApplication.class)
    String[] excludeName() default {};

    /**
     * Base packages to scan for annotated components. Use {@link #scanBasePackageClasses}
     * for a type-safe alternative to String-based package names.
     *
     * @return base packages to scan
     * @since 1.3.0
     */
    @AliasFor(annotation = SpringBootApplication.class, attribute = "scanBasePackages")
    String[] scanBasePackages() default {};

    /**
     * Type-safe alternative to {@link #scanBasePackages} for specifying the packages to
     * scan for annotated components. The package of each class specified will be scanned.
     * <p>
     * Consider creating a special no-op marker class or interface in each package that
     * serves no purpose other than being referenced by this attribute.
     *
     * @return base packages to scan
     * @since 1.3.0
     */
    @AliasFor(annotation = SpringBootApplication.class, attribute = "scanBasePackageClasses")
    Class<?>[] scanBasePackageClasses() default {};

    /**
     * Base packages to scan for annotated components. Use {@link #scanBasePackageClasses}
     * for a type-safe alternative to String-based package names.
     *
     * @return base packages to scan
     * @since 1.3.0
     */
    @AliasFor(annotation = EnableFeignClients.class, attribute = "basePackages")
    String[] scanFeignBasePackages() default {};

    /**
     * Type-safe alternative to {@link #scanBasePackages} for specifying the packages to
     * scan for annotated components. The package of each class specified will be scanned.
     * <p>
     * Consider creating a special no-op marker class or interface in each package that
     * serves no purpose other than being referenced by this attribute.
     *
     * @return base packages to scan
     * @since 1.3.0
     */
    @AliasFor(annotation = EnableFeignClients.class, attribute = "basePackageClasses")
    Class<?>[] scanFeignBasePackageClasses() default {};

    @AliasFor(annotation = EnableFeignClients.class, attribute = "defaultConfiguration")
    Class<?>[] defaultConfiguration() default {};

    @AliasFor(annotation = EnableFeignClients.class, attribute = "clients")
    Class<?>[] clients() default {};

}
