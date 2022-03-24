package com.askerlve.query.core.query.fields;

import com.askerlve.query.core.query.annotation.NE;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * NEField
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public class NEField extends AbstractQueryField {
    public NEField(String groupName, Field field, Annotation annotation) {
        super(groupName, field, annotation);
    }

    @Override
    public <T> void doBuildCondition(QueryWrapper<T> query, Object val) {
        NE neAnnotation = (NE) getAnnotation();
        query.ne(neAnnotation.field(), val);
    }

    @Override
    public boolean ignoreEmptyValue() {
        NE neAnnotation = (NE) getAnnotation();

        return neAnnotation.ignoreEmpty();
    }
}
