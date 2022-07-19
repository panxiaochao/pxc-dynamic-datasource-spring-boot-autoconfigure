package io.github.panxiaochao.datasource.config.builder;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
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
public class DynamicDataSourceBuilderConfigure {

    public static final int ORDER_DRUID = 2000;
    public static final int ORDER_HIKARI = 3000;

    /**
     * @param dataSourceBuilders
     * @return
     */
    @Primary
    @Bean
    @ConditionalOnMissingBean
    public DefaultDataSourceBuilder dataSourceCreator(List<DataSourceBuilder> dataSourceBuilders) {
        DefaultDataSourceBuilder defaultDataSourceBuilder = new DefaultDataSourceBuilder();
        defaultDataSourceBuilder.setBuilders(dataSourceBuilders);
        return defaultDataSourceBuilder;
    }

    /**
     * 存在Druid数据源时，加入创建器
     */
    @ConditionalOnClass(DruidDataSource.class)
    @Configuration
    static class DruidDataSourceBuildConfiguration {
        /**
         * 采用内部类
         *
         * @return
         */
        @Bean
        @Order(ORDER_DRUID)
        public DruidDataSourceBuilder druidDataSourceBuilder() {
            return new DruidDataSourceBuilder();
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
        public HikariDataSourceBuilder hikariDataSourceBuilder() {
            return new HikariDataSourceBuilder();
        }
    }
}
