package com.aihuishou.c2b.service.common.config.feign;

import feign.MethodMetadata;
import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;
import org.springframework.web.method.annotation.SpringQueryMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * {@link SpringQueryMap} parameter processor.
 *
 * @author Aram Peres
 * @see AnnotatedParameterProcessor
 */
public class SpringQueryMapParameterProcessor implements AnnotatedParameterProcessor {

    private static final Class<SpringQueryMap> ANNOTATION = SpringQueryMap.class;

    @Override
    public Class<? extends Annotation> getAnnotationType() {
        return ANNOTATION;
    }

    @Override
    public boolean processArgument(AnnotatedParameterContext context, Annotation annotation, Method method) {
        int paramIndex = context.getParameterIndex();
        MethodMetadata metadata = context.getMethodMetadata();
        if (metadata.queryMapIndex() == null) {
            metadata.queryMapIndex(paramIndex);
            metadata.queryMapEncoded(SpringQueryMap.class.cast(annotation).encoded());
        }
        return true;
    }
}