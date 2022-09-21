package io.github.panxiaochao.datasource.core.aop.after;

import io.github.panxiaochao.datasource.core.holder.DynamicDataSourceContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * {@code DynamicDataSourceAfterReturningAdvice}
 * <p> 后置通知增强
 *
 * @author Lypxc
 * @since 2022/09/21
 */
public class DynamicDataSourceAfterReturningAdvice implements AfterReturningAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceAfterReturningAdvice.class);

    /**
     * 清除当前线程使用数据源
     *
     * @param o
     * @param method
     * @param args
     * @param target
     */
    @Override
    public void afterReturning(Object o, Method method, Object[] args, Object target) {
        LOGGER.info(">>> after clear dateSource " + DynamicDataSourceContextHolder.get());
        DynamicDataSourceContextHolder.clear();
    }
}
