package io.github.panxiaochao.db.annotation;

import io.github.panxiaochao.db.enums.DbSourceEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口、类、枚举、注解、方法
 *
 * @author Mr_LyPxc
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DbSource(value = DbSourceEnum.MASTER)
public @interface Master {
}
