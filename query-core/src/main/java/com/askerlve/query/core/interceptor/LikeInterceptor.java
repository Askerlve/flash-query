package com.askerlve.query.core.interceptor;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.github.yulichang.wrapper.MPJAbstractWrapper;
import com.google.common.escape.CharEscaperBuilder;
import com.google.common.escape.Escaper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * MYSQL Like特殊字符处理
 *
 * @author asker_lve
 * @date 2021/8/17 14:34
 */
@ConditionalOnClass(name = {"com.mysql.jdbc.Driver", "com.mysql.cj.jdbc.Driver"})
@Component
@Slf4j
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,
                        ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,
                        ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
public class LikeInterceptor implements Interceptor {
    private static final Escaper ESCAPER = new CharEscaperBuilder()
            .addEscape('%', "\\%")
            .addEscape('_', "\\_")
            .addEscape('\\', "\\\\")
            .toEscaper();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 拦截sql
        Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = statement.getBoundSql(parameterObject);
        String sql = boundSql.getSql();
        // 处理特殊字符
        modifyLikeSql(sql, parameterObject, boundSql);
        // 返回
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    @SuppressWarnings("unchecked")
    private String modifyLikeSql(String sql, Object parameterObject, BoundSql boundSql) {
        if (!(parameterObject instanceof HashMap)) {
            return sql;
        }
        if (!sql.toLowerCase().contains(" like ") || !sql.toLowerCase().contains("?")) {
            return sql;
        }
        Boolean updateFlag = UpdateFlagHolder.get();
        if (Objects.nonNull(updateFlag) && updateFlag) {
            return sql;
        }
        UpdateFlagHolder.put(true);

        // 获取关键字的个数（去重）
        String[] strList = sql.split("\\?");
        Set<String> keyNames = new HashSet<>();
        for (int i = 0; i < strList.length; i++) {
            if (strList[i].toLowerCase().contains(" like ")) {
                String keyName = boundSql.getParameterMappings().get(i).getProperty();
                keyNames.add(keyName);
            }
        }
        // 对关键字进行特殊字符“清洗”，如果有特殊字符的，在特殊字符前添加转义字符（\）
        for (String keyName : keyNames) {
            HashMap parameter = (HashMap) parameterObject;
            if (keyName.contains("ew.paramNameValuePairs.") && sql.toLowerCase().contains(" like ?")) {
                // 第一种情况：在业务层进行条件构造产生的模糊查询关键字
                Wrapper wrapper = (Wrapper) parameter.get("ew");
                if (wrapper instanceof AbstractWrapper) {
                    parameter = (HashMap) ((AbstractWrapper) wrapper).getParamNameValuePairs();
                } else if (wrapper instanceof MPJAbstractWrapper) {
                    parameter = (HashMap) ((MPJAbstractWrapper) wrapper).getParamNameValuePairs();
                } else {
                    throw new RuntimeException("not support wrapper type: " + wrapper.getClass());
                }

                String[] keyList = keyName.split("\\.");
                // ew.paramNameValuePairs.MPGENVAL1，截取字符串之后，获取第三个，即为参数名
                Object a = parameter.get(keyList[2]);
                if (a instanceof String && (a.toString().contains("_") || a.toString().contains("\\") || a.toString()
                        .contains("%"))) {
                    //SqlLike DEFAULT
                    if (a.toString().startsWith("%") && a.toString().endsWith("%")) {
                        parameter.put(keyList[2],
                                "%" + ESCAPER.escape(a.toString().substring(1, a.toString().length() - 1)) + "%");
                    }
                    //SqlLike LEFT
                    else if (a.toString().startsWith("%")) {
                        parameter.put(keyList[2], "%" + ESCAPER.escape(a.toString().substring(1)));
                    }
                    //SqlLike RIGHT
                    else if (a.toString().endsWith("%")) {
                        parameter.put(keyList[2],
                                ESCAPER.escape(a.toString().substring(0, a.toString().length() - 1)) + "%");
                    }
                }
            } else if (!keyName.contains("ew.paramNameValuePairs.") && sql.toLowerCase().contains(" like ?")) {
                // 第二种情况：未使用条件构造器，但是在service层进行了查询关键字与模糊查询符`%`手动拼接
                Object a = parameter.get(keyName);
                if (a instanceof String && (a.toString().contains("_") || a.toString().contains("\\") || a.toString()
                        .contains("%"))) {
                    //SqlLike DEFAULT
                    if (a.toString().startsWith("%") && a.toString().endsWith("%")) {
                        parameter.put(keyName,
                                "%" + ESCAPER.escape(a.toString().substring(1, a.toString().length() - 1)) + "%");
                    }
                    //SqlLike LEFT
                    else if (a.toString().startsWith("%")) {
                        parameter.put(keyName, "%" + ESCAPER.escape(a.toString().substring(1)));
                    }
                    //SqlLike RIGHT
                    else if (a.toString().endsWith("%")) {
                        parameter.put(keyName,
                                ESCAPER.escape(a.toString().substring(0, a.toString().length() - 1)) + "%");
                    }
                }
            } else {
                // 第三种情况：在Mapper类的注解SQL中进行了模糊查询的拼接
                Object a = parameter.get(keyName);
                if (a instanceof String && (a.toString().contains("_") || a.toString().contains("\\") || a.toString()
                        .contains("%"))) {
                    parameter.put(keyName, ESCAPER.escape(a.toString()));
                }
            }
        }
        return sql;
    }
}
