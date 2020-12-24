package com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Description 动态数据源切换的切面，切 DAO 层，通过 DAO 层方法名判断使用哪个数据源，实现数据源切换
 * @Author ZRD
 * @Date 2020/12/19
 */
@Aspect
@Component
public class DynamicDataSourceAspect {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    private final String[] QUERY_PREFIX = {"select","query","get"};

    /**
     * DAO 层切面
     * @Author: ZRD
     * @Date: 2020/12/19
     * @return: void
     **/
    @Pointcut("execution( * com.zrd.dynamicdatasource.springbootdynamicdsrawseparation.mapper.*.*(..))")
    public void daoAspect() {
    }

    /**
     * 轮询数据源
     * @Author: ZRD
     * @Date: 2020/12/19
     * @param point:
     * @return: void
     **/
    @Before("daoAspect()")
    public void switchDataSource(JoinPoint point) {
        Boolean isQueryMethod = isQueryMethod(point.getSignature().getName());
        if (isQueryMethod) {
            DynamicDataSourceContextHolder.useSecondaryReadDataSource();
            logger.info("------Switch DataSource to [{}] in Method [{}]",
                    DynamicDataSourceContextHolder.getDataSourceKey(), point.getSignature());
        }
    }

    /**
     * 恢复数据源
     * @Author: ZRD
     * @Date: 2020/12/19
     * @param point:
     * @return: void
     **/
    @After("daoAspect())")
    public void restoreDataSource(JoinPoint point) {
        DynamicDataSourceContextHolder.clearDataSourceKey();
        logger.info("------Restore DataSource to [{}] in Method [{}]",
                DynamicDataSourceContextHolder.getDataSourceKey(), point.getSignature());
    }


    /**
     * 判断方式是查询方法
     * @Author: ZRD
     * @Date: 2020/12/19
     * @param methodName:
     * @return: java.lang.Boolean
     **/
    private Boolean isQueryMethod(String methodName) {
        for (String prefix : QUERY_PREFIX) {
            if (methodName.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

}
