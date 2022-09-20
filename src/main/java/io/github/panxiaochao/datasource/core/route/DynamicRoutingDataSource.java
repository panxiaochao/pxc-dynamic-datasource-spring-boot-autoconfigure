package io.github.panxiaochao.datasource.core.route;

import io.github.panxiaochao.datasource.common.enums.DsEnum;
import io.github.panxiaochao.datasource.common.exception.DsException;
import io.github.panxiaochao.datasource.core.factory.DynamicDataSourceBean;
import io.github.panxiaochao.datasource.core.holder.DynamicDataSourceContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@code DynamicRoutingDataSource}
 * <p> 核心组件，动态数据源
 *
 * @author Lypxc
 * @since 2022/7/20
 */
public class DynamicRoutingDataSource extends AbstractDynamicRoutingDataSource implements InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicRoutingDataSource.class);
    /**
     * 所有数据库
     */
    private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

    /**
     * 加载数据源的动态信息
     */
    private List<DynamicDataSourceBean> dynamicDataSourceBeans;

    public DynamicRoutingDataSource() {
    }

    public DynamicRoutingDataSource(List<DynamicDataSourceBean> dynamicDataSourceBeans) {
        this.dynamicDataSourceBeans = dynamicDataSourceBeans;
    }

    /**
     * 连接对象
     *
     * @return
     */
    @Override
    protected DataSource determineTargetDataSource() {
        String dbSource = DynamicDataSourceContextHolder.get();
        return getDataSource(dbSource);
    }

    /**
     * 获取数据源
     *
     * @param ds 数据源名称
     * @return 数据源
     */
    public DataSource getDataSource(String ds) {
        if (StringUtils.isEmpty(ds)) {
            return determinePrimaryDataSource();
        } else if (dataSourceMap.containsKey(ds)) {
            LOGGER.info("dynamic datasource switch to the datasource named [{}]", ds);
            return dataSourceMap.get(ds);
        }
        return determinePrimaryDataSource();
    }

    /**
     * 获取默认数据源，默认 Master
     *
     * @return
     */
    private DataSource determinePrimaryDataSource() {
        DataSource dataSource = dataSourceMap.get(DsEnum.MASTER.name());
        if (dataSource == null) {
            throw new DsException("dynamic datasource can not find primary Master datasource");
        }
        return dataSource;
    }

    /**
     * 添加数据源
     *
     * @param ds         数据源名称
     * @param dataSource 数据源
     */
    public synchronized void addDataSource(String ds, DataSource dataSource) {
        DataSource oldDataSource = dataSourceMap.put(ds, dataSource);
        // 关闭老的数据源
        if (oldDataSource != null) {
            closeDataSource(ds, oldDataSource);
        }
        LOGGER.info("dynamic datasource - add a datasource named [{}] success", ds);
    }

    /**
     * close db
     *
     * @param ds         dsName
     * @param dataSource db
     */
    private void closeDataSource(String ds, DataSource dataSource) {
        try {
            Method closeMethod = ReflectionUtils.findMethod(dataSource.getClass(), "close");
            if (closeMethod != null) {
                closeMethod.invoke(dataSource);
            }
        } catch (Exception e) {
            LOGGER.warn("dynamic datasource closed datasource named [{}] failed", ds, e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 添加数据源
        Map<String, DataSource> dataSources = new HashMap<>(16);
        for (DynamicDataSourceBean dynamicDataSourceBean : dynamicDataSourceBeans) {
            dataSources.putAll(dynamicDataSourceBean.loadDataSource());
        }
        for (Map.Entry<String, DataSource> dsItem : dataSources.entrySet()) {
            addDataSource(dsItem.getKey(), dsItem.getValue());
        }
        // 检测默认数据源是否设置
        if (dataSourceMap.containsKey(DsEnum.MASTER.name())) {
            LOGGER.info("dynamic datasource initial loaded [{}] datasource, primary datasource named [{}]", dataSources.size(), DsEnum.MASTER.name());
        } else {
            throw new DsException("dynamic datasource can not find primary Master datasource");
        }
    }

    @Override
    public void destroy() throws Exception {
        LOGGER.info("dynamic-datasource start closing ....");
        for (Map.Entry<String, DataSource> item : dataSourceMap.entrySet()) {
            closeDataSource(item.getKey(), item.getValue());
        }
        LOGGER.info("dynamic-datasource all closed success,bye");
    }
}
