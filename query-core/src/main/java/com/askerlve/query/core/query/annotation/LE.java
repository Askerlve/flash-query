package com.askerlve.query.core.query.annotation;

import com.askerlve.query.core.query.fields.LEField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * LE
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@QueryFieldClazz(clazz = LEField.class, annotationClazz = LE.class)
public @interface LE {

    /**
     * 数据库字断
     *
     * @return String
     */
    String field() default "";

    /**
     * 条件组ID
     *
     * @return java.lang.String
     */
    String groupName() default "";
}
