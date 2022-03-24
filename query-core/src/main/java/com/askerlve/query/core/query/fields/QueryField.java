package com.askerlve.query.core.query.fields;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * QueryField
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public interface QueryField {
    <D, T> void buildCondition(QueryWrapper<T> query, D param);

    String getGroupName();

    Field getField();

    Annotation getAnnotation();
}
