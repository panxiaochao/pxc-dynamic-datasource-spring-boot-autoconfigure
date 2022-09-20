package io.github.panxiaochao.datasource.config.creator;

import io.github.panxiaochao.datasource.common.properties.DataSourceProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.List;

/**
 * {@code DefaultDataSourceBuilder}
 * <p> 默认数据源生成器
 *
 * @author Lypxc
 * @since 2022/7/18
 */
@Setter
@Getter
public class DefaultDataSourceCreator {

    private List<DataSourceCreator> builders;

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
