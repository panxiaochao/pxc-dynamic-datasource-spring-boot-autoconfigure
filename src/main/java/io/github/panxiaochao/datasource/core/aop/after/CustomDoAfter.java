package io.github.panxiaochao.datasource.core.aop.after;

import io.github.panxiaochao.datasource.core.holder.DynamicDataSourceContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * {@code CustomDoAfter}
 * <p>后置环绕
 *
 * @author Lypxc
 * @since 2022/1/6
 */
public class CustomDoAfter implements AfterReturningAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomDoAfter.class);

    /**
     * 后置清楚数据源
     *
     * @param o
     * @param method
     * @param args
     * @param target
     */
    @Override
    public void afterReturning(Object o, Method method, Object[] args, Object target) {
        LOGGER.info(">>> after clear datesource " + DynamicDataSourceContextHolder.get());
        DynamicDataSourceContextHolder.clear();
    }
}