package io.github.panxiaochao.datasource.core.aop.before;

import io.github.panxiaochao.datasource.common.annotation.DSource;
import io.github.panxiaochao.datasource.common.enums.DsEnum;
import io.github.panxiaochao.datasource.core.holder.DynamicDataSourceContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * {@code DynamicDataSourceMethodBeforeAdvice}
 * <p> 方法执行前增强
 *
 * @author Lypxc
 * @since 2022/09/21
 */
public class DynamicDataSourceMethodBeforeAdvice implements MethodBeforeAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceMethodBeforeAdvice.class);

    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        LOGGER.info(">>> before dataSource " + DynamicDataSourceContextHolder.get());
        // 默认数据源 master
        String dateSource = DsEnum.MASTER.getName();
        try {
            StringBuilder builderStr = new StringBuilder();
            builderStr.append("Aop pointcut ");
            // 得到访问的方法对象
            if (method.isAnnotationPresent(DSource.class)) {
                // 1.采用最小化优先原则，先判断方法上是否有注解，优先方法注解
                dateSource = getMethodAnnotation(method, false);
                builderStr.append("method is: ").append(method.getName());
                builderStr.append(", isAnnotation: true");
            } else if (method.getDeclaringClass().isAnnotationPresent(DSource.class)) {
                // 2.判断类上是否有注解
                dateSource = getMethodAnnotation(method, true);
                builderStr.append("class is: ").append(method.getDeclaringClass().getName());
                builderStr.append(", isAnnotation: true");
            } else {
                builderStr.append("is default");
            }
            builderStr.append(", DateSource is: ").append(dateSource);
            LOGGER.info(">>> {}", builderStr);
            // 切换数据源
            DynamicDataSourceContextHolder.set(dateSource);
        } catch (Exception e) {
            LOGGER.error("dynamicDataSourceMethodBeforeAdvice error", e);
        }
    }

    /**
     * 获取方法或者类上注解值
     *
     * @param method 方法
     * @return String
     */
    private String getMethodAnnotation(Method method, boolean isDeclaringClass) {
        DSource annotation = null;
        if (isDeclaringClass) {
            annotation = method.getDeclaringClass().getAnnotation(DSource.class);
        } else {
            annotation = method.getAnnotation(DSource.class);
        }
        // 取出注解中的数据源名
        DsEnum dsEnum = annotation.value();
        return dsEnum.name().toLowerCase();
    }
}
