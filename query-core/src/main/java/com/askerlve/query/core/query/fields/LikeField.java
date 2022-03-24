package com.askerlve.query.core.query.fields;

import com.askerlve.query.core.query.annotation.LIKE;
import com.askerlve.query.core.query.enums.SqlLike;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * LikeField
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public class LikeField extends AbstractQueryField {
    public LikeField(String groupName, Field field, Annotation annotation) {
        super(groupName, field, annotation);
    }

    @Override
    public <T> void doBuildCondition(QueryWrapper<T> query, Object val) {
        LIKE likeAnnotation = (LIKE) getAnnotation();

        SqlLike sqlLike = likeAnnotation.type();
        switch (sqlLike) {
            case LEFT:
                query.likeLeft(likeAnnotation.field(), val);
                break;
            case RIGHT:
                query.likeRight(likeAnnotation.field(), val);
                break;
            case DEFAULT:
            default:
                query.like(likeAnnotation.field(), val);
                break;
        }
    }
}
