package io.github.panxiaochao.datasource.common.properties;

import io.github.panxiaochao.datasource.config.druid.DruidConfig;
import io.github.panxiaochao.datasource.config.hikari.HikariCpConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@code DynamicDataSourceProperties}
 * <p> 属性配置，来源于YML
 *
 * @author Lypxc
 * @since 2022/7/18
 */
@ConfigurationProperties(prefix = DynamicDataSourceProperties.PREFIX, ignoreInvalidFields = true)
public class DynamicDataSourceProperties {
    /**
     * 属性前缀
     */
    public static final String PREFIX = "pxc.datasource";
    private Map<String, DataSourceProperty> dynamic = new LinkedHashMap<>();
    @NestedConfigurationProperty
    private DruidConfig druid = new DruidConfig();
    @NestedConfigurationProperty
    private HikariCpConfig hikari = new HikariCpConfig();


    public Map<String, DataSourceProperty> getDynamic() {
        return dynamic;
    }

    public void setDynamic(Map<String, DataSourceProperty> dynamic) {
        this.dynamic = dynamic;
    }

    public DruidConfig getDruid() {
        return druid;
    }

    public void setDruid(DruidConfig druid) {
        this.druid = druid;
    }

    public HikariCpConfig getHikari() {
        return hikari;
    }

    public void setHikari(HikariCpConfig hikari) {
        this.hikari = hikari;
    }
}
