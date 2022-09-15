package io.github.panxiaochao.datasource.boot.autoconfigure;

import io.github.panxiaochao.datasource.common.properties.DsProperties;
import io.github.panxiaochao.datasource.common.properties.DynamicDataSourceProperties;
import io.github.panxiaochao.datasource.config.creator.DynamicDataSourceCreatorConfigure;
import io.github.panxiaochao.datasource.config.druid.DynamicDruidDataSourceConfigure;
import io.github.panxiaochao.datasource.core.aop.CustomDefaultPointcutAdvisor;
import io.github.panxiaochao.datasource.core.factory.DynamicDataSourceBean;
import io.github.panxiaochao.datasource.core.factory.YmlDynamicDataSourceBean;
import io.github.panxiaochao.datasource.core.route.DynamicRoutingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

/**
 * 多数据源自动配置类
 * <p>ADD: 2022-01-05
 * <p>由于是自定义多数据源，又是借助于DruidDataSource来创建的，优先创建
 * <p>注意：所以必须在DataSourceAutoConfiguration 和 DruidDataSourceAutoConfigure之前创建！！！
 * <p>@AutoConfigureBefore({DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
 *
 * <p>ADD: 2022-07-20
 * <p>本次加入多数据驱动创建，druid和hikari驱动
 *
 * @author Lypxc
 * @since 2022-07-20
 */
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@AutoConfigureBefore(value = DataSourceAutoConfiguration.class, name = "com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure")
@Import(value = {DynamicDruidDataSourceConfigure.class, DynamicDataSourceCreatorConfigure.class})
@ConditionalOnProperty(prefix = DsProperties.PXC_DATASOURCE_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DynamicDataSourceAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceAutoConfiguration.class);

    private final DynamicDataSourceProperties dynamicDataSourceProperties;

    public DynamicDataSourceAutoConfiguration(DynamicDataSourceProperties dynamicDataSourceProperties) {
        this.dynamicDataSourceProperties = dynamicDataSourceProperties;
    }

    /**
     * @return DynamicDataSourceBean
     */
    @Bean
    public DynamicDataSourceBean ymlDynamicDataSourceBean() {
        return new YmlDynamicDataSourceBean(dynamicDataSourceProperties.getDynamic());
    }

    /**
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        return dataSource;
    }

    /**
     * 前置环绕
     *
     * @return DefaultPointcutAdvisor
     */
    @Bean
    public DefaultPointcutAdvisor doBefore() {
        String execution = "";
        return CustomDefaultPointcutAdvisor.create(execution).doBefore();
    }

    /**
     * 后置环绕
     *
     * @return DefaultPointcutAdvisor
     */
    @Bean
    public DefaultPointcutAdvisor doAfter() {
        String execution = "";
        return CustomDefaultPointcutAdvisor.create(execution).doAfter();
    }
}
