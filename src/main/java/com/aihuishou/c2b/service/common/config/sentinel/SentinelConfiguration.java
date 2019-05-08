package com.aihuishou.c2b.service.common.config.sentinel;

import com.aihuishou.c2b.service.common.config.sentinel.servlet.callback.CustomizerRequestOriginParser;
import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlCleaner;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.csp.sentinel.log.LogBase;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.alibaba.sentinel.custom.SentinelAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

/**
 * @author jiashuai.xie
 */
@Configuration
@AutoConfigureAfter(WebMvcConfigurationSupport.class)
@AutoConfigureBefore(SentinelAutoConfiguration.class)
@ConditionalOnClass(SentinelAutoConfiguration.class)
@ConditionalOnWebApplication(type = SERVLET)
public class SentinelConfiguration implements ApplicationRunner, ApplicationContextAware, EnvironmentAware {

    private ApplicationContext applicationContext;

    /**
     * All {@link RequestMapping} Url in the container
     */
    private List<String> mappingUrls;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private ConfigurableEnvironment environment;

    static {
        WebCallbackManager.setRequestOriginParser(new CustomizerRequestOriginParser());
    }

    @PostConstruct
    public void init() {

        String logDir = environment.getProperty(LogBase.LOG_DIR, "");

        if (StringUtils.hasText(logDir)) {
            System.setProperty(LogBase.LOG_DIR, logDir);
        }

    }

    @Bean
    @ConditionalOnMissingBean
    public UrlBlockHandler urlBlockHandler() {

        return (request, response, ex) -> {

            throw new FlowControlException(ex.getRuleLimitApp() + "is limited by sentinel");

        };
    }

    @Bean
    @ConditionalOnMissingBean
    public UrlCleaner urlCleaner() {

        return originUrl -> {

            Optional<String> urlOptional = mappingUrls.stream().filter(urlPattern -> antPathMatcher.match(urlPattern, originUrl)).findFirst();

            return urlOptional.orElse(originUrl);

        };
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);

        List<String> urls = handlerMapping.getHandlerMethods().keySet().stream().map(requestMappingInfo -> toString(requestMappingInfo.getPatternsCondition())).collect(Collectors.toList());

        this.mappingUrls = Collections.unmodifiableList(urls);
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }

    private String toString(PatternsRequestCondition patternsRequestCondition) {
        StringBuilder builder = new StringBuilder();
        for (Iterator<?> iterator = patternsRequestCondition.getPatterns().iterator(); iterator.hasNext(); ) {
            Object expression = iterator.next();
            builder.append(expression.toString());
            if (iterator.hasNext()) {
                builder.append(getToStringInfix());
            }
        }
        return builder.toString();
    }

    private String getToStringInfix() {
        return " || ";
    }


}
