package com.askerlve.query.core.query.annotation;

import com.askerlve.query.core.query.fields.NEField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * NE
 *
 * @author asker_lve
 * @date 2021/4/21 17:35
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@QueryFieldClazz(clazz = NEField.class, annotationClazz = NE.class)
public @interface NE {
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

    /**
     * 是否忽略空值，例如空字符串等
     *
     * @return
     */
    boolean ignoreEmpty() default true;
}
