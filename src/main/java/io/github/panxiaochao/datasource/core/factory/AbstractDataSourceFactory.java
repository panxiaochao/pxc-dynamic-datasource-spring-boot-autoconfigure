package io.github.panxiaochao.datasource.core.factory;

import io.github.panxiaochao.datasource.common.properties.DataSourceProperty;
import io.github.panxiaochao.datasource.config.builder.DefaultDataSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code AbstractDataSourceFactory}
 * <p>
 *
 * @author Lypxc
 * @since 2022/7/18
 */
public abstract class AbstractDataSourceFactory implements DataSourceFactory {

    @Autowired
    private DefaultDataSourceBuilder defaultDataSourceBuilder;

    protected Map<String, DataSource> buildDataSourceMap(
            Map<String, DataSourceProperty> dataSourcePropertiesMap) {
        Map<String, DataSource> dataSourceMap = new HashMap<>(dataSourcePropertiesMap.size() * 2);
        for (Map.Entry<String, DataSourceProperty> item : dataSourcePropertiesMap.entrySet()) {
            String dsName = item.getKey();
            DataSourceProperty dataSourceProperty = item.getValue();
            String poolName = dataSourceProperty.getPoolName();
            if (poolName == null || "".equals(poolName)) {
                poolName = dsName;
            }
            dataSourceProperty.setPoolName(poolName);
            dataSourceMap.put(dsName, defaultDataSourceBuilder.buildDataSource(dataSourceProperty));
        }
        return dataSourceMap;
    }
}
