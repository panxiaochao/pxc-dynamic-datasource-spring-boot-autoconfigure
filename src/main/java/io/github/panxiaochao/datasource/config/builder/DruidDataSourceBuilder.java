package io.github.panxiaochao.datasource.config.builder;

import com.alibaba.druid.pool.DruidDataSource;
import io.github.panxiaochao.datasource.common.properties.DataSourceProperty;
import io.github.panxiaochao.datasource.common.properties.DynamicDataSourceProperties;
import io.github.panxiaochao.datasource.config.druid.DruidConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;

/**
 * {@code DruidDataSourceBuilder}
 * <p>
 *
 * @author Lypxc
 * @since 2022/7/18
 */
public class DruidDataSourceBuilder implements DataSourceBuilder, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(DruidDataSourceBuilder.class);

    private final ApplicationContext applicationContext;

    protected final DynamicDataSourceProperties dataSourceProperties;

    private DruidConfig druidConfig;

    private static final String DRUID_DATASOURCE = "com.alibaba.druid.pool.DruidDataSource";

    public DruidDataSourceBuilder(ApplicationContext applicationContext, DynamicDataSourceProperties properties) {
        this.applicationContext = applicationContext;
        this.dataSourceProperties = properties;
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
        //DruidConfig config = dataSourceProperty.getDruid();
        //Properties properties = config.toProperties(gConfig);

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
        return type == null || DRUID_DATASOURCE.equals(type.getName());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 先获取属性
        this.druidConfig = dataSourceProperties.getDruid();
    }
}
