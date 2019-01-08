package com.aihuishou.c2b.service.common.config.web.servlet.support;

import com.aihuishou.c2b.service.common.config.web.servlet.util.MethodArgumentResolverUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.validation.Valid;
import java.util.List;

/**
 * Extend {@link RequestResponseBodyMethodProcessor} to resolve problem
 * that Spring MVC can't support param annotation {@link } on interface
 *
 * <note>
 * 处理{@link RequestBody}仍然由{@link RequestResponseBodyMethodProcessor}处理
 * 该扩展类只是当类上的方法不含有{@link RequestBody}注解的时候，从接口中获取
 * </note>
 *
 * @author jiashuai.xie
 * @since 2018/12/24 10:53
 */
public class ExtendedRequestResponseBodyMethodProcessor extends RequestResponseBodyMethodProcessor {


    public ExtendedRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters,
                                                      @Nullable ContentNegotiationManager manager,
                                                      @Nullable List<Object> requestResponseBodyAdvice) {

        super(converters, manager, requestResponseBodyAdvice);
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        MethodParameter methodParameter = MethodArgumentResolverUtils.detectAvailableMethodParameter(parameter, RequestBody.class);
        return super.supportsParameter(methodParameter);
    }

    /**
     * 接口支持{@link Valid}验证
     */
    @Override
    protected void validateIfApplicable(WebDataBinder binder, MethodParameter methodParam) {
        MethodParameter methodParameter = MethodArgumentResolverUtils.detectAvailableMethodParameter(methodParam, Valid.class);
        super.validateIfApplicable(binder, methodParameter);
    }

}
