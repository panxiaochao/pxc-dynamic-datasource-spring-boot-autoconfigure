package io.github.panxiaochao.db.multiple;

import io.github.panxiaochao.db.enums.DbSourceEnum;
import io.github.panxiaochao.db.logging.Log;
import io.github.panxiaochao.db.logging.LogFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


/**
 * @author Mr_LyPxc
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final Log LOGGER = LogFactory.getLog(DynamicDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        String dbSource = DbSourceContextHolder.get();
        if (StringUtils.isBlank(dbSource)) {
            dbSource = DbSourceEnum.MASTER.name();
        }
        LOGGER.info(">>> switch datasource " + dbSource);
        return dbSource;
    }
}
