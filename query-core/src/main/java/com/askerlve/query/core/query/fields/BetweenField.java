package com.askerlve.query.core.query.fields;

import com.askerlve.query.core.query.BetweenQuery;
import com.askerlve.query.core.query.annotation.BETWEEN;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * BetweenField
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public class BetweenField extends AbstractQueryField {
    public BetweenField(String groupName, Field field, Annotation annotation) {
        super(groupName, field, annotation);
    }

    @Override
    public <T> void doBuildCondition(QueryWrapper<T> query, Object val) {
        BETWEEN annotation = (BETWEEN) getAnnotation();

        if (!(val instanceof BetweenQuery)) {
            throw new RuntimeException("between query value type invalid! value type must instance of BetweenQuery");
        }
        BetweenQuery betweenQueryValue = (BetweenQuery) val;

        query.between(annotation.field(), betweenQueryValue.getStart(), betweenQueryValue.getEnd());
    }
}
