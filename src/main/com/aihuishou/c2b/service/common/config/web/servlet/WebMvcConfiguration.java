package com.aihuishou.c2b.service.common.config.web.servlet;

import com.aihuishou.c2b.service.common.config.web.servlet.support.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;

/**
 * 扩展原生Spring MVC 支持在接口上使用{@link PathVariable} {@link RequestHeader} {@link RequestBody} {@link CookieValue}注解
 *
 * @author jiashuai.xie
 * @since 2018/12/24 10:24
 */
@Configuration
public class WebMvcConfiguration implements BeanFactoryAware {

    @Autowired
    private RequestMappingHandlerAdapter adapter;

    private ConfigurableBeanFactory beanFactory;

    @Autowired
    private Validator validator;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableBeanFactory) beanFactory;
    }

    @PostConstruct
    public void init() {

        // 获取内建的参数解析器
        List<HandlerMethodArgumentResolver> argumentResolvers = adapter.getArgumentResolvers();

        // 新参数解析器
        List<HandlerMethodArgumentResolver> newArgumentResolvers = new ArrayList<>(argumentResolvers);

        // 优先使用扩展后的参数解析器
        newArgumentResolvers.add(0, new ExtendedPathVariableMethodArgumentResolver());
        newArgumentResolvers.add(0, new ExtendedRequestHeaderMethodArgumentResolver(beanFactory));
        newArgumentResolvers.add(0, new ExtendedRequestResponseBodyMethodProcessor(adapter.getMessageConverters()));
        newArgumentResolvers.add(0, new ExtendedServletCookieValueMethodArgumentResolver(beanFactory));
        newArgumentResolvers.add(0, new SpringQueryMapMethodArgumentResolver(validator));

        // 覆盖默认内建参数解析器 支持接口注解
        adapter.setArgumentResolvers(newArgumentResolvers);

    }

}
