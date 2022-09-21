package io.github.panxiaochao.datasource.config.creator;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.panxiaochao.datasource.common.properties.DataSourceProperty;
import io.github.panxiaochao.datasource.common.properties.DynamicDataSourceProperties;
import io.github.panxiaochao.datasource.config.hikari.HikariCpConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * {@code HikariDataSourceBuilder}
 * <p>
 *
 * @author Lypxc
 * @since 2022/7/18
 */
public class HikariDataSourceCreator implements DataSourceCreator, InitializingBean {

    private static Method CONFIG_COPY_METHOD = null;

    @Resource
    protected DynamicDataSourceProperties dataSourceProperties;

    /**
     * HikariCp数据源
     */
    private static final String HIKARI_DATASOURCE = "com.zaxxer.hikari.HikariDataSource";

    private HikariCpConfig hikariCpConfig;

    static {
        fetchMethod();
    }

    /**
     * to support springboot 1.5 and 2.x
     * HikariConfig 2.x use 'copyState' to copy config
     * HikariConfig 3.x use 'copyStateTo' to copy config
     */
    private static void fetchMethod() {
        try {
            CONFIG_COPY_METHOD = HikariConfig.class.getMethod("copyState", HikariConfig.class);
            return;
        } catch (NoSuchMethodException ignored) {
            // skip
        }

        try {
            CONFIG_COPY_METHOD = HikariConfig.class.getMethod("copyStateTo", HikariConfig.class);
            return;
        } catch (NoSuchMethodException ignored) {
            // skip
        }
        throw new RuntimeException("HikariConfig does not has 'copyState' or 'copyStateTo' method!");
    }

    /**
     * 构建DataSource
     *
     * @param dataSourceProperty
     * @return
     */
    @Override
    public DataSource buildDataSource(DataSourceProperty dataSourceProperty) {
        // 转换本地属性
        HikariCpConfig localConfig = dataSourceProperty.getHikari();
        Properties properties = localConfig.toProperties(hikariCpConfig);
        HikariConfig config = new HikariConfig(properties);
        config.setUsername(dataSourceProperty.getUsername());
        config.setPassword(dataSourceProperty.getPassword());
        config.setJdbcUrl(dataSourceProperty.getUrl());
        if (StringUtils.isBlank(config.getPoolName())) {
            config.setPoolName(dataSourceProperty.getPoolName());
        }
        String driverClassName = dataSourceProperty.getDriverClassName();
        if (StringUtils.isNotBlank(driverClassName)) {
            config.setDriverClassName(driverClassName);
        }
        // 额外属性操作
        if (localConfig.getDataSourceProperties() != null) {
            config.setDataSourceProperties(localConfig.getDataSourceProperties());
        }
        if (localConfig.getHealthCheckProperties() != null) {
            config.setHealthCheckProperties(localConfig.getHealthCheckProperties());
        }
        config.validate();
        HikariDataSource dataSource = new HikariDataSource();
        try {
            CONFIG_COPY_METHOD.invoke(config, dataSource);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("HikariConfig failed to copy to HikariDataSource", e);
        }
        return dataSource;
    }

    /**
     * 是否支持
     *
     * @param dataSourceProperty
     * @return
     */
    @Override
    public boolean support(DataSourceProperty dataSourceProperty) {
        Class<? extends DataSource> type = dataSourceProperty.getType();
        return type == null || HIKARI_DATASOURCE.equals(type.getName());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        hikariCpConfig = dataSourceProperties.getHikari();
    }
}
