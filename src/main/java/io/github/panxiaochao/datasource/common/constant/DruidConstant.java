package io.github.panxiaochao.datasource.common.constant;

/**
 * Druid 常量属性
 *
 * @author Lypxc
 */
public class DruidConstant {
    private static final String INITIAL_SIZE = "druid.initialSize";
    private static final String MAX_ACTIVE = "druid.maxActive";
    private static final String MIN_IDLE = "druid.minIdle";
    private static final String MAX_WAIT = "druid.maxWait";

    private static final String TIME_BETWEEN_EVICTION_RUNS_MILLIS = "druid.timeBetweenEvictionRunsMillis";
    private static final String TIME_BETWEEN_LOG_STATS_MILLIS = "druid.timeBetweenLogStatsMillis";
    private static final String MIN_EVICTABLE_IDLE_TIME_MILLIS = "druid.minEvictableIdleTimeMillis";
    private static final String MAX_EVICTABLE_IDLE_TIME_MILLIS = "druid.maxEvictableIdleTimeMillis";

    private static final String TEST_WHILE_IDLE = "druid.testWhileIdle";
    private static final String TEST_ON_BORROW = "druid.testOnBorrow";
    private static final String VALIDATION_QUERY = "druid.validationQuery";
    private static final String USE_GLOBAL_DATA_SOURCE_STAT = "druid.useGlobalDataSourceStat";
    private static final String ASYNC_INIT = "druid.asyncInit";

    private static final String FILTERS = "druid.filters";
    private static final String CLEAR_FILTERS_ENABLE = "druid.clearFiltersEnable";
    private static final String RESET_STAT_ENABLE = "druid.resetStatEnable";
    private static final String NOT_FULL_TIMEOUT_RETRY_COUNT = "druid.notFullTimeoutRetryCount";
    private static final String MAX_WAIT_THREAD_COUNT = "druid.maxWaitThreadCount";

    private static final String FAIL_FAST = "druid.failFast";
    private static final String PHY_TIMEOUT_MILLIS = "druid.phyTimeoutMillis";
    private static final String KEEP_ALIVE = "druid.keepAlive";
    private static final String POOL_PREPARED_STATEMENTS = "druid.poolPreparedStatements";
    private static final String INIT_VARIANTS = "druid.initVariants";
    private static final String INIT_GLOBAL_VARIANTS = "druid.initGlobalVariants";
    private static final String USE_UNFAIR_LOCK = "druid.useUnfairLock";
    private static final String KILL_WHEN_SOCKET_READ_TIMEOUT = "druid.killWhenSocketReadTimeout";
    private static final String MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE = "druid.maxPoolPreparedStatementPerConnectionSize";
    private static final String INIT_CONNECTION_SQLS = "druid.initConnectionSqls";

    private static final String CONFIG_STR = "config";
    private static final String STAT_STR = "stat";
}
