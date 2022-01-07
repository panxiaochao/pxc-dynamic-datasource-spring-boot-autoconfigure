package io.github.panxiaochao.aop.after;

import io.github.panxiaochao.multiple.DbSourceContextHolder;
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

    @Override
    public void afterReturning(Object o, Method method, Object[] args, Object target) throws Throwable {
        LOGGER.info(">>> after clear datesource {}", DbSourceContextHolder.get());
        DbSourceContextHolder.clear();
    }
}
