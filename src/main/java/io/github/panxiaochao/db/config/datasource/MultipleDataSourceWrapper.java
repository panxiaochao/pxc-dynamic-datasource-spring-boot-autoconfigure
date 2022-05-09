package io.github.panxiaochao.db.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import io.github.panxiaochao.db.enums.DbSourceEnum;
import io.github.panxiaochao.db.exception.MultipleDataSourceException;
import io.github.panxiaochao.db.logging.Log;
import io.github.panxiaochao.db.logging.LogFactory;
import io.github.panxiaochao.db.multiple.DbSourceContextHolder;
import io.github.panxiaochao.db.multiple.DynamicDataSource;
import io.github.panxiaochao.db.properties.PxcDynamicDataSourceProperties;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
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
public class MultipleDataSourceWrapper {
    private static final Log LOGGER = LogFactory.getLog(MultipleDataSourceWrapper.class);
    private static final String MASTER = "master";
    private ApplicationContext applicationContext;
    private PxcDynamicDataSourceProperties pxcDynamicDataSourceProperties;

    public MultipleDataSourceWrapper() {
    }

    public MultipleDataSourceWrapper(ApplicationContext applicationContext,
                                     PxcDynamicDataSourceProperties pxcDynamicDataSourceProperties) {
        this.applicationContext = applicationContext;
        this.pxcDynamicDataSourceProperties = pxcDynamicDataSourceProperties;
    }

    /**
     * @param applicationContext             applicationContext
     * @param pxcDynamicDataSourceProperties pxcDynamicDataSourceProperties
     * @return MultipleDataSourceWrapper
     */
    public static MultipleDataSourceWrapper create(ApplicationContext applicationContext,
                                                   PxcDynamicDataSourceProperties pxcDynamicDataSourceProperties) {
        return new MultipleDataSourceWrapper(applicationContext, pxcDynamicDataSourceProperties);
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
        Map<String, PxcDynamicDataSourceProperties.DruidProperties> dynamicMap = pxcDynamicDataSourceProperties.getDynamic();
        if (!dynamicMap.containsKey(MASTER)) {
            throw new MultipleDataSourceException("请设置主数据源`spring.datasource.dynamic.master`相关配置");
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
            PxcDynamicDataSourceProperties.DruidProperties druidProperties = dynamicMap.get(key);
            String properties = druidProperties.getUrl();
            if (StringUtils.isNotBlank(properties)) {
                beanDefinitionBuilder.addPropertyValue("url", properties);
            } else {
                throw new MultipleDataSourceException(key + " url is not set!");
            }

            properties = druidProperties.getUsername();
            if (StringUtils.isNotBlank(properties)) {
                beanDefinitionBuilder.addPropertyValue("username", properties);
            } else {
                throw new MultipleDataSourceException(key + " username is not set!");
            }

            properties = druidProperties.getPassword();
            if (StringUtils.isNotBlank(properties)) {
                beanDefinitionBuilder.addPropertyValue("password", properties);
            } else {
                throw new MultipleDataSourceException(key + " password is not set!");
            }

            properties = druidProperties.getDriverClassName();
            if (StringUtils.isNotBlank(properties)) {
                beanDefinitionBuilder.addPropertyValue("driverClassName", properties);
            } else {
                throw new MultipleDataSourceException(key + " driverClassName is not set!");
            }

            // 以下是设置数据库配置属性
            addPropertyValue(beanDefinitionBuilder);

            // 注册以key为beanName的DataSource
            defaultListableBeanFactory.registerBeanDefinition(key, beanDefinitionBuilder.getBeanDefinition());
            dataSourceMap.put(key, applicationContext.getBean(key, DataSource.class));
            DbSourceContextHolder.DATA_SOURCE_LIST.add(key);
        }
        if (DbSourceContextHolder.DATA_SOURCE_LIST.size() > DbSourceEnum.values().length) {
            throw new MultipleDataSourceException("默认支持1个master主数据库，5个db从数据库！");
        }
        return dataSourceMap;
    }

    /**
     * 以下是设置数据库配置属性
     *
     * @param beanDefinitionBuilder beanDefinitionBuilder
     */
    private void addPropertyValue(BeanDefinitionBuilder beanDefinitionBuilder) {
        beanDefinitionBuilder.addPropertyValue("initialSize", pxcDynamicDataSourceProperties.getInitialSize());
        beanDefinitionBuilder.addPropertyValue("minIdle", pxcDynamicDataSourceProperties.getMinIdle());
        beanDefinitionBuilder.addPropertyValue("maxActive", pxcDynamicDataSourceProperties.getMaxActive());
        beanDefinitionBuilder.addPropertyValue("maxWait", pxcDynamicDataSourceProperties.getMaxWait());
        beanDefinitionBuilder.addPropertyValue("timeBetweenEvictionRunsMillis", pxcDynamicDataSourceProperties.getTimeBetweenEvictionRunsMillis());
        beanDefinitionBuilder.addPropertyValue("minEvictableIdleTimeMillis", pxcDynamicDataSourceProperties.getMinEvictableIdleTimeMillis());

        String properties = pxcDynamicDataSourceProperties.getValidationQuery();
        if (StringUtils.isNotBlank(properties)) {
            beanDefinitionBuilder.addPropertyValue("validationQuery", properties);
        }

        beanDefinitionBuilder.addPropertyValue("testWhileIdle", pxcDynamicDataSourceProperties.isTestWhileIdle());
        beanDefinitionBuilder.addPropertyValue("testOnBorrow", pxcDynamicDataSourceProperties.isTestOnBorrow());
        beanDefinitionBuilder.addPropertyValue("testOnReturn", pxcDynamicDataSourceProperties.isTestOnReturn());
        beanDefinitionBuilder.addPropertyValue("poolPreparedStatements",
                pxcDynamicDataSourceProperties.isPoolPreparedStatements());
        beanDefinitionBuilder.addPropertyValue("maxPoolPreparedStatementPerConnectionSize",
                pxcDynamicDataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());

        properties = pxcDynamicDataSourceProperties.getFilters();
        if (StringUtils.isNotBlank(properties)) {
            beanDefinitionBuilder.addPropertyValue("filters", properties);
        }

        properties = pxcDynamicDataSourceProperties.getConnectionProperties();
        if (StringUtils.isNotBlank(properties)) {
            beanDefinitionBuilder.addPropertyValue("connectionProperties", properties);
        }

        beanDefinitionBuilder.addPropertyValue("useGlobalDataSourceStat",
                pxcDynamicDataSourceProperties.isUseGlobalDataSourceStat());
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
