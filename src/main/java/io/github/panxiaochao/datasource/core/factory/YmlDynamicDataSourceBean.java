package io.github.panxiaochao.datasource.core.factory;

import io.github.panxiaochao.datasource.common.properties.DataSourceProperty;

import javax.sql.DataSource;
import java.util.Map;

/**
 * {@code YmlDynamicDataSourceBean}
 * <p> 通过YML加载数据库源信息
 *
 * @author Lypxc
 * @since 2022/7/19
 */
public class YmlDynamicDataSourceBean extends AbstractDataSourceFactory {

    private final Map<String, DataSourceProperty> dataSourcePropertiesMap;

    public YmlDynamicDataSourceBean(Map<String, DataSourceProperty> dataSourcePropertiesMap) {
        this.dataSourcePropertiesMap = dataSourcePropertiesMap;
    }

    /**
     * 加载数据库
     *
     * @return
     */
    @Override
    public Map<String, DataSource> loadDataSource() {
        return buildDataSourceMap(dataSourcePropertiesMap);
    }
}
