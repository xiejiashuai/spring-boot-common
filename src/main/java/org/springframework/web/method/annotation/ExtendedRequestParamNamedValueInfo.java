package org.springframework.web.method.annotation;

/**
 * 扩展{@link AbstractNamedValueMethodArgumentResolver.NamedValueInfo}
 * 用于解决原生的{@link RequestParamMethodArgumentResolver.RequestParamNamedValueInfo} 无法从注解的value中获取值
 *
 * @author jiashuai.xie
 * @since 2018/12/28 19:15 1.0.0.RELEASE
 */
public class ExtendedRequestParamNamedValueInfo extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo {

    public ExtendedRequestParamNamedValueInfo(String name, boolean required, String defaultValue) {
        super(name, required, defaultValue);
    }

}
