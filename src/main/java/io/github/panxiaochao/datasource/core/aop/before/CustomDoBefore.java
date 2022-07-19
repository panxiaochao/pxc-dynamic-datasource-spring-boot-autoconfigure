package io.github.panxiaochao.datasource.core.aop.before;

import io.github.panxiaochao.datasource.common.annotation.DSource;
import io.github.panxiaochao.datasource.common.enums.DsEnum;
import io.github.panxiaochao.datasource.common.exception.DsException;
import io.github.panxiaochao.datasource.core.holder.DynamicDataSourceContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * {@code CustomDoAfter}
 * <p>前置拦截
 *
 * @author Lypxc
 * @since 2022/1/6
 */
public class CustomDoBefore implements MethodBeforeAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomDoBefore.class);

    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        LOGGER.info(">>> before datasource " + DynamicDataSourceContextHolder.get());
        // 默认数据源 master
        String datesource = "";
        try {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Aop pointcut ");
            // 得到访问的方法对象
            if (method.isAnnotationPresent(DSource.class)) {
                // 1.采用最小化优先原则，先判断方法上是否有注解，优先方法注解
                datesource = getMethodAnnotation(method, false);
                stringBuffer.append("method is: ").append(method.getName());
                stringBuffer.append(", isAnnotation: true");
            } else if (method.getDeclaringClass().isAnnotationPresent(DSource.class)) {
                // 2.判断类上是否有注解
                datesource = getMethodAnnotation(method, true);
                stringBuffer.append("class is: ").append(method.getDeclaringClass().getName());
                stringBuffer.append(", isAnnotation: true");
            } else {
                stringBuffer.append("is defalut");
                datesource = DsEnum.MASTER.name();
            }
            stringBuffer.append(", datesource is: ").append(datesource);
            LOGGER.info(">>> " + stringBuffer.toString());
            if (!DynamicDataSourceContextHolder.containsKey(datesource)) {
                throw new DsException("请检查`spring.datasource.dynamic.XX`配置的XX是否与注解@DSource(DsEnum.XX)相对应");
            } else {
                // 切换数据源
                DynamicDataSourceContextHolder.set(datesource);
            }
        } catch (Exception e) {
            LOGGER.error("aop before error", e);
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
        DsEnum dbSourceEnum = annotation.value();
        return dbSourceEnum.name().toLowerCase();
    }
}
