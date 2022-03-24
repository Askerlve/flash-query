package com.askerlve.query.core.query.fields;

import com.askerlve.query.core.query.annotation.GT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * GTField
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public class GTField extends AbstractQueryField {
    public GTField(String groupName, Field field, Annotation annotation) {
        super(groupName, field, annotation);
    }

    @Override
    public <T> void doBuildCondition(QueryWrapper<T> query, Object val) {
        GT gtAnnotation = (GT) getAnnotation();
        query.gt(gtAnnotation.field(), val);
    }
}
