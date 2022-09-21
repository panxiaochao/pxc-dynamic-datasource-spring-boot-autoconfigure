package io.github.panxiaochao.datasource.common.properties;

import io.github.panxiaochao.datasource.common.banner.PxcBanner;
import io.github.panxiaochao.datasource.config.druid.DruidConfig;
import io.github.panxiaochao.datasource.config.hikari.HikariCpConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@code DynamicDataSourceProperties}
 * <p> 属性配置，来源于YML
 *
 * <p>update: 修改配置，新增数据源配置
 * <p>since:  2022-09-19
 *
 * @author Lypxc
 * @since 2022/7/18
 */
@Setter
@Getter
@ConfigurationProperties(prefix = DynamicDataSourceProperties.PREFIX, ignoreInvalidFields = true)
public class DynamicDataSourceProperties implements InitializingBean {
    /**
     * 属性前缀
     */
    public static final String PREFIX = "pxc.datasource";

    /**
     * 每一个独立数据源配置
     */
    private Map<String, DataSourceProperty> dynamic = new LinkedHashMap<>();

    /**
     * DRUID 数据源配置
     */
    @NestedConfigurationProperty
    private DruidConfig druid = new DruidConfig();

    /**
     * HIKARICP 数据源配置
     */
    @NestedConfigurationProperty
    private HikariCpConfig hikari = new HikariCpConfig();

    /**
     * 是否显示自定义 banner
     */
    private Boolean showBanner = Boolean.TRUE;

    /**
     * 是否需要AOP，默认启用
     */
    private Boolean aop = true;

    /**
     * AOP 拦截自定义正则表达式
     */
    private String execution;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.showBanner) {
            PxcBanner.printBanner();
        }
    }
}
