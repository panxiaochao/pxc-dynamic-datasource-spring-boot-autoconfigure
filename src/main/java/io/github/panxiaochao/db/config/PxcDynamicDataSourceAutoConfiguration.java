package io.github.panxiaochao.db.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import io.github.panxiaochao.db.aop.CustomDefaultPointcutAdvisor;
import io.github.panxiaochao.db.config.datasource.MultipleDataSourceWrapper;
import io.github.panxiaochao.db.logging.Log;
import io.github.panxiaochao.db.logging.LogFactory;
import io.github.panxiaochao.db.properties.PxcDynamicDataSourceProperties;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * <p>由于是自定义多数据源，又是借助于DruidDataSource来创建的，优先创建
 * <p>注意：所以必须在DataSourceAutoConfiguration 和 DruidDataSourceAutoConfigure之前创建！！！
 *
 * @author Lypxc
 * @since 2021-01-05
 */
@Configuration
@ConditionalOnClass({SqlSessionFactory.class})
@AutoConfigureBefore({DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
@EnableConfigurationProperties(PxcDynamicDataSourceProperties.class)
@ConditionalOnProperty(prefix = PxcDynamicDataSourceProperties.PXC_DATASOURCE_PREFIX, name = "enabled", havingValue = "true")
public class PxcDynamicDataSourceAutoConfiguration {

    private static final Log LOGGER = LogFactory.getLog(PxcDynamicDataSourceAutoConfiguration.class);

    private final ApplicationContext applicationContext;

    private final PxcDynamicDataSourceProperties pxcDynamicDataSourceProperties;

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
        LOGGER.info(">>> init multipleDataSource");
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
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(pxcDynamicDataSourceProperties.getDbType()));
        sqlSessionFactory.setPlugins(interceptor);
        Resource[] mapperLocations = pxcDynamicDataSourceProperties.resolveMapperLocations();
        if (!ObjectUtils.isEmpty(mapperLocations)) {
            sqlSessionFactory.setMapperLocations(mapperLocations);
        }
        if (StringUtils.hasLength(pxcDynamicDataSourceProperties.getTypeAliasesPackage())) {
            sqlSessionFactory.setTypeAliasesPackage(this.pxcDynamicDataSourceProperties.getTypeAliasesPackage());
            //该配置请和 typeAliasesPackage 一块儿使用，若是配置了该属性，则仅仅会扫描路径下以该类做为父类的域对象
            sqlSessionFactory.setTypeAliasesSuperType(Object.class);
        }
        sqlSessionFactory.setDatabaseIdProvider(databaseIdProvider());
        // GlobalConfig
        GlobalConfig globalConfig = new GlobalConfig();
        // 设置不显示Banner
        globalConfig.setBanner(false);
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setIdType(pxcDynamicDataSourceProperties.getIdType());
        globalConfig.setDbConfig(dbConfig);
        // 采用手动注入这种方式，不然失效
        Class<? extends MetaObjectHandler> implClass = pxcDynamicDataSourceProperties.getMetaObjectImpl();
        // 启动无参构造方法
        globalConfig.setMetaObjectHandler(implClass.newInstance());
        // Id 生成器
        globalConfig.setIdentifierGenerator(new DefaultIdentifierGenerator());
        sqlSessionFactory.setGlobalConfig(globalConfig);
        return sqlSessionFactory.getObject();
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
        ExecutorType executorType = pxcDynamicDataSourceProperties.getExecutorType();
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
