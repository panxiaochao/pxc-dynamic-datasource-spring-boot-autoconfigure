package io.github.panxiaochao.db.logging.impl;

import io.github.panxiaochao.db.logging.Log;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * {@code Log4j2Impl}
 * <p>
 *
 * @author Lypxc
 * @since 2022/1/7
 */
public class Log4j2Impl implements Log {

    private Logger log;

    private int errorCount;
    private int warnCount;
    private int infoCount;
    private int debugCount;

    public Log4j2Impl(Logger log) {
        this.log = log;
    }

    public Log4j2Impl(String loggerName) {
        log = LogManager.getLogger(loggerName);
    }

    public Logger getLog() {
        return log;
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public void error(String s, Throwable e) {
        errorCount++;
        log.error(s, e);
    }

    @Override
    public void error(String s) {
        errorCount++;
        log.error(s);
    }

    @Override
    public void debug(String s) {
        debugCount++;
        log.debug(s);
    }

    @Override
    public void debug(String s, Throwable e) {
        debugCount++;
        log.debug(s, e);
    }

    @Override
    public void warn(String s) {
        log.warn(s);
        warnCount++;
    }

    @Override
    public void warn(String s, Throwable e) {
        log.warn(s, e);
        warnCount++;
    }

    @Override
    public int getWarnCount() {
        return warnCount;
    }

    @Override
    public int getErrorCount() {
        return errorCount;
    }

    @Override
    public void resetStat() {
        errorCount = 0;
        warnCount = 0;
        infoCount = 0;
        debugCount = 0;
    }

    @Override
    public int getDebugCount() {
        return debugCount;
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        infoCount++;
        log.info(msg);
    }

    @Override
    public void info(String msg, Object params) {
        infoCount++;
        log.info(msg, params);
    }

    @Override
    public void info(String msg, Object... params) {
        infoCount++;
        log.info(msg, params);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isEnabled(Level.WARN);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public int getInfoCount() {
        return infoCount;
    }

}
