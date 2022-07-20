package io.github.panxiaochao.datasource.config.creator;

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
public class DefaultDataSourceCreator {

    private List<DataSourceCreator> builders;

    public List<DataSourceCreator> getBuilders() {
        return builders;
    }

    public void setBuilders(List<DataSourceCreator> builders) {
        this.builders = builders;
    }

    /**
     * 默认构建
     *
     * @param dataSourceProperty
     * @return
     */
    public DataSource buildDataSource(DataSourceProperty dataSourceProperty) {
        DataSourceCreator dataSourceCreator = null;
        for (DataSourceCreator builder : this.builders) {
            if (builder.support(dataSourceProperty)) {
                dataSourceCreator = builder;
                break;
            }
        }
        Assert.notNull(dataSourceCreator, "dataSourceCreator must not be null !");
        return dataSourceCreator.buildDataSource(dataSourceProperty);
    }
}
