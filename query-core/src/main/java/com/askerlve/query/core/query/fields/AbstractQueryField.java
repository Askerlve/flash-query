package com.askerlve.query.core.query.fields;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.logging.log4j.util.Strings;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * AbstractQueryField
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public abstract class AbstractQueryField implements QueryField {
    private String groupName;
    private Field field;
    private Annotation annotation;

    public AbstractQueryField(String groupName, Field field, Annotation annotation) {
        this.groupName = groupName;
        this.field = field;
        this.annotation = annotation;
    }

    @Override
    public <D, T> void buildCondition(QueryWrapper<T> query, D param) {
        field.setAccessible(true);
        Object val = null;
        try {
            val = field.get(param);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("field: " + field + ", param: " + param, e);
        }
        if (Objects.nonNull(annotation)) {
            if (!ignoreEmptyValue()) {
                doBuildCondition(query, val);
            } else if (checkValue(val)) {
                doBuildCondition(query, val);
            }
        }
    }

    public abstract <T> void doBuildCondition(QueryWrapper<T> query, Object val);

    public boolean ignoreEmptyValue() {
        return true;
    }

    @Override
    public String getGroupName() {
        return groupName;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public Annotation getAnnotation() {
        return annotation;
    }

    /**
     * 检查val值
     *
     * @param val
     * @return
     */
    protected static boolean checkValue(Object val) {
        if (Objects.isNull(val)) {
            return false;
        }

        if (val instanceof String && Strings.isBlank(val.toString())) {
            return false;
        }

        if (val.getClass().isArray() && ((Object[]) val).length == 0) {
            return false;
        }

        if (val instanceof List && ((List<Object>) val).size() == 0) {
            return false;
        }

        return true;
    }
}
