package com.askerlve.query.core.query.annotation;

import com.askerlve.query.core.query.fields.BetweenField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * BETWEEN
 *
 * @author asker_lve
 * @date 2021/8/17 14:34
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@QueryFieldClazz(clazz = BetweenField.class, annotationClazz = BETWEEN.class)
public @interface BETWEEN {

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
