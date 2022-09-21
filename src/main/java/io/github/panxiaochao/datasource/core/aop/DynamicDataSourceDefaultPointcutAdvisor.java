package io.github.panxiaochao.datasource.core.aop;

import io.github.panxiaochao.datasource.common.properties.DynamicDataSourceProperties;
import io.github.panxiaochao.datasource.core.aop.after.DynamicDataSourceAfterReturningAdvice;
import io.github.panxiaochao.datasource.core.aop.before.DynamicDataSourceMethodBeforeAdvice;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@code DynamicDataSourceDefaultPointcutAdvisor}
 * <p>切点+通知
 * <p>使用DefaultPointcutAdvisor最强通用增加类，可以吧Advice和Pointcut放在一起
 * <p>Pointcut: 切点 Advice: 增强（切入点做什么）
 * <p>PointCut + Advice形成了切面Aspect
 *
 * @author Lypxc
 * @since 2022-09-21
 */
@Setter
@Getter
@Configuration
@RequiredArgsConstructor
public class DynamicDataSourceDefaultPointcutAdvisor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceDefaultPointcutAdvisor.class);

    /**
     * <p>@annotation: 自定义注解标注在方法上
     * <p>@within: 自定义注解标注在的类上；该类的所有方法（不包含子类方法）
     */
    private static final String ANNOTATION_DB_SOURCE =
            "@annotation(io.github.panxiaochao.datasource.common.annotation.DSource) || @within(io.github.panxiaochao.datasource.common.annotation.DSource)";

    /**
     * 配置属性
     */
    private final DynamicDataSourceProperties dynamicDataSourceProperties;

    /**
     * 定义顺序，需要在事务之前切换数据源
     */
    private static final int ORDER = -100;

    /**
     * 方法执行前增强
     *
     * @return DefaultPointcutAdvisor
     */
    @Bean
    public DefaultPointcutAdvisor dynamicDataSourceMethodBeforeAdvice() {
        LOGGER.info(">>> do MethodBeforeAdvice");
        DefaultPointcutAdvisor methodBeforeAdvice =
                new DefaultPointcutAdvisor(buildPointcut(), new DynamicDataSourceMethodBeforeAdvice());
        methodBeforeAdvice.setOrder(ORDER);
        return methodBeforeAdvice;
    }

    /**
     * 后置环绕
     *
     * @return DefaultPointcutAdvisor
     */
    @Bean
    public DefaultPointcutAdvisor dynamicDataSourceAfterReturningAdvice() {
        LOGGER.info(">>> do AfterReturningAdvice");
        DefaultPointcutAdvisor afterReturningAdvice =
                new DefaultPointcutAdvisor(buildPointcut(), new DynamicDataSourceAfterReturningAdvice());
        afterReturningAdvice.setOrder(ORDER);
        return afterReturningAdvice;
    }

    /**
     * 组合切点，采用表达式节点
     *
     * @return Pointcut
     */
    private Pointcut buildPointcut() {
        // TODO：后面需要加入execution属性
        String execution = ANNOTATION_DB_SOURCE;
        if (dynamicDataSourceProperties.getExecution() != null
                && dynamicDataSourceProperties.getExecution().length() > 0) {
            execution = dynamicDataSourceProperties.getExecution() + " || " + execution;
        }
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(execution);
        return pointcut;
    }
}
