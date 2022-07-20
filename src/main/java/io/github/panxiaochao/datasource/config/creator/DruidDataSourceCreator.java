package io.github.panxiaochao.datasource.config.creator;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.CommonsLogFilter;
import com.alibaba.druid.filter.logging.Log4j2Filter;
import com.alibaba.druid.filter.logging.Log4jFilter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import io.github.panxiaochao.datasource.common.exception.DsException;
import io.github.panxiaochao.datasource.common.properties.DataSourceProperty;
import io.github.panxiaochao.datasource.common.properties.DynamicDataSourceProperties;
import io.github.panxiaochao.datasource.config.druid.DruidConfig;
import io.github.panxiaochao.datasource.config.druid.DruidLogConfigUtil;
import io.github.panxiaochao.datasource.config.druid.DruidStatConfigUtil;
import io.github.panxiaochao.datasource.config.druid.DruidWallConfigUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * {@code DruidDataSourceBuilder}
 * <p>
 *
 * @author Lypxc
 * @since 2022/7/18
 */
public class DruidDataSourceCreator implements DataSourceCreator, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(DruidDataSourceCreator.class);

    private ApplicationContext applicationContext;

    protected DynamicDataSourceProperties dataSourceProperties;

    private DruidConfig druidConfig;

    private static final String DRUID_DATASOURCE = "com.alibaba.druid.pool.DruidDataSource";

    public DruidDataSourceCreator() {
    }

    public DruidDataSourceCreator(ApplicationContext applicationContext, DynamicDataSourceProperties dataSourceProperties) {
        this.applicationContext = applicationContext;
        this.dataSourceProperties = dataSourceProperties;
    }


    /**
     * 构建DataSource
     *
     * @param dataSourceProperty
     * @return
     */
    @Override
    public DataSource buildDataSource(DataSourceProperty dataSourceProperty) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(dataSourceProperty.getUsername());
        dataSource.setPassword(dataSourceProperty.getPassword());
        dataSource.setUrl(dataSourceProperty.getUrl());
        dataSource.setName(dataSourceProperty.getPoolName());
        String driverClassName = dataSourceProperty.getDriverClassName();
        if (StringUtils.isNotBlank(driverClassName)) {
            dataSource.setDriverClassName(driverClassName);
        }
        DruidConfig config = dataSourceProperty.getDruid();
        Properties properties = config.toProperties(druidConfig);
        List<Filter> proxyFilters = this.initFilters(dataSourceProperty, properties.getProperty("druid.filters"));
        dataSource.setProxyFilters(proxyFilters);

        dataSource.configFromPropety(properties);
        //连接参数单独设置
        dataSource.setConnectProperties(config.getConnectionProperties());
        //设置druid内置properties不支持的的参数，额外参数
        this.setParam(dataSource, config);

        //if (Boolean.FALSE.equals(dataSourceProperty.getLazy())) {
        try {
            dataSource.init();
        } catch (SQLException e) {
            throw new DsException("druid create error", e);
        }
        //}
        return dataSource;
    }

    /**
     * 设置druid filters
     *
     * @param dataSourceProperty
     * @param filters
     * @return
     */
    private List<Filter> initFilters(DataSourceProperty dataSourceProperty, String filters) {
        List<Filter> proxyFilters = new ArrayList<>(2);
        if (StringUtils.isNotBlank(filters)) {
            String[] filterItems = filters.split(",");
            for (String filter : filterItems) {
                switch (filter) {
                    case "stat":
                        proxyFilters.add(DruidStatConfigUtil.toStatFilter(dataSourceProperty.getDruid().getStat(), druidConfig.getStat()));
                        break;
                    case "wall":
                        WallConfig wallConfig = DruidWallConfigUtil.toWallConfig(dataSourceProperty.getDruid().getWall(), druidConfig.getWall());
                        WallFilter wallFilter = new WallFilter();
                        wallFilter.setConfig(wallConfig);
                        proxyFilters.add(wallFilter);
                        break;
                    case "slf4j":
                        proxyFilters.add(DruidLogConfigUtil.initFilter(Slf4jLogFilter.class, dataSourceProperty.getDruid().getSlf4j(), druidConfig.getSlf4j()));
                        break;
                    case "commons-log":
                        proxyFilters.add(DruidLogConfigUtil.initFilter(CommonsLogFilter.class, dataSourceProperty.getDruid().getCommonsLog(), druidConfig.getCommonsLog()));
                        break;
                    case "log4j":
                        proxyFilters.add(DruidLogConfigUtil.initFilter(Log4jFilter.class, dataSourceProperty.getDruid().getLog4j(), druidConfig.getLog4j()));
                        break;
                    case "log4j2":
                        proxyFilters.add(DruidLogConfigUtil.initFilter(Log4j2Filter.class, dataSourceProperty.getDruid().getLog4j2(), druidConfig.getLog4j2()));
                        break;
                    default:
                        LOGGER.warn("dynamic-datasource current not support [{}]", filter);
                }
            }
        }
        if (this.applicationContext != null) {
            for (String filterId : druidConfig.getProxyFilters()) {
                proxyFilters.add(this.applicationContext.getBean(filterId, Filter.class));
            }
        }
        return proxyFilters;
    }

    /**
     * 设置druid内置properties不支持的的参数，额外参数
     *
     * @param dataSource
     * @param config
     */
    private void setParam(DruidDataSource dataSource, DruidConfig config) {
        String defaultCatalog = config.getDefaultCatalog() == null ? druidConfig.getDefaultCatalog() : config.getDefaultCatalog();
        if (defaultCatalog != null) {
            dataSource.setDefaultCatalog(defaultCatalog);
        }
        Boolean defaultAutoCommit = config.getDefaultAutoCommit() == null ? druidConfig.getDefaultAutoCommit() : config.getDefaultAutoCommit();
        if (defaultAutoCommit != null && !defaultAutoCommit) {
            dataSource.setDefaultAutoCommit(false);
        }
        Boolean defaultReadOnly = config.getDefaultReadOnly() == null ? druidConfig.getDefaultReadOnly() : config.getDefaultReadOnly();
        if (defaultReadOnly != null) {
            dataSource.setDefaultReadOnly(defaultReadOnly);
        }
        Integer defaultTransactionIsolation = config.getDefaultTransactionIsolation() == null ? druidConfig.getDefaultTransactionIsolation() : config.getDefaultTransactionIsolation();
        if (defaultTransactionIsolation != null) {
            dataSource.setDefaultTransactionIsolation(defaultTransactionIsolation);
        }

        Boolean testOnReturn = config.getTestOnReturn() == null ? druidConfig.getTestOnReturn() : config.getTestOnReturn();
        if (testOnReturn != null && testOnReturn) {
            dataSource.setTestOnReturn(true);
        }
        Integer validationQueryTimeout =
                config.getValidationQueryTimeout() == null ? druidConfig.getValidationQueryTimeout() : config.getValidationQueryTimeout();
        if (validationQueryTimeout != null && !validationQueryTimeout.equals(-1)) {
            dataSource.setValidationQueryTimeout(validationQueryTimeout);
        }

        Boolean sharePreparedStatements =
                config.getSharePreparedStatements() == null ? druidConfig.getSharePreparedStatements() : config.getSharePreparedStatements();
        if (sharePreparedStatements != null && sharePreparedStatements) {
            dataSource.setSharePreparedStatements(true);
        }
        Integer connectionErrorRetryAttempts =
                config.getConnectionErrorRetryAttempts() == null ? druidConfig.getConnectionErrorRetryAttempts()
                        : config.getConnectionErrorRetryAttempts();
        if (connectionErrorRetryAttempts != null && !connectionErrorRetryAttempts.equals(1)) {
            dataSource.setConnectionErrorRetryAttempts(connectionErrorRetryAttempts);
        }
        Boolean breakAfterAcquireFailure =
                config.getBreakAfterAcquireFailure() == null ? druidConfig.getBreakAfterAcquireFailure() : config.getBreakAfterAcquireFailure();
        if (breakAfterAcquireFailure != null && breakAfterAcquireFailure) {
            dataSource.setBreakAfterAcquireFailure(true);
        }

        Integer timeout = config.getRemoveAbandonedTimeoutMillis() == null ? druidConfig.getRemoveAbandonedTimeoutMillis()
                : config.getRemoveAbandonedTimeoutMillis();
        if (timeout != null) {
            dataSource.setRemoveAbandonedTimeoutMillis(timeout);
        }

        Boolean abandoned = config.getRemoveAbandoned() == null ? druidConfig.getRemoveAbandoned() : config.getRemoveAbandoned();
        if (abandoned != null) {
            dataSource.setRemoveAbandoned(abandoned);
        }

        Boolean logAbandoned = config.getLogAbandoned() == null ? druidConfig.getLogAbandoned() : config.getLogAbandoned();
        if (logAbandoned != null) {
            dataSource.setLogAbandoned(logAbandoned);
        }

        Integer queryTimeOut = config.getQueryTimeout() == null ? druidConfig.getQueryTimeout() : config.getQueryTimeout();
        if (queryTimeOut != null) {
            dataSource.setQueryTimeout(queryTimeOut);
        }

        Integer transactionQueryTimeout =
                config.getTransactionQueryTimeout() == null ? druidConfig.getTransactionQueryTimeout() : config.getTransactionQueryTimeout();
        if (transactionQueryTimeout != null) {
            dataSource.setTransactionQueryTimeout(transactionQueryTimeout);
        }
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
        return type == null || DRUID_DATASOURCE.equals(type.getName());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 先获取属性
        this.druidConfig = dataSourceProperties.getDruid();
    }
}
