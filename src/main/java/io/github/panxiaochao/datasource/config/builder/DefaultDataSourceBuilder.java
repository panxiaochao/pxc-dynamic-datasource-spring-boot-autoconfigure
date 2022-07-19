package io.github.panxiaochao.datasource.config.builder;

import io.github.panxiaochao.datasource.common.properties.DataSourceProperty;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.List;

/**
 * {@code DefaultDataSourceBuilder}
 * <p>
 *
 * @author Lypxc
 * @since 2022/7/18
 */
public class DefaultDataSourceBuilder {

    private List<DataSourceBuilder> builders;

    public List<DataSourceBuilder> getBuilders() {
        return builders;
    }

    public void setBuilders(List<DataSourceBuilder> builders) {
        this.builders = builders;
    }

    /**
     * 默认构建
     *
     * @param dataSourceProperty
     * @return
     */
    public DataSource buildDataSource(DataSourceProperty dataSourceProperty) {
        DataSourceBuilder dataSourceBuilder = null;
        for (DataSourceBuilder builder : this.builders) {
            if (builder.support(dataSourceProperty)) {
                dataSourceBuilder = builder;
                break;
            }
        }
        Assert.notNull(dataSourceBuilder, "dataSourceBuilder must not be null !");
        return dataSourceBuilder.buildDataSource(dataSourceProperty);
    }
}
