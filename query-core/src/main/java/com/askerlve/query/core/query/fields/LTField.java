package com.askerlve.query.core.query.fields;

import com.askerlve.query.core.query.annotation.LT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * LTField
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public class LTField extends AbstractQueryField {
    public LTField(String groupName, Field field, Annotation annotation) {
        super(groupName, field, annotation);
    }

    @Override
    public <T> void doBuildCondition(QueryWrapper<T> query, Object val) {
        LT ltAnnotation = (LT) getAnnotation();
        query.lt(ltAnnotation.field(), val);
    }
}
