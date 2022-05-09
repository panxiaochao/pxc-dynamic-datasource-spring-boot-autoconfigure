package io.github.panxiaochao.db.aop.after;

import io.github.panxiaochao.db.logging.Log;
import io.github.panxiaochao.db.logging.LogFactory;
import io.github.panxiaochao.db.multiple.DbSourceContextHolder;
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
    private static final Log LOGGER = LogFactory.getLog(CustomDoAfter.class);

    @Override
    public void afterReturning(Object o, Method method, Object[] args, Object target) throws Throwable {
        LOGGER.info(">>> after clear datesource " + DbSourceContextHolder.get());
        DbSourceContextHolder.clear();
    }
}
