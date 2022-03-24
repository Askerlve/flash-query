package com.askerlve.query.core.query.fields;

import com.askerlve.query.core.query.RangeQuery;
import com.askerlve.query.core.query.annotation.RANGE;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * RangeField
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public class RangeField extends AbstractQueryField {
    public RangeField(String groupName, Field field, Annotation annotation) {
        super(groupName, field, annotation);
    }

    @Override
    public <T> void doBuildCondition(QueryWrapper<T> query, Object val) {
        RANGE annotation = (RANGE) getAnnotation();

        if (!(val instanceof RangeQuery)) {
            throw new RuntimeException("range query value type invalid! value type must instance of RangeQuery");
        }
        RangeQuery rangeQueryVal = (RangeQuery) val;
        if (Objects.isNull(rangeQueryVal.getStart()) && Objects.isNull(rangeQueryVal.getEnd())) {
            return;
        }

        if (Objects.nonNull(rangeQueryVal.getStart()) && Objects.nonNull(rangeQueryVal.getEnd())) {
            query.ge(annotation.field(), rangeQueryVal.getStart()).le(annotation.field(), rangeQueryVal.getEnd());
        } else if (Objects.nonNull(rangeQueryVal.getStart())) {
            query.ge(annotation.field(), rangeQueryVal.getStart());
        } else if (Objects.nonNull(rangeQueryVal.getEnd())) {
            query.le(annotation.field(), rangeQueryVal.getEnd());
        }
    }
}
