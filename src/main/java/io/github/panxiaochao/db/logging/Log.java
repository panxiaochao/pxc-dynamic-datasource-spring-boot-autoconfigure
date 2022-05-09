package io.github.panxiaochao.db.logging;

/**
 * {@code Log}
 * <p>
 *
 * @author Lypxc
 * @since 2022/1/7
 */
public interface Log {
    boolean isErrorEnabled();

    void error(String msg, Throwable e);

    void error(String msg);

    boolean isInfoEnabled();

    void info(String msg);

    void info(String msg, Object params);

    void info(String msg, Object... params);

    boolean isDebugEnabled();

    void debug(String msg);

    void debug(String msg, Throwable e);

    boolean isWarnEnabled();

    void warn(String msg);

    void warn(String msg, Throwable e);

    int getErrorCount();

    int getWarnCount();

    int getInfoCount();

    int getDebugCount();

    void resetStat();
}
