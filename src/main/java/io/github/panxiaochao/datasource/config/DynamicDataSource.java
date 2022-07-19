package io.github.panxiaochao.datasource.config;

import io.github.panxiaochao.datasource.common.enums.DsEnum;
import io.github.panxiaochao.datasource.core.holder.DynamicDataSourceContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


/**
 * 核心动态数据源
 *
 * @author Lypxc
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        String dbSource = DynamicDataSourceContextHolder.get();
        if (StringUtils.isBlank(dbSource)) {
            dbSource = DsEnum.MASTER.name();
        }
        LOGGER.info(">>> switch datasource " + dbSource);
        return dbSource;
    }
}
