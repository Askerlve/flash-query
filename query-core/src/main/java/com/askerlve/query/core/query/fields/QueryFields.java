package com.askerlve.query.core.query.fields;

import com.askerlve.query.core.query.annotation.QueryFieldClazz;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Optional;

/**
 * QueryFields
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public class QueryFields {
    private QueryFields() {

    }

    /**
     * QueryField工厂方法
     *
     * @param field
     * @return
     */
    public static QueryField of(Field field) {
        QueryFieldClazz queryFieldClazz = AnnotationUtils.findAnnotation(field, QueryFieldClazz.class);
        if (queryFieldClazz == null) {
            return null;
        }
        Annotation queryAnnotation = AnnotationUtils.findAnnotation(field, queryFieldClazz.annotationClazz());
        if (queryAnnotation == null) {
            throw new RuntimeException("Annotation " + queryFieldClazz.annotationClazz() + " not found on field "
                    + field);
        }

        Optional<Method> groupNameOp = Arrays.stream(queryAnnotation.annotationType().getMethods())
                .filter(f -> f.getName().equals("groupName"))
                .findFirst();
        String groupName = "";
        if (groupNameOp.isPresent()) {
            try {
                groupName = (String)groupNameOp.get().invoke(queryAnnotation);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException("field groupName fetch fail, annotation:" + queryAnnotation, e);
            }
        }

        Class<? extends QueryField> clazz = queryFieldClazz.clazz();
        Constructor<? extends QueryField> constructor;
        try {
            constructor = clazz.getConstructor(String.class, Field.class, Annotation.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("constructor not found, clazz: " + clazz
                    + ", params: String, Field, Annotation");
        }

        try {
            return constructor.newInstance(groupName, field, queryAnnotation);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("construct " + clazz + " fail", e);
        }
    }

    private static <T extends Annotation> T findAnnotation(AnnotatedElement element, Class<T> clazz) {
        Annotation[] annotations = element.getAnnotations();
        for (Annotation annotation : annotations) {
            if (clazz.equals(annotation.annotationType())) {
                return clazz.cast(annotation);
            }

            if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotation)) {
                return findAnnotation(annotation.annotationType(), clazz);
            }
        }

        return null;
    }
}
