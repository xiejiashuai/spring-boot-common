package org.springframework.web.method.annotation;

import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

/**
 * 扩展{@link AbstractNamedValueMethodArgumentResolver.NamedValueInfo}
 * 用于解决原生的{@link PathVariableMethodArgumentResolver.PathVariableNamedValueInfo} 无法从注解的value中获取值
 *
 * @author jiashuai.xie
 * @since 2018/12/24 13:53
 */
public class ExtendedPathVariableNamedValueInfo extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo {

    public ExtendedPathVariableNamedValueInfo(String name, boolean required, String defaultValue) {
        super(name, required, defaultValue);
    }

}
