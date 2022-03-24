package com.askerlve.query.core.query.fields;

import com.askerlve.query.core.query.annotation.GE;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * GEField
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public class GEField extends AbstractQueryField {
    public GEField(String groupName, Field field, Annotation annotation) {
        super(groupName, field, annotation);
    }

    @Override
    public <T> void doBuildCondition(QueryWrapper<T> query, Object val) {
        GE geAnnotation = (GE)getAnnotation();
        query.ge(geAnnotation.field(), val);
    }
}
