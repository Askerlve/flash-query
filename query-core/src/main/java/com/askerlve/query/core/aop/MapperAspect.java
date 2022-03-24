package com.askerlve.query.core.aop;

import com.askerlve.query.core.interceptor.UpdateFlagHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * MapperAspect
 *
 * @author asker_lve
 * @date 2021/8/17 14:34
 */
@Slf4j
@Aspect
@Component
public class MapperAspect {

    @Around("execution(public * com.github.yulichang.base.MPJBaseMapper.*(..))")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        try {
            UpdateFlagHolder.put(false);
            return proceedingJoinPoint.proceed();
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        } finally {
            UpdateFlagHolder.clear();
        }
    }

}
