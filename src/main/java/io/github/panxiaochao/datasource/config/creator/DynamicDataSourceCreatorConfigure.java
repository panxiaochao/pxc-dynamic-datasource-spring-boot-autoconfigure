package io.github.panxiaochao.datasource.config.creator;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * {@code DynamicDataSourceBuilderConfigure}
 * <p> 动态数据源创建，根据排序创建，默认druid > hikari
 *
 * @author Lypxc
 * @since 2022/7/18
 */
@Configuration
public class DynamicDataSourceCreatorConfigure {

    public static final int ORDER_DRUID = 2000;
    public static final int ORDER_HIKARI = 3000;

    /**
     * @param dataSourceCreators
     * @return
     */
    @Primary
    @Bean
    @ConditionalOnMissingBean
    public DefaultDataSourceCreator dataSourceCreator(List<DataSourceCreator> dataSourceCreators) {
        DefaultDataSourceCreator defaultDataSourceCreator = new DefaultDataSourceCreator();
        defaultDataSourceCreator.setBuilders(dataSourceCreators);
        return defaultDataSourceCreator;
    }

    /**
     * 存在Druid数据源时，加入创建器
     */
    @ConditionalOnClass(DruidDataSource.class)
    @Configuration
    static class DruidDataSourceCreatorConfiguration {
        /**
         * 采用内部类
         *
         * @return
         */
        @Bean
        @Order(ORDER_DRUID)
        public DruidDataSourceCreator druidDataSourceCreator() {
            return new DruidDataSourceCreator();
        }
    }

    /**
     * 存在Hikari数据源时, 加入创建器
     */
    @ConditionalOnClass(HikariDataSource.class)
    @Configuration
    static class HikariDataSourceCreatorConfiguration {
        /**
         * 采用内部类
         *
         * @return
         */
        @Bean
        @Order(ORDER_HIKARI)
        public HikariDataSourceCreator hikariDataSourceCreator() {
            return new HikariDataSourceCreator();
        }
    }
}
