package io.github.panxiaochao.multiple;

import io.github.panxiaochao.enums.DbSourceEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


/**
 * @author Mr_LyPxc
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        String dbSource = DbSourceContextHolder.get();
        if (StringUtils.isBlank(dbSource)) {
            dbSource = DbSourceEnum.MASTER.name();
        }
        LOGGER.info(">>> switch datasource {}", dbSource);
        return dbSource;
    }
}
