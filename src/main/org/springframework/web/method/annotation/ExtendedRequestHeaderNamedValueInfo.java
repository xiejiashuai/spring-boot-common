package org.springframework.web.method.annotation;

/**
 * 扩展{@link AbstractNamedValueMethodArgumentResolver.NamedValueInfo}
 * 用于解决原生的{@link RequestHeaderMethodArgumentResolver.RequestHeaderNamedValueInfo} 无法从注解的value中获取值
 *
 * @author jiashuai.xie
 * @since 2018/12/24 13:56
 */
public class ExtendedRequestHeaderNamedValueInfo extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo {

    public ExtendedRequestHeaderNamedValueInfo(String name, boolean required, String defaultValue) {
        super(name, required, defaultValue);
    }

}
