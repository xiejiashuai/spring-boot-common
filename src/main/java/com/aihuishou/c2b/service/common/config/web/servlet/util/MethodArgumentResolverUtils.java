package com.aihuishou.c2b.service.common.config.web.servlet.util;

import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.TimeZone;

/**
 * Utility class to get <code>MethodParameter</code> from interface
 *
 * @author jiashuai.xie
 * @since 2018/12/24 10:34
 */
public abstract class MethodArgumentResolverUtils {

    /**
     * 获取含有含有annotationType类型的注解的parameter
     *
     * <note>
     * 如果传递的parameter上含有annotationType类型的注解，则直接返回该parameter
     * 不然，获取接口上含有annotationType类型的注解的MethodParameter并且返回
     * </note>
     *
     * @param parameter      方法参数
     * @param annotationType 注解类型
     * @return 原parameter或者parameter所在接口的MethodParameter
     */
    public static MethodParameter detectAvailableMethodParameter(MethodParameter parameter, Class annotationType) {

        // 如果parameter含有注解则直接返回 否则进行下一步操作
        if (!parameter.hasParameterAnnotation(annotationType)) {
            // 获取parameter所在的类的所有接口
            for (Class<?> itf : parameter.getDeclaringClass().getInterfaces()) {
                try {
                    // 从接口中获取parameter的方法
                    Method method = itf.getMethod(parameter.getMethod().getName(), parameter.getMethod().getParameterTypes());

                    // 构造接口parameter
                    MethodParameter itfParameter = new MethodParameter(method, parameter.getParameterIndex());
                    // 如果含有annotationType注解 返回接口parameter
                    if (itfParameter.hasParameterAnnotation(annotationType)) {
                        // 处理 @AliasFor 行不通
//                        Annotation annotation = AnnotationUtils.synthesizeAnnotation(itfParameter.getParameterAnnotation(annotationType), method.getParameters()[parameter.getParameterIndex()]);
                        return itfParameter;
                    }
                } catch (NoSuchMethodException e) {

                    // ignore 从下个接口中寻找
                }
            }
        }
        return parameter;
    }

}
