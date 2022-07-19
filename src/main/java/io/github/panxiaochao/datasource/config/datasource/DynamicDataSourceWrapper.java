package io.github.panxiaochao.datasource.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import io.github.panxiaochao.datasource.common.enums.DsEnum;
import io.github.panxiaochao.datasource.common.exception.DsException;
import io.github.panxiaochao.datasource.common.properties.DsProperties;
import io.github.panxiaochao.datasource.config.DynamicDataSource;
import io.github.panxiaochao.datasource.core.holder.DynamicDataSourceContextHolder;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>自定义多数据源配置
 *
 * @author Lypxc
 */
public class DynamicDataSourceWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceWrapper.class);
    private static final String MASTER = "master";
    private ApplicationContext applicationContext;
    private DsProperties dsProperties;

    public DynamicDataSourceWrapper() {
    }

    public DynamicDataSourceWrapper(ApplicationContext applicationContext,
                                    DsProperties dsProperties) {
        this.applicationContext = applicationContext;
        this.dsProperties = dsProperties;
    }

    /**
     * @param applicationContext applicationContext
     * @param dsProperties       dsProperties
     * @return DynamicDataSourceWrapper
     */
    public static DynamicDataSourceWrapper create(ApplicationContext applicationContext,
                                                  DsProperties dsProperties) {
        return new DynamicDataSourceWrapper(applicationContext, dsProperties);
    }

    /**
     * @return DataSource
     */
    public DataSource build() {
        LOGGER.info(">>> DataSource.build");
        // 设置多数据源target
        Map<String, DataSource> dataSourceMap = bindDruidProperties();
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        for (String dateSourceKey : dataSourceMap.keySet()) {
            DataSource dataSource = dataSourceMap.get(dateSourceKey);
            targetDataSources.put(dateSourceKey, dataSource);
        }
        dynamicDataSource.setTargetDataSources(targetDataSources);
        // 设置默认数据库
        DataSource master = dataSourceMap.get(MASTER);
        dynamicDataSource.setDefaultTargetDataSource(master);
        return dynamicDataSource;
    }

    /**
     * 绑定参数属性，返回多数据源参数
     *
     * @return Map<String, DataSource>
     */
    private Map<String, DataSource> bindDruidProperties() {
        LOGGER.info(">>> DataSource.bindDruidProperties");
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        // 首先判断数据库，是否存在Master，这个属性必须存在，单个数据库也要配置
        Map<String, DsProperties.DruidProperties> dynamicMap = dsProperties.getDynamic();
        if (!dynamicMap.containsKey(MASTER)) {
            throw new DsException("请设置主数据源`spring.datasource.dynamic.master`相关配置");
        }
        for (String key : dynamicMap.keySet()) {
            // 都以小写为key，统一规则
            key = key.toLowerCase();
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                    .rootBeanDefinition(DruidDataSource.class);
            // 设置别名
            beanDefinitionBuilder.addPropertyValue("name", key);
            beanDefinitionBuilder.setDestroyMethodName("close");
            beanDefinitionBuilder.setInitMethodName("init");
            beanDefinitionBuilder.getBeanDefinition().setAttribute("id", uuidStr());
            // 以下设置数据库链接属性
            DsProperties.DruidProperties druidProperties = dynamicMap.get(key);
            String properties = druidProperties.getUrl();
            if (StringUtils.isNotBlank(properties)) {
                beanDefinitionBuilder.addPropertyValue("url", properties);
            } else {
                throw new DsException(key + " url is not set!");
            }

            properties = druidProperties.getUsername();
            if (StringUtils.isNotBlank(properties)) {
                beanDefinitionBuilder.addPropertyValue("username", properties);
            } else {
                throw new DsException(key + " username is not set!");
            }

            properties = druidProperties.getPassword();
            if (StringUtils.isNotBlank(properties)) {
                beanDefinitionBuilder.addPropertyValue("password", properties);
            } else {
                throw new DsException(key + " password is not set!");
            }

            properties = druidProperties.getDriverClassName();
            if (StringUtils.isNotBlank(properties)) {
                beanDefinitionBuilder.addPropertyValue("driverClassName", properties);
            } else {
                throw new DsException(key + " driverClassName is not set!");
            }

            // 以下是设置数据库配置属性
            addPropertyValue(beanDefinitionBuilder);

            // 注册以key为beanName的DataSource
            defaultListableBeanFactory.registerBeanDefinition(key, beanDefinitionBuilder.getBeanDefinition());
            dataSourceMap.put(key, applicationContext.getBean(key, DataSource.class));
            DynamicDataSourceContextHolder.DATA_SOURCE_LIST.add(key);
        }
        if (DynamicDataSourceContextHolder.DATA_SOURCE_LIST.size() > DsEnum.values().length) {
            throw new DsException("默认支持1个master主数据库，5个db从数据库！");
        }
        return dataSourceMap;
    }

    /**
     * 以下是设置数据库配置属性
     *
     * @param beanDefinitionBuilder beanDefinitionBuilder
     */
    private void addPropertyValue(BeanDefinitionBuilder beanDefinitionBuilder) {
        //beanDefinitionBuilder.addPropertyValue("initialSize", dsProperties.getInitialSize());
        //beanDefinitionBuilder.addPropertyValue("minIdle", dsProperties.getMinIdle());
        //beanDefinitionBuilder.addPropertyValue("maxActive", dsProperties.getMaxActive());
        //beanDefinitionBuilder.addPropertyValue("maxWait", dsProperties.getMaxWait());
        //beanDefinitionBuilder.addPropertyValue("timeBetweenEvictionRunsMillis", dsProperties.getTimeBetweenEvictionRunsMillis());
        //beanDefinitionBuilder.addPropertyValue("minEvictableIdleTimeMillis", dsProperties.getMinEvictableIdleTimeMillis());
        //
        //String properties = dsProperties.getValidationQuery();
        //if (StringUtils.isNotBlank(properties)) {
        //    beanDefinitionBuilder.addPropertyValue("validationQuery", properties);
        //}
        //
        //beanDefinitionBuilder.addPropertyValue("testWhileIdle", dsProperties.isTestWhileIdle());
        //beanDefinitionBuilder.addPropertyValue("testOnBorrow", dsProperties.isTestOnBorrow());
        //beanDefinitionBuilder.addPropertyValue("testOnReturn", dsProperties.isTestOnReturn());
        //beanDefinitionBuilder.addPropertyValue("poolPreparedStatements",
        //        dsProperties.isPoolPreparedStatements());
        //beanDefinitionBuilder.addPropertyValue("maxPoolPreparedStatementPerConnectionSize",
        //        dsProperties.getMaxPoolPreparedStatementPerConnectionSize());
        //
        //properties = dsProperties.getFilters();
        //if (StringUtils.isNotBlank(properties)) {
        //    beanDefinitionBuilder.addPropertyValue("filters", properties);
        //}
        //
        //properties = dsProperties.getConnectionProperties();
        //if (StringUtils.isNotBlank(properties)) {
        //    beanDefinitionBuilder.addPropertyValue("connectionProperties", properties);
        //}
        //
        //beanDefinitionBuilder.addPropertyValue("useGlobalDataSourceStat",
        //        dsProperties.isUseGlobalDataSourceStat());
    }

    /**
     * uuidStr
     *
     * @return String
     */
    private static String uuidStr() {
        return Base64.encodeBase64String(UUID.randomUUID().toString().getBytes());
    }
}
