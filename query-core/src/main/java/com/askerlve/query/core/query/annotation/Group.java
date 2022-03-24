package com.askerlve.query.core.query.annotation;

import com.askerlve.query.core.query.enums.GroupType;

import java.lang.annotation.*;

/**
 * 条件组
 *
 * @author asker_lve
 * @date 2021/8/17 14:34
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Group {

    /**
     * 条件组联合类型，and或者or
     *
     * @return java.lang.String
     */
    GroupType type() default GroupType.and;

    /**
     * 条件组ID, ID相同的条件放入一组
     *
     * @return
     */
    String name() default "";

    /**
     * 条件组ID
     *
     * @return java.lang.String
     */
    String groupName() default "";
}
