package io.github.panxiaochao.datasource.boot.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import io.github.panxiaochao.datasource.common.properties.DsProperties;
import io.github.panxiaochao.datasource.common.properties.DynamicDataSourceProperties;
import io.github.panxiaochao.datasource.config.creator.DynamicDataSourceCreatorConfigure;
import io.github.panxiaochao.datasource.config.datasource.DynamicDataSourceWrapper;
import io.github.panxiaochao.datasource.config.druid.DynamicDruidDataSourceConfigure;
import io.github.panxiaochao.datasource.core.aop.CustomDefaultPointcutAdvisor;
import io.github.panxiaochao.datasource.core.factory.DataSourceFactory;
import io.github.panxiaochao.datasource.core.factory.YmlDynamicDataSourceBean;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.session.ExecutorType;
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
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * <p>由于是自定义多数据源，又是借助于DruidDataSource来创建的，优先创建
 * <p>注意：所以必须在DataSourceAutoConfiguration 和 DruidDataSourceAutoConfigure之前创建！！！
 * <p>@AutoConfigureBefore({DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
 *
 * @author Lypxc
 * @since 2022-01-05
 */
@Configuration
@EnableConfigurationProperties({DsProperties.class, DynamicDataSourceProperties.class})
@ConditionalOnClass({SqlSessionFactory.class})
@AutoConfigureBefore(value = DataSourceAutoConfiguration.class, name = "com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure")
@Import(value = {DynamicDruidDataSourceConfigure.class, DynamicDataSourceCreatorConfigure.class})
@ConditionalOnProperty(prefix = DsProperties.PXC_DATASOURCE_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class PxcDynamicDataSourceAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(PxcDynamicDataSourceAutoConfiguration.class);

    private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();

    private final ApplicationContext applicationContext;

    private final DsProperties dsProperties;
    private final DynamicDataSourceProperties dynamicDataSourceProperties;

    public PxcDynamicDataSourceAutoConfiguration(ApplicationContext applicationContext, DsProperties dsProperties, DynamicDataSourceProperties dynamicDataSourceProperties) {
        this.applicationContext = applicationContext;
        this.dsProperties = dsProperties;
        this.dynamicDataSourceProperties = dynamicDataSourceProperties;
    }

    /**
     * @return
     */
    @Bean
    public DataSourceFactory ymlDynamicDataSourceBean() {
        return new YmlDynamicDataSourceBean(dynamicDataSourceProperties.getDynamic());
    }

    /**
     * 动态数据源配置
     *
     * @return DataSource
     */
    @Bean(name = "dynamicDataSource")
    @Primary
    @ConditionalOnMissingBean
    public DataSource dynamicDataSource() {
        LOGGER.info(">>> init dynamicDataSource");
        return DynamicDataSourceWrapper.create(applicationContext, dsProperties).build();
    }

    /**
     * SqlSessionFactory
     *
     * @return SqlSessionFactory
     * @throws Exception Exception
     */
    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dataSource) throws Exception {
        LOGGER.info(">>> init sqlSessionFactory");
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setVfs(SpringBootVFS.class);
        // MybatisConfiguration
        applyConfiguration(sqlSessionFactory);
        // 暂时不用config配置文件
        //factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
        // 分页插件
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(dsProperties.getDbType()));
        sqlSessionFactory.setPlugins(interceptor);
        if (!ObjectUtils.isEmpty(dsProperties.getMapperLocations())) {
            Resource[] mapperLocationsResource =
                    Stream.of(Optional.ofNullable(dsProperties.getMapperLocations()).orElse(new String[0]))
                            .flatMap(location -> Stream.of(getResources(location))).toArray(Resource[]::new);
            sqlSessionFactory.setMapperLocations(mapperLocationsResource);
        }
        if (StringUtils.hasLength(dsProperties.getTypeAliasesPackage())) {
            sqlSessionFactory.setTypeAliasesPackage(this.dsProperties.getTypeAliasesPackage());
            //该配置请和 typeAliasesPackage 一块儿使用，若是配置了该属性，则仅仅会扫描路径下以该类做为父类的域对象
            sqlSessionFactory.setTypeAliasesSuperType(Object.class);
        }
        sqlSessionFactory.setDatabaseIdProvider(databaseIdProvider());
        // GlobalConfig
        GlobalConfig globalConfig = new GlobalConfig();
        // 设置不显示Banner
        globalConfig.setBanner(false);
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setIdType(dsProperties.getIdType());
        globalConfig.setDbConfig(dbConfig);
        // 采用手动注入这种方式，不然失效
        Class<? extends MetaObjectHandler> implClass = dsProperties.getMetaObjectImpl();
        // 启动无参构造方法
        globalConfig.setMetaObjectHandler(implClass.newInstance());
        // Id 生成器
        globalConfig.setIdentifierGenerator(new DefaultIdentifierGenerator());
        sqlSessionFactory.setGlobalConfig(globalConfig);
        return sqlSessionFactory.getObject();
    }

    /**
     * 获取资源
     *
     * @param location
     * @return
     */
    private Resource[] getResources(String location) {
        try {
            return RESOURCE_PATTERN_RESOLVER.getResources(location);
        } catch (IOException e) {
            return new Resource[0];
        }
    }

    /**
     * 多种数据源支持
     *
     * <pre><select id="XX" databaseId="mysql"></pre>
     * <pre><if test="_databaseId == 'mysql'"></pre>
     *
     * @return
     */
    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("Oracle", "oracle");
        properties.setProperty("MySQL", "mysql");
        properties.setProperty("PostgreSQL", "postgresql");
        properties.setProperty("DB2", "db2");
        properties.setProperty("SQL Server", "sqlserver");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }

    /**
     * @param factory
     */
    private void applyConfiguration(MybatisSqlSessionFactoryBean factory) {
        // MybatisConfiguration
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        configuration.setLogImpl(NoLoggingImpl.class);
        factory.setConfiguration(configuration);
    }

    /**
     * @param sqlSessionFactory sqlSessionFactory
     * @return SqlSessionTemplate
     */
    @Bean
    @ConditionalOnMissingBean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        ExecutorType executorType = dsProperties.getExecutorType();
        if (executorType != null) {
            return new SqlSessionTemplate(sqlSessionFactory, executorType);
        } else {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }

    /**
     * 前置环绕
     *
     * @return DefaultPointcutAdvisor
     */
    @Bean
    public DefaultPointcutAdvisor doBefore() {
        String execution = dsProperties.getExecution();
        return CustomDefaultPointcutAdvisor.create(execution).doBefore();
    }

    /**
     * 后置环绕
     *
     * @return DefaultPointcutAdvisor
     */
    @Bean
    public DefaultPointcutAdvisor doAfter() {
        String execution = dsProperties.getExecution();
        return CustomDefaultPointcutAdvisor.create(execution).doAfter();
    }
}
