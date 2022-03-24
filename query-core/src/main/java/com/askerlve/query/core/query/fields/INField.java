package com.askerlve.query.core.query.fields;

import com.askerlve.query.core.query.annotation.IN;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * INField
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public class INField extends AbstractQueryField {
    public INField(String groupName, Field field, Annotation annotation) {
        super(groupName, field, annotation);
    }

    @Override
    public <T> void doBuildCondition(QueryWrapper<T> query, Object val) {
        IN inAnnotation = (IN) getAnnotation();
        if (ClassUtils.isAssignable(val.getClass(), String.class)) {
            query.in(inAnnotation.field(), ((String) val).split("\\,"));
        } else if (ClassUtils.isAssignable(val.getClass(), Collection.class)) {
            query.in(inAnnotation.field(), ((Collection) val));
        } else if (val.getClass().isArray()) {
            query.in(inAnnotation.field(), val);
        }
    }
}
