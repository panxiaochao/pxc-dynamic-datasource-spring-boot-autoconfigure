package io.github.panxiaochao.db.properties;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import io.github.panxiaochao.db.banner.PxcBanner;
import io.github.panxiaochao.db.config.mybatisplus.MyMetaObjectHandler;
import io.github.panxiaochao.db.config.mybatisplus.MetaObjectHandlerFactory;
import io.github.panxiaochao.db.logging.Log;
import io.github.panxiaochao.db.logging.LogFactory;
import io.github.panxiaochao.db.logging.impl.NoLoggingImpl;
import org.apache.ibatis.session.ExecutorType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * {@code DataSourceProperties}
 * <p>
 *
 * @author Lypxc
 * @since 2022/1/4
 */
@ConfigurationProperties(prefix = PxcDynamicDataSourceProperties.PXC_DATASOURCE_PREFIX, ignoreInvalidFields = true)
public class PxcDynamicDataSourceProperties implements InitializingBean {
    /**
     * 属性前缀
     */
    public static final String PXC_DATASOURCE_PREFIX = "pxc.datasource";
    /**
     * 是否显示自定义 banner
     */
    private Boolean showBanner = Boolean.TRUE;
    private String execution;
    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    private String[] mapperLocations = new String[]{"classpath*:/mapper/**/*.xml"};
    private String typeAliasesPackage;
    private Map<String, DruidProperties> dynamic;
    private ExecutorType executorType;
    private Class<? extends MetaObjectHandler> metaObjectImpl = MyMetaObjectHandler.class;
    private IdType idType = IdType.AUTO;
    private DbType dbType = DbType.MYSQL;
    private Class<? extends Log> logImpl = NoLoggingImpl.class;
    private String type;
    private int initialSize = 5;
    private int minIdle = 10;
    private int maxActive = 20;
    private long maxWait = 60000;
    private long timeBetweenEvictionRunsMillis = 60000;
    private long minEvictableIdleTimeMillis = 300000;
    private String validationQuery;
    private boolean testWhileIdle = true;
    private boolean testOnBorrow = false;
    private boolean testOnReturn = false;
    private boolean poolPreparedStatements = true;
    private int maxPoolPreparedStatementPerConnectionSize = -1;
    private String filters;
    private String connectionProperties;
    private boolean useGlobalDataSourceStat = true;

    public Resource[] resolveMapperLocations() {
        return Stream.of(Optional.ofNullable(this.mapperLocations).orElse(new String[0]))
                .flatMap(location -> Stream.of(getResources(location))).toArray(Resource[]::new);
    }

    private Resource[] getResources(String location) {
        try {
            return resourceResolver.getResources(location);
        } catch (IOException e) {
            return new Resource[0];
        }
    }

    public String getExecution() {
        return execution;
    }

    public void setExecution(String execution) {
        this.execution = execution;
    }

    public String[] getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String[] mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public String getTypeAliasesPackage() {
        return typeAliasesPackage;
    }

    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }

    public Boolean getShowBanner() {
        return showBanner;
    }

    public void setShowBanner(Boolean showBanner) {
        this.showBanner = showBanner;
    }

    public Map<String, DruidProperties> getDynamic() {
        return dynamic;
    }

    public void setDynamic(Map<String, DruidProperties> dynamic) {
        this.dynamic = dynamic;
    }

    public ExecutorType getExecutorType() {
        return executorType;
    }

    public void setExecutorType(ExecutorType executorType) {
        this.executorType = executorType;
    }

    public Class<? extends MetaObjectHandler> getMetaObjectImpl() {
        return metaObjectImpl;
    }

    public void setMetaObjectImpl(Class<? extends MetaObjectHandler> metaObjectImpl) {
        if (metaObjectImpl != null) {
            this.metaObjectImpl = metaObjectImpl;
            MetaObjectHandlerFactory.userCustomClass(this.metaObjectImpl);
        }
    }

    public IdType getIdType() {
        return idType;
    }

    public void setIdType(IdType idType) {
        this.idType = idType;
    }

    public DbType getDbType() {
        return dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }

    public Class<? extends Log> getLogImpl() {
        return logImpl;
    }

    public void setLogImpl(Class<? extends Log> logImpl) {
        if (logImpl != null) {
            this.logImpl = logImpl;
            LogFactory.userCustomClass(this.logImpl);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isPoolPreparedStatements() {
        return poolPreparedStatements;
    }

    public void setPoolPreparedStatements(boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    public int getMaxPoolPreparedStatementPerConnectionSize() {
        return maxPoolPreparedStatementPerConnectionSize;
    }

    public void setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public String getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(String connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    public boolean isUseGlobalDataSourceStat() {
        return useGlobalDataSourceStat;
    }

    public void setUseGlobalDataSourceStat(boolean useGlobalDataSourceStat) {
        this.useGlobalDataSourceStat = useGlobalDataSourceStat;
    }

    public static class DruidProperties {
        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private String type;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // print banner
        if (this.showBanner) {
            PxcBanner pxcBanner = new PxcBanner();
            pxcBanner.printBanner();
        }
    }
}
