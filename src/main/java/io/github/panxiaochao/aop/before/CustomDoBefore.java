package io.github.panxiaochao.aop.before;

import io.github.panxiaochao.annotation.DbSource;
import io.github.panxiaochao.enums.DbSourceEnum;
import io.github.panxiaochao.multiple.DbSourceContextHolder;
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
        LOGGER.info(">>> before datasource {}", DbSourceContextHolder.get());
        // 默认数据源 master
        String datesource = "";
        try {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Aop pointcut ");
            // 得到访问的方法对象
            if (method.isAnnotationPresent(DbSource.class)) {
                // 1.采用最小化优先原则，先判断方法上是否有注解，优先方法注解
                datesource = getMethodAnnotation(method, false);
                stringBuffer.append("method is: ").append(method.getName());
                stringBuffer.append(", isAnnotation: true");
            } else if (method.getDeclaringClass().isAnnotationPresent(DbSource.class)) {
                // 2.判断类上是否有注解
                datesource = getMethodAnnotation(method, true);
                stringBuffer.append("class is: ").append(method.getDeclaringClass().getName());
                stringBuffer.append(", isAnnotation: true");
            } else {
                stringBuffer.append("is defalut");
                datesource = DbSourceEnum.MASTER.name();
            }
            stringBuffer.append(", datesource is: ").append(datesource);
            LOGGER.info(">>> {}", stringBuffer.toString());
            // 切换数据源
            DbSourceContextHolder.set(datesource);
        } catch (Exception e) {
            e.printStackTrace();
            DbSourceContextHolder.set(datesource);
        }
    }

    /**
     * 获取方法或者类上注解值
     *
     * @param method 方法
     * @return String
     */
    private String getMethodAnnotation(Method method, boolean isDeclaringClass) {
        DbSource annotation = null;
        if (isDeclaringClass) {
            annotation = method.getDeclaringClass().getAnnotation(DbSource.class);
        } else {
            annotation = method.getAnnotation(DbSource.class);
        }
        // 取出注解中的数据源名
        DbSourceEnum dbSourceEnum = annotation.value();
        return dbSourceEnum.name().toLowerCase();
    }
}
