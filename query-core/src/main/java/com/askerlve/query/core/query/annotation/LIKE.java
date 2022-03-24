package com.askerlve.query.core.query.annotation;

import com.askerlve.query.core.query.enums.SqlLike;
import com.askerlve.query.core.query.fields.LikeField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * LIKE
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@QueryFieldClazz(clazz = LikeField.class, annotationClazz = LIKE.class)
public @interface LIKE {

    /**
     * 数据库字段
     *
     * @return java.lang.String
     */
    String field() default "";

    /**
     * like类型
     *
     * @return com.baomidou.mybatisplus.core.enums.SqlLike
     */
    SqlLike type() default SqlLike.DEFAULT;

    /**
     * 条件组ID
     *
     * @return java.lang.String
     */
    String groupName() default "";
}
