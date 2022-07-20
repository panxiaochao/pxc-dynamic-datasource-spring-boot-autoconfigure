package io.github.panxiaochao.datasource.common.properties;

import io.github.panxiaochao.datasource.config.druid.DruidConfig;
import io.github.panxiaochao.datasource.config.hikari.HikariCpConfig;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.sql.DataSource;

/**
 * {@code DataSourceProperty}
 * <p> 每个数据库的属性
 *
 * @author Lypxc
 * @since 2022/7/18
 */
public class DataSourceProperty {
    /**
     * 连接池名字
     */
    private String poolName;
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private Class<? extends DataSource> type;

    @NestedConfigurationProperty
    private DruidConfig druid = new DruidConfig();
    @NestedConfigurationProperty
    private HikariCpConfig hikari = new HikariCpConfig();

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Class<? extends DataSource> getType() {
        return type;
    }

    public void setType(Class<? extends DataSource> type) {
        this.type = type;
    }

    public DruidConfig getDruid() {
        return druid;
    }

    public void setDruid(DruidConfig druid) {
        this.druid = druid;
    }

    public HikariCpConfig getHikari() {
        return hikari;
    }

    public void setHikari(HikariCpConfig hikari) {
        this.hikari = hikari;
    }
}
