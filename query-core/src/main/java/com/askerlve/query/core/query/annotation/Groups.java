package com.askerlve.query.core.query.annotation;

import java.lang.annotation.*;

/**
 * 条件组聚合
 *
 * @author asker_lve
 * @date 2021/8/17 14:34
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Groups {
    Group[] groups() default {};
}
