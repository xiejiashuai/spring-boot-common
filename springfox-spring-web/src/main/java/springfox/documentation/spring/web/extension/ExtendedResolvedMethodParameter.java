package springfox.documentation.spring.web.extension;

import com.fasterxml.classmate.ResolvedType;
import org.springframework.core.MethodParameter;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import springfox.documentation.service.ResolvedMethodParameter;

import javax.validation.Valid;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Extend {@link ResolvedMethodParameter}
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

    /**
     * get Annotation from interface methodParameter
     *
     * @param methodParameter
     * @return
     */
    public static List<Annotation> interfaceAnnotations(MethodParameter methodParameter) {

        List<Annotation> annotationList = new ArrayList<>();
        // exclude @Valid @Validated on class method(only contain @Valid or @Validated annotation on that method)
        //  @see OperationParameterReader#shouldExpand(...)
        List<Annotation> annotations = Arrays.stream(methodParameter.getParameterAnnotations())
                .filter(annotation -> !Valid.class.isInstance(annotation))
                .filter(annotation -> !Validated.class.isInstance(annotation))
                .collect(Collectors.toList());

        annotationList.addAll(annotations);

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