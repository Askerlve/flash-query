package com.askerlve.query.core.query.fields;

import com.askerlve.query.core.query.annotation.EQ;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * EQField
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public class EQField extends AbstractQueryField {
    public EQField(String groupName, Field field, Annotation annotation) {
        super(groupName, field, annotation);
    }

    @Override
    public <T> void doBuildCondition(QueryWrapper<T> query, Object val) {
        EQ eqAnnotation = (EQ) getAnnotation();
        query.eq(eqAnnotation.field(), val);
    }

    @Override
    public boolean ignoreEmptyValue() {
        EQ eqAnnotation = (EQ) getAnnotation();

        return eqAnnotation.ignoreEmpty();
    }
}
