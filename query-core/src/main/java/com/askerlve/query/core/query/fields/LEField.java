package com.askerlve.query.core.query.fields;

import com.askerlve.query.core.query.annotation.LE;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * LEField
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public class LEField extends AbstractQueryField {
    public LEField(String groupName, Field field, Annotation annotation) {
        super(groupName, field, annotation);
    }

    @Override
    public <T> void doBuildCondition(QueryWrapper<T> query, Object val) {
        LE leAnnotation = (LE) getAnnotation();
        query.le(leAnnotation.field(), val);
    }
}
