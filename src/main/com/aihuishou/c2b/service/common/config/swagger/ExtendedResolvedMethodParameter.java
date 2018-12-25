package com.aihuishou.c2b.service.common.config.swagger;

import com.fasterxml.classmate.ResolvedType;
import org.springframework.core.MethodParameter;
import org.springframework.util.CollectionUtils;
import springfox.documentation.service.ResolvedMethodParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO-描述性注释
 *
 * @author jiashuai.xie
 * @since 2018/12/24 21:47
 */
public class ExtendedResolvedMethodParameter extends ResolvedMethodParameter {

    public ExtendedResolvedMethodParameter(String paramName, MethodParameter methodParameter, ResolvedType parameterType) {
        this(methodParameter.getParameterIndex(),
                paramName,
                interfaceAnnotations(methodParameter),
                parameterType);
    }

    public ExtendedResolvedMethodParameter(int parameterIndex, String defaultName, List<Annotation> annotations, ResolvedType parameterType) {
        super(parameterIndex, defaultName, annotations, parameterType);
    }

    public static List<Annotation> interfaceAnnotations(MethodParameter methodParameter) {
        List<Annotation> annotationList = new ArrayList<>();
        annotationList.addAll(Arrays.asList(methodParameter.getParameterAnnotations()));

        if (CollectionUtils.isEmpty(annotationList)) {
            for (Class<?> itf : methodParameter.getDeclaringClass().getInterfaces()) {
                try {
                    Method method = itf.getMethod(methodParameter.getMethod().getName(), methodParameter.getMethod().getParameterTypes());
                    MethodParameter itfParameter = new MethodParameter(method, methodParameter.getParameterIndex());
                    annotationList.addAll(Arrays.asList(itfParameter.getParameterAnnotations()));

                } catch (NoSuchMethodException e) {

                }
            }
        }

        return annotationList;
    }

}