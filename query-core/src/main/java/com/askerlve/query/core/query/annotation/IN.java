package com.askerlve.query.core.query.annotation;

import com.askerlve.query.core.query.fields.INField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * IN
 *
 * @author asker_lve
 * @date 2021/4/21 17:37
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@QueryFieldClazz(clazz = INField.class, annotationClazz = IN.class)
public @interface IN {

    /**
     * 数据库字段
     *
     * @return java.lang.String
     */
    String field() default "";

    /**
     * 条件组ID
     *
     * @return java.lang.String
     */
    String groupName() default "";
}
