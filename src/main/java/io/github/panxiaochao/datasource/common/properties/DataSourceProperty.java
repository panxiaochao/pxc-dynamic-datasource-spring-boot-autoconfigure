package io.github.panxiaochao.datasource.common.properties;

import io.github.panxiaochao.datasource.config.druid.DruidConfig;
import io.github.panxiaochao.datasource.config.hikari.HikariCpConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.sql.DataSource;

/**
 * {@code DataSourceProperty}
 * <p> 每个数据库的属性
 *
 * @author Lypxc
 * @since 2022/7/18
 */
@Setter
@Getter
public class DataSourceProperty {
    /**
     * 连接池名字
     */
    private String poolName;

    /**
     * JDBC driver
     */
    private String driverClassName;

    /**
     * JDBC url
     */
    private String url;

    /**
     * JDBC username
     */
    private String username;

    /**
     * JDBC password
     */
    private String password;

    /**
     * JDBC type
     */
    private Class<? extends DataSource> type;

    /**
     * lazy start
     */
    private Boolean lazy;

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
}
