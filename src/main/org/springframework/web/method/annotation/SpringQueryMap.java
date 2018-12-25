package org.springframework.web.method.annotation;

import feign.QueryMap;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Spring MVC equivalent of OpenFeign's {@link feign.QueryMap} parameter annotation.
 *
 * @author Aram Peres
 * @see feign.QueryMap
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface SpringQueryMap {

    /**
     * Alias for {@link #encoded()}.
     *
     * @see QueryMap#encoded()
     */
    @AliasFor("encoded")
    boolean value() default false;

    /**
     * Specifies whether parameter names and values are already encoded.
     *
     * @see QueryMap#encoded()
     */
    @AliasFor("value")
    boolean encoded() default false;
}
