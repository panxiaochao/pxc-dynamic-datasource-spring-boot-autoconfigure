package io.github.panxiaochao.datasource.config.builder;

import io.github.panxiaochao.datasource.common.properties.DataSourceProperty;
import org.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;

/**
 * {@code HikariDataSourceBuilder}
 * <p>
 *
 * @author Lypxc
 * @since 2022/7/18
 */
public class HikariDataSourceBuilder implements DataSourceBuilder, InitializingBean {
    /**
     * 构建DataSource
     *
     * @param dataSourceProperty
     * @return
     */
    @Override
    public DataSource buildDataSource(DataSourceProperty dataSourceProperty) {
        return null;
    }

    /**
     * 是否支持
     *
     * @param dataSourceProperty
     * @return
     */
    @Override
    public boolean support(DataSourceProperty dataSourceProperty) {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
