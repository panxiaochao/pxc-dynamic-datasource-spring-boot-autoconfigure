package io.github.panxiaochao.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import io.github.panxiaochao.aop.CustomDefaultPointcutAdvisor;
import io.github.panxiaochao.config.datasource.MultipleDataSourceWrapper;
import io.github.panxiaochao.config.druid.DruidConfig;
import io.github.panxiaochao.config.mybatisplus.CustomMetaObjectHandler;
import io.github.panxiaochao.properties.PxcDynamicDataSourceProperties;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * <p>由于是自定义多数据源，又是借助于DruidDataSource来创建的
 * 所以必须在DruidDataSourceAutoConfigure之前创建！！！
 *
 * @author Lypxc
 * @since 2021-01-05
 */
@Configuration
@ConditionalOnClass({SqlSessionFactory.class})
@AutoConfigureBefore({DruidDataSourceAutoConfigure.class})
@EnableConfigurationProperties(PxcDynamicDataSourceProperties.class)
@ConditionalOnProperty(prefix = PxcDynamicDataSourceProperties.PXCDYNAMIC_PREFIX, name = "enabled", havingValue = "true")
@Import({DruidConfig.class})
public class PxcDynamicDataSourceAutoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(PxcDynamicDataSourceAutoConfiguration.class);

    private ApplicationContext applicationContext;
    private PxcDynamicDataSourceProperties pxcDynamicDataSourceProperties;

    public PxcDynamicDataSourceAutoConfiguration(ApplicationContext applicationContext, PxcDynamicDataSourceProperties pxcDynamicDataSourceProperties) {
        this.applicationContext = applicationContext;
        this.pxcDynamicDataSourceProperties = pxcDynamicDataSourceProperties;
    }

    /**
     * 动态数据源配置
     *
     * @return DataSource
     */
    @Bean(name = "multipleDataSource")
    @Primary
    @ConditionalOnMissingBean
    public DataSource multipleDataSource() {
        LOGGER.info(">>> Init MultipleDataSource...");
        return MultipleDataSourceWrapper.create(applicationContext, pxcDynamicDataSourceProperties).build();
    }

    /**
     * SqlSessionFactory
     *
     * @return SqlSessionFactory
     * @throws Exception Exception
     */
    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("multipleDataSource") DataSource dataSource) throws Exception {
        LOGGER.info(">>> Init sqlSessionFactory...");
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        // 分页插件
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        sqlSessionFactory.setPlugins(interceptor);
        Resource[] mapperLocations = pxcDynamicDataSourceProperties.resolveMapperLocations();
        if (!ObjectUtils.isEmpty(mapperLocations)) {
            sqlSessionFactory.setMapperLocations(mapperLocations);
        }
        if (StringUtils.hasLength(pxcDynamicDataSourceProperties.getTypeAliasesPackage())) {
            sqlSessionFactory.setTypeAliasesPackage(this.pxcDynamicDataSourceProperties.getTypeAliasesPackage());
        }
        sqlSessionFactory.setTypeAliasesSuperType(Object.class);
        // GlobalConfig
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setBanner(false);
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setIdType(IdType.AUTO);
        globalConfig.setDbConfig(dbConfig);
        // 采用手动注入这种方式，不然失效
        globalConfig.setMetaObjectHandler(new CustomMetaObjectHandler());
        sqlSessionFactory.setGlobalConfig(globalConfig);
        // MybatisConfiguration
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        configuration.setLogImpl(NoLoggingImpl.class);
        sqlSessionFactory.setConfiguration(configuration);
        return sqlSessionFactory.getObject();
    }

    /**
     * @param sqlSessionFactory sqlSessionFactory
     * @return SqlSessionTemplate
     */
    @Bean
    @ConditionalOnMissingBean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 前置环绕
     *
     * @return DefaultPointcutAdvisor
     */
    @Bean
    public DefaultPointcutAdvisor doBefore() {
        String execution = pxcDynamicDataSourceProperties.getExecution();
        return CustomDefaultPointcutAdvisor.create(execution).doBefore();
    }

    /**
     * 后置环绕
     *
     * @return DefaultPointcutAdvisor
     */
    @Bean
    public DefaultPointcutAdvisor doAfter() {
        String execution = pxcDynamicDataSourceProperties.getExecution();
        return CustomDefaultPointcutAdvisor.create(execution).doAfter();
    }
}
