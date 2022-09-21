package io.github.panxiaochao.datasource.config.hikari;

import lombok.Getter;
import lombok.Setter;

import java.util.Properties;


/**
 * Hikari参数配置
 *
 * @author Lypxc
 */
@Setter
@Getter
public class HikariCpConfig {

    private String catalog;
    private Long connectionTimeout;
    private Long validationTimeout;
    private Long idleTimeout;
    private Long leakDetectionThreshold;
    private Long maxLifetime;
    private Integer maxPoolSize;
    private Integer maximumPoolSize;
    private Integer minIdle;
    private Integer minimumIdle;
    private Long initializationFailTimeout;
    private String connectionInitSql;
    private String connectionTestQuery;
    private String dataSourceClassName;
    private String dataSourceJndiName;
    private String transactionIsolationName;
    private Boolean isAutoCommit;
    private Boolean isReadOnly;
    private Boolean isIsolateInternalQueries;
    private Boolean isRegisterMbeans;
    private Boolean isAllowPoolSuspension;
    private Properties dataSourceProperties;
    private Properties healthCheckProperties;
    private String poolName;
    private String schema;
    private String exceptionOverrideClassName;
    private Long keepaliveTime;
    private Boolean sealed;

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maxPoolSize = maximumPoolSize;
    }

    public void setMinimumIdle(Integer minimumIdle) {
        this.minIdle = minimumIdle;
    }

    /**
     * 根据全局配置和本地配置结合转换为Properties
     *
     * @param hikariCpConfig 全局配置
     * @return hikariCpConfig 配置
     */
    public Properties toProperties(HikariCpConfig hikariCpConfig) {
        Properties properties = new Properties();
        String catalog = this.catalog == null ? hikariCpConfig.getCatalog() : this.catalog;
        if (catalog != null && catalog.length() > 0) {
            properties.setProperty("catalog", catalog);
        }
        Long connectionTimeout = this.connectionTimeout == null ? hikariCpConfig.getConnectionTimeout() : this.connectionTimeout;
        if (connectionTimeout != null) {
            properties.setProperty("connectionTimeout", String.valueOf(connectionTimeout));
        }
        Long validationTimeout = this.connectionTimeout == null ? hikariCpConfig.getValidationTimeout() : this.connectionTimeout;
        if (validationTimeout != null) {
            properties.setProperty("validationTimeout", String.valueOf(validationTimeout));
        }
        Long idleTimeout = this.idleTimeout == null ? hikariCpConfig.getIdleTimeout() : this.idleTimeout;
        if (idleTimeout != null) {
            properties.setProperty("idleTimeout", String.valueOf(idleTimeout));
        }
        Long leakDetectionThreshold =
                this.leakDetectionThreshold == null ? hikariCpConfig.getLeakDetectionThreshold() : this.leakDetectionThreshold;
        if (leakDetectionThreshold != null) {
            properties.setProperty("leakDetectionThreshold", String.valueOf(leakDetectionThreshold));
        }
        Long maxLifetime = this.maxLifetime == null ? hikariCpConfig.getMaxLifetime() : this.maxLifetime;
        if (maxLifetime != null) {
            properties.setProperty("maxLifetime", String.valueOf(maxLifetime));
        }
        Integer maxPoolSize = this.maxPoolSize == null ? hikariCpConfig.getMaxPoolSize() : this.maxPoolSize;
        if (maxPoolSize != null) {
            properties.setProperty("maxPoolSize", String.valueOf(maxPoolSize));
        }
        // 需要设置 maxPoolSize
        Integer maximumPoolSize = this.maximumPoolSize == null ? hikariCpConfig.getMaximumPoolSize() : this.maximumPoolSize;
        if (maximumPoolSize != null) {
            properties.setProperty("maxPoolSize", String.valueOf(maximumPoolSize));
        }
        Integer minIdle = this.minIdle == null ? hikariCpConfig.getMinIdle() : this.minIdle;
        if (minIdle != null) {
            properties.setProperty("minIdle", String.valueOf(minIdle));
        }
        // 需要设置 minIdle
        Integer minimumIdle = this.minimumIdle == null ? hikariCpConfig.getMinimumIdle() : this.minimumIdle;
        if (minimumIdle != null) {
            properties.setProperty("minIdle", String.valueOf(minimumIdle));
        }
        Long initializationFailTimeout =
                this.initializationFailTimeout == null ? hikariCpConfig.getInitializationFailTimeout() : this.initializationFailTimeout;
        if (initializationFailTimeout != null) {
            properties.setProperty("initializationFailTimeout", String.valueOf(initializationFailTimeout));
        }
        String connectionInitSql =
                this.connectionInitSql == null ? hikariCpConfig.getConnectionInitSql() : this.connectionInitSql;
        if (connectionInitSql != null && connectionInitSql.length() > 0) {
            properties.setProperty("connectionInitSql", connectionInitSql);
        }
        String connectionTestQuery =
                this.connectionTestQuery == null ? hikariCpConfig.getConnectionTestQuery() : this.connectionTestQuery;
        if (connectionTestQuery != null && connectionTestQuery.length() > 0) {
            properties.setProperty("connectionTestQuery", connectionTestQuery);
        }
        String dataSourceClassName = this.dataSourceClassName == null ? hikariCpConfig.getDataSourceClassName() : this.dataSourceClassName;
        if (dataSourceClassName != null && dataSourceClassName.length() > 0) {
            properties.setProperty("dataSourceClassName", dataSourceClassName);
        }
        String dataSourceJndiName = this.dataSourceJndiName == null ? hikariCpConfig.getDataSourceJndiName() : this.dataSourceJndiName;
        if (dataSourceJndiName != null && dataSourceJndiName.length() > 0) {
            properties.setProperty("dataSourceJndiName", dataSourceJndiName);
        }
        String transactionIsolationName = this.transactionIsolationName == null ? hikariCpConfig.getTransactionIsolationName() : this.transactionIsolationName;
        if (transactionIsolationName != null && transactionIsolationName.length() > 0) {
            properties.setProperty("transactionIsolationName", transactionIsolationName);
        }
        Boolean isAutoCommit = this.isAutoCommit == null ? hikariCpConfig.getIsAutoCommit() : this.isAutoCommit;
        if (isAutoCommit != null) {
            properties.setProperty("isAutoCommit", isAutoCommit.toString());
        }
        Boolean isReadOnly = this.isReadOnly == null ? hikariCpConfig.getIsReadOnly() : this.isReadOnly;
        if (isReadOnly != null) {
            properties.setProperty("isReadOnly", isReadOnly.toString());
        }
        Boolean isIsolateInternalQueries = this.isIsolateInternalQueries == null ? hikariCpConfig.getIsIsolateInternalQueries() : this.isIsolateInternalQueries;
        if (isIsolateInternalQueries != null) {
            properties.setProperty("isIsolateInternalQueries", isIsolateInternalQueries.toString());
        }
        Boolean isRegisterMbeans = this.isRegisterMbeans == null ? hikariCpConfig.getIsRegisterMbeans() : this.isRegisterMbeans;
        if (isRegisterMbeans != null) {
            properties.setProperty("isRegisterMbeans", isRegisterMbeans.toString());
        }
        Boolean isAllowPoolSuspension = this.isAllowPoolSuspension == null ? hikariCpConfig.getIsAllowPoolSuspension() : this.isAllowPoolSuspension;
        if (isAllowPoolSuspension != null) {
            properties.setProperty("isAllowPoolSuspension", isAllowPoolSuspension.toString());
        }
        String poolName = this.poolName == null ? hikariCpConfig.getPoolName() : this.poolName;
        if (poolName != null && poolName.length() > 0) {
            properties.setProperty("poolName", poolName);
        }
        String schema = this.schema == null ? hikariCpConfig.getSchema() : this.schema;
        if (schema != null && schema.length() > 0) {
            properties.setProperty("schema", schema);
        }
        String exceptionOverrideClassName = this.exceptionOverrideClassName == null ? hikariCpConfig.getExceptionOverrideClassName() : this.exceptionOverrideClassName;
        if (exceptionOverrideClassName != null && exceptionOverrideClassName.length() > 0) {
            properties.setProperty("exceptionOverrideClassName", exceptionOverrideClassName);
        }
        Long keepaliveTime = this.keepaliveTime == null ? hikariCpConfig.getKeepaliveTime() : this.keepaliveTime;
        if (keepaliveTime != null) {
            properties.setProperty("keepaliveTime", String.valueOf(keepaliveTime));
        }
        Boolean sealed = this.sealed == null ? hikariCpConfig.getSealed() : this.sealed;
        if (sealed != null) {
            properties.setProperty("sealed", sealed.toString());
        }
        return properties;
    }
}
