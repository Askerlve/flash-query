package com.askerlve.query.core.query.fields;

import com.askerlve.query.core.query.annotation.KEYWORD;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * KeywordField
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public class KeywordField extends AbstractQueryField {
    public KeywordField(String groupName, Field field, Annotation annotation) {
        super(groupName, field, annotation);
    }

    @Override
    public <T> void doBuildCondition(QueryWrapper<T> query, Object val) {
        KEYWORD keyWordAnnotation = (KEYWORD) getAnnotation();

        String[] keys = keyWordAnnotation.field();
        if (query.isEmptyOfWhere()) {
            for (int i = 0; i < keys.length; i++) {
                if (i != keys.length - 1) {
                    query.like(keys[i], val).or();
                } else {
                    query.like(keys[i], val);
                }
            }
        } else {
            query.and(q -> {
                for (int i = 0; i < keys.length; i++) {
                    if (i != (keys.length - 1)) {
                        q.like(keys[i], val).or();
                    } else {
                        q.like(keys[i], val);
                    }
                }
            });
        }
    }
}
