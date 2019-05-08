package com.aihuishou.c2b.service.common.config.web.servlet.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.SpringQueryMap;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static com.aihuishou.c2b.service.common.config.web.servlet.util.MethodArgumentResolverUtils.detectAvailableMethodParameter;
import static java.util.stream.Collectors.toList;

/**
 * Resolves method arguments annotated with an @{@link SpringQueryMap}.
 *
 * @author jiashuai.xie
 * @since 2018/12/25 18:47 1.0.0.RELEASE
 */
public class SpringQueryMapMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringQueryMapMethodArgumentResolver.class);

    private final Validator validator;

    private final ObjectMapper objectMapper;

    public SpringQueryMapMethodArgumentResolver(Validator validator, ObjectMapper objectMapper) {
        this.validator = validator;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        MethodParameter methodParameter = detectAvailableMethodParameter(parameter, SpringQueryMap.class);

        return methodParameter.hasParameterAnnotation(SpringQueryMap.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        Class<?> parameterType = parameter.getParameterType();

        Field[] declaredFields = parameterType.getDeclaredFields();

        List<String> filedNames = Arrays.stream(declaredFields).map(Field::getName).collect(toList());

        Map<String, Object> map = new HashMap<>(16);

        filedNames.forEach(name -> {
            try {
                String[] paramValues = webRequest.getParameterValues(name);
                map.put(name, paramValues[0]);
            } catch (Exception e) {
                // ignore
            }
        });


        try {

            Object object = objectMapper.readValue(objectMapper.writeValueAsString(map), parameterType);

            Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);

            // validator failed
            if (!CollectionUtils.isEmpty(constraintViolations)) {
                StringBuilder sb = new StringBuilder();
                constraintViolations.forEach(constraintViolation -> sb.append(constraintViolation.getMessage()).append("\n"));
                throw new ValidationException(sb.toString());
            }

            return object;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
