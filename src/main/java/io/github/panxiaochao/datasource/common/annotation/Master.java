package io.github.panxiaochao.datasource.common.annotation;

import io.github.panxiaochao.datasource.common.enums.DsEnum;

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
@DSource(value = DsEnum.MASTER)
public @interface Master {
}
