package com.askerlve.query.core.query.annotation;

import com.askerlve.query.core.query.fields.QueryField;

import java.lang.annotation.*;

/**
 * RANGE
 *
 * @author asker_lve
 * @date 2021/4/21 17:35
 */
@Target(value = {ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryFieldClazz {
    /**
     * 注解处理器class
     *
     * @return
     */
    Class<? extends QueryField> clazz();

    /**
     * 注解class
     *
     * @return
     */
    Class<? extends Annotation> annotationClazz();
}
