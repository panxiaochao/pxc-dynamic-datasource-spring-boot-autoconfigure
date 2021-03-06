package io.github.panxiaochao.aop;

import io.github.panxiaochao.aop.after.CustomDoAfter;
import io.github.panxiaochao.aop.before.CustomDoBefore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * {@code CustomDefaultPointcutAdvisor}
 * <p>切点+通知
 *
 * @author Lypxc
 * @since 2022/1/6
 */
public class CustomDefaultPointcutAdvisor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomDefaultPointcutAdvisor.class);
    private String execution;
    private static final String ANNOTATION_DBSOURCE = "@annotation(io.github.panxiaochao.annotation.DbSource) || @within(io.github.panxiaochao.annotation.DbSource)";
    private static final int ORDER = -100;

    public CustomDefaultPointcutAdvisor(String execution) {
        if (StringUtils.isNotBlank(execution)) {
            this.execution = execution + " || " + ANNOTATION_DBSOURCE;
        } else {
            this.execution = ANNOTATION_DBSOURCE;
        }
    }

    public String getExecution() {
        return execution;
    }

    public void setExecution(String execution) {
        this.execution = execution;
    }

    /**
     * @param execution execution
     * @return CustomDefaultPointcutAdvisor
     */
    public static CustomDefaultPointcutAdvisor create(String execution) {
        return new CustomDefaultPointcutAdvisor(execution);
    }

    /**
     * 前置环绕
     *
     * @return DefaultPointcutAdvisor
     */
    public DefaultPointcutAdvisor doBefore() {
        LOGGER.info(">>> doBefore");
        DefaultPointcutAdvisor doBefore = new DefaultPointcutAdvisor();
        doBefore.setPointcut(buildPointcut());
        doBefore.setOrder(ORDER);
        CustomDoBefore customDoBefore = new CustomDoBefore();
        doBefore.setAdvice(customDoBefore);
        return doBefore;
    }

    /**
     * 后置环绕
     *
     * @return DefaultPointcutAdvisor
     */
    public DefaultPointcutAdvisor doAfter() {
        LOGGER.info(">>> doAfter");
        DefaultPointcutAdvisor doAfter = new DefaultPointcutAdvisor();
        doAfter.setPointcut(buildPointcut());
        doAfter.setOrder(ORDER);
        CustomDoAfter customDoAfter = new CustomDoAfter();
        doAfter.setAdvice(customDoAfter);
        return doAfter;
    }

    /**
     * 组合切点
     *
     * @return Pointcut
     */
    private Pointcut buildPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(execution);
        return pointcut;
    }
}
