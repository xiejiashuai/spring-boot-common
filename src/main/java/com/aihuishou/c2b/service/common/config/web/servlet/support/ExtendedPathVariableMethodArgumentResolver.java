package com.aihuishou.c2b.service.common.config.web.servlet.support;

import com.aihuishou.c2b.service.common.config.web.servlet.util.MethodArgumentResolverUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.method.annotation.ExtendedPathVariableNamedValueInfo;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

/**
 * Extend {@link PathVariableMethodArgumentResolver} to resolve problem
 * that Spring MVC can't support param annotation {@link PathVariable} on interface
 *
 * <note>
 * 处理{@link PathVariable}仍然由{@link PathVariableMethodArgumentResolver}处理
 * 该扩展类只是当类上的方法不含有{@link PathVariable}注解的时候，从接口中获取
 * </note>
 *
 * @author jiashuai.xie
 * @since 2018/12/24 10:29
 */
public class ExtendedPathVariableMethodArgumentResolver extends PathVariableMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        MethodParameter methodParameter = MethodArgumentResolverUtils.detectAvailableMethodParameter(parameter, PathVariable.class);
        return super.supportsParameter(methodParameter);
    }

    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        MethodParameter methodParameter = MethodArgumentResolverUtils.detectAvailableMethodParameter(parameter, PathVariable.class);
        PathVariable annotation = methodParameter.getParameterAnnotation(PathVariable.class);
        Assert.state(annotation != null, "No PathVariable annotation");
        String name = annotation.name();
        if (!StringUtils.hasText(name)) {
            name = annotation.value();
        }
        return new ExtendedPathVariableNamedValueInfo(name, annotation.required(), ValueConstants.DEFAULT_NONE);
    }
}
