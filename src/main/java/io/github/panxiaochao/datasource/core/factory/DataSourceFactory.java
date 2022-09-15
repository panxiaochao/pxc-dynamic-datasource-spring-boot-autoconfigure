package io.github.panxiaochao.datasource.core.factory;

import javax.sql.DataSource;
import java.util.Map;

/**
 * {@code DataSourceFactory}
 * <p> 多数据源工厂，默认从Yml文件读取信息加载数据源，也可以实现此类自定义加载数据源
 *
 * @author Lypxc
 * @since 2022/7/18
 */
public interface DataSourceFactory {

    /**
     * 加载数据库
     *
     * @return
     */
    Map<String, DataSource> loadDataSource();
}
