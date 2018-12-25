package com.aihuishou.c2b.service.common.config.web.servlet.support;

import com.aihuishou.c2b.service.common.config.web.servlet.util.MethodArgumentResolverUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.method.annotation.ExtendedCookieValueNamedValueInfo;
import org.springframework.web.servlet.mvc.method.annotation.ServletCookieValueMethodArgumentResolver;

/**
 * Extend {@link ServletCookieValueMethodArgumentResolver} to resolve problem
 * that Spring MVC can't support param annotation {@link CookieValue} on interface
 *
 * <note>
 * 处理{@link CookieValue}仍然由{@link ServletCookieValueMethodArgumentResolver}处理
 * 该扩展类只是当类上的方法不含有{@link CookieValue}注解的时候，从接口中获取
 * </note>
 *
 * @author jiashuai.xie
 * @since 2018/12/24 10:47
 */
public class ExtendedServletCookieValueMethodArgumentResolver extends ServletCookieValueMethodArgumentResolver {

    public ExtendedServletCookieValueMethodArgumentResolver(ConfigurableBeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        MethodParameter methodParameter = MethodArgumentResolverUtils.detectAvailableMethodParameter(parameter, CookieValue.class);
        return super.supportsParameter(methodParameter);
    }

    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        MethodParameter methodParameter = MethodArgumentResolverUtils.detectAvailableMethodParameter(parameter, CookieValue.class);
        CookieValue annotation = methodParameter.getParameterAnnotation(CookieValue.class);
        Assert.state(annotation != null, "No CookieValue annotation");
        String name = annotation.name();
        if (!StringUtils.hasText(name)) {
            name = annotation.value();
        }
        return new ExtendedCookieValueNamedValueInfo(name, annotation.required(), annotation.defaultValue());
    }


}
