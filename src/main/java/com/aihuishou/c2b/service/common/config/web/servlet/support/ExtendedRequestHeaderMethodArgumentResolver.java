package com.aihuishou.c2b.service.common.config.web.servlet.support;

import com.aihuishou.c2b.service.common.config.web.servlet.util.MethodArgumentResolverUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.method.annotation.ExtendedRequestHeaderNamedValueInfo;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;

/**
 * Extend {@link RequestHeaderMethodArgumentResolver} to resolve problem
 * that Spring MVC can't support param annotation {@link RequestHeader} on interface
 *
 * <note>
 * 处理{@link RequestHeader}仍然由{@link RequestHeaderMethodArgumentResolver}处理
 * 该扩展类只是当类上的方法不含有{@link RequestHeader}注解的时候，从接口中获取
 * </note>
 *
 * @author jiashuai.xie
 * @since 2018/12/24 10:51
 */
public class ExtendedRequestHeaderMethodArgumentResolver extends RequestHeaderMethodArgumentResolver {

    /**
     * @param beanFactory a bean factory to use for resolving  ${...}
     *                    placeholder and #{...} SpEL expressions in default values;
     *                    or {@code null} if default values are not expected to have expressions
     */
    public ExtendedRequestHeaderMethodArgumentResolver(ConfigurableBeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        MethodParameter methodParameter = MethodArgumentResolverUtils.detectAvailableMethodParameter(parameter, RequestHeader.class);
        return super.supportsParameter(methodParameter);
    }

    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        MethodParameter methodParameter = MethodArgumentResolverUtils.detectAvailableMethodParameter(parameter, RequestHeader.class);
        RequestHeader annotation = methodParameter.getParameterAnnotation(RequestHeader.class);
        Assert.state(annotation != null, "No RequestHeader annotation");
        String name = annotation.name();
        if (!StringUtils.hasText(name)) {
            name = annotation.value();
        }
        return new ExtendedRequestHeaderNamedValueInfo(name, annotation.required(), annotation.defaultValue());
    }
}
