package io.github.panxiaochao.annotation;

import io.github.panxiaochao.enums.DbSourceEnum;

import java.lang.annotation.*;

/**
 * 接口、类、枚举、注解、方法
 *
 * @author Mr_LyPxc
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DbSource {
    DbSourceEnum value() default DbSourceEnum.MASTER;
}
