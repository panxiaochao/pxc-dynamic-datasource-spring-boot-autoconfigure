package io.github.panxiaochao.db.logging.impl;


import io.github.panxiaochao.db.logging.Log;

/**
 * {@code NoLoggingImpl}
 * <p>
 *
 * @author Lypxc
 * @since 2022/1/7
 */
public class NoLoggingImpl implements Log {

    private int infoCount;
    private int errorCount;
    private int warnCount;
    private int debugCount;
    private String loggerName;

    private boolean debugEnable = false;
    private boolean infoEnable = true;
    private boolean warnEnable = true;
    private boolean errorEnable = true;

    public NoLoggingImpl(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getLoggerName() {
        return this.loggerName;
    }

    @Override
    public boolean isDebugEnabled() {
        return debugEnable;
    }

    @Override
    public void error(String s, Throwable e) {
        if (!errorEnable) {
            return;
        }

        error(s);

        if (e != null) {
            e.printStackTrace();
        }
    }

    @Override
    public void error(String s) {
        errorCount++;
        if (s != null) {
            System.err.println(loggerName + " : " + s);
        }
    }

    @Override
    public void debug(String s) {
        debugCount++;
    }

    @Override
    public void debug(String s, Throwable e) {
        debugCount++;
    }

    @Override
    public void warn(String s) {
        warnCount++;
    }

    @Override
    public void warn(String s, Throwable e) {
        warnCount++;
    }

    @Override
    public int getErrorCount() {
        return errorCount;
    }

    @Override
    public int getWarnCount() {
        return warnCount;
    }

    @Override
    public void resetStat() {
        errorCount = 0;
        warnCount = 0;
        infoCount = 0;
        debugCount = 0;
    }

    @Override
    public boolean isInfoEnabled() {
        return infoEnable;
    }

    @Override
    public void info(String s) {
        infoCount++;
    }

    @Override
    public void info(String msg, Object params) {
    }

    @Override
    public void info(String msg, Object... params) {
    }

    @Override
    public boolean isWarnEnabled() {
        return warnEnable;
    }

    @Override
    public int getInfoCount() {
        return infoCount;
    }

    @Override
    public int getDebugCount() {
        return debugCount;
    }

    @Override
    public boolean isErrorEnabled() {
        return errorEnable;
    }

    public void setErrorEnabled(boolean value) {
        this.errorEnable = value;
    }
}
