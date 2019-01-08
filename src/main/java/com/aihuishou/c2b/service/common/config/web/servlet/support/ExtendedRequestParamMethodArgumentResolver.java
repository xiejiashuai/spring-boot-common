package com.aihuishou.c2b.service.common.config.web.servlet.support;

import com.aihuishou.c2b.service.common.config.web.servlet.util.MethodArgumentResolverUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.annotation.ExtendedRequestParamNamedValueInfo;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;

/**
 * Extend {@link RequestParamMethodArgumentResolver} to resolve problem
 * that Spring MVC can't support param annotation {@link RequestParam} on interface
 *
 * <note>
 * 处理{@link RequestParam}仍然由{@link RequestParamMethodArgumentResolver}处理
 * 该扩展类只是当类上的方法不含有{@link RequestParam}注解的时候，从接口中获取
 * </note>
 *
 * @author jiashuai.xie
 * @since 2018/12/28 19:10 1.0.0.RELEASE
 */
public class ExtendedRequestParamMethodArgumentResolver extends RequestParamMethodArgumentResolver {


    public ExtendedRequestParamMethodArgumentResolver(ConfigurableBeanFactory beanFactory, boolean useDefaultResolution) {
        super(beanFactory, useDefaultResolution);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        MethodParameter methodParameter = MethodArgumentResolverUtils.detectAvailableMethodParameter(parameter, RequestParam.class);
        return super.supportsParameter(methodParameter);
    }

    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        MethodParameter methodParameter = MethodArgumentResolverUtils.detectAvailableMethodParameter(parameter, RequestParam.class);
        RequestParam annotation = methodParameter.getParameterAnnotation(RequestParam.class);
        Assert.state(annotation != null, "No RequestParam annotation");
        String name = annotation.name();
        if (!StringUtils.hasText(name)) {
            name = annotation.value();
        }
        return new ExtendedRequestParamNamedValueInfo(name, annotation.required(), annotation.defaultValue());
    }

}
