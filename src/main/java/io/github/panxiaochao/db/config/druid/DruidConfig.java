package io.github.panxiaochao.db.config.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import io.github.panxiaochao.db.properties.PxcDynamicDataSourceProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Mr_LyPxc
 */
@Component
@ConditionalOnProperty(prefix = PxcDynamicDataSourceProperties.PXC_DATASOURCE_PREFIX + ".druid", name = "enabled", havingValue = "true")
public class DruidConfig {

    private String loginUsername;

    private String loginPassword;

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    /**
     * @return ServletRegistrationBean<StatViewServlet>
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> druidServlet() {
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<StatViewServlet>();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        // 黑名单 deny优先于allow，如果在deny列表中，就算在allow列表中，也会被拒绝。
        servletRegistrationBean.addInitParameter("deny", "");
        // 白名单 如果allow没有配置或者为空，则允许所有访问
        servletRegistrationBean.addInitParameter("allow", "");
        // 是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", "false");

        if (StringUtils.isNotBlank(loginUsername)) {
            servletRegistrationBean.addInitParameter("loginUsername", loginUsername);
        }

        if (StringUtils.isNotBlank(loginPassword)) {
            servletRegistrationBean.addInitParameter("loginPassword", loginPassword);
        }
        return servletRegistrationBean;
    }

    /**
     * @return FilterRegistrationBean<WebStatFilter>
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<WebStatFilter>();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        filterRegistrationBean.addInitParameter("principalCookieName", "USER_COOKIE");
        filterRegistrationBean.addInitParameter("principalSessionName", "USER_SESSION");
        filterRegistrationBean.addInitParameter("DruidWebStatFilter", "/*");
        return filterRegistrationBean;
    }

}
