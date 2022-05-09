package io.github.panxiaochao.db.logging.impl;


import io.github.panxiaochao.db.logging.Log;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * {@code Log4jImpl}
 * <p>
 *
 * @author Lypxc
 * @since 2022/1/7
 */
public class Log4jImpl implements Log {

    private static final String callerFQCN = Log4jImpl.class.getName();

    private Logger log;

    private int errorCount;
    private int warnCount;
    private int infoCount;
    private int debugCount;

    public Log4jImpl(Logger log) {
        this.log = log;
    }

    public Log4jImpl(String loggerName) {
        log = Logger.getLogger(loggerName);
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
        log.log(callerFQCN, Level.ERROR, s, e);
    }

    @Override
    public void error(String s) {
        errorCount++;
        log.log(callerFQCN, Level.ERROR, s, null);
    }

    @Override
    public void debug(String s) {
        debugCount++;
        log.log(callerFQCN, Level.DEBUG, s, null);
    }

    @Override
    public void debug(String s, Throwable e) {
        debugCount++;
        log.log(callerFQCN, Level.DEBUG, s, e);
    }

    @Override
    public void warn(String s) {
        log.log(callerFQCN, Level.WARN, s, null);
        warnCount++;
    }

    @Override
    public void warn(String s, Throwable e) {
        log.log(callerFQCN, Level.WARN, s, e);
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
        log.log(callerFQCN, Level.INFO, msg, null);
    }
    @Override
    public void info(String msg, Object params) {
    }

    @Override
    public void info(String msg, Object... params) {
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isEnabledFor(Level.WARN);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isEnabledFor(Level.ERROR);
    }

    @Override
    public int getInfoCount() {
        return infoCount;
    }

}
