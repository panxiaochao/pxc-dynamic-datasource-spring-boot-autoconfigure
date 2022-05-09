package io.github.panxiaochao.db.logging;


import io.github.panxiaochao.db.logging.impl.*;

import java.lang.reflect.Constructor;

/**
 * {@code LogFactory}
 * <p>
 *
 * @author Lypxc
 * @since 2022/1/7
 */
public final class LogFactory {

    private static Constructor<? extends Log> logConstructor;

    static {
        //tryImplementation("org.slf4j.Logger", "io.github.panxiaochao.db.logging.impl.SLF4JImpl");
        //tryImplementation("org.apache.log4j.Logger", "io.github.panxiaochao.db.logging.impl.Log4jImpl");
        //tryImplementation("org.apache.logging.log4j.Logger", "io.github.panxiaochao.db.logging.impl.Log4j2Impl");
        //tryImplementation("org.apache.commons.logging.LogFactory",
        //        "io.github.panxiaochao.db.logging.impl.JakartaCommonsLoggingImpl");
        //tryImplementation("java.util.logging.Logger", "io.github.panxiaochao.db.logging.impl.Jdk14LoggingImpl");
        //tryImplementation("java.util.logging.Logger", "io.github.panxiaochao.db.logging.impl.Jdk14LoggingImpl");

        tryImplementation(SLF4JImpl.class);
        tryImplementation(Log4jImpl.class);
        tryImplementation(Log4j2Impl.class);
        tryImplementation(JakartaCommonsLoggingImpl.class);
        tryImplementation(Jdk14LoggingImpl.class);
        tryImplementation(NoLoggingImpl.class);
    }

    private LogFactory() {
        // disable construction
    }


    @SuppressWarnings("unchecked")
    private static void tryImplementation(String testClassName, String implClassName) {
        if (logConstructor != null) {
            return;
        }

        try {
            Resources.classForName(testClassName);
            Class implClass = Resources.classForName(implClassName);
            logConstructor = implClass.getConstructor(new Class[]{String.class});

            Class<?> declareClass = logConstructor.getDeclaringClass();
            if (!Log.class.isAssignableFrom(declareClass)) {
                logConstructor = null;
            }

            try {
                if (null != logConstructor) {
                    logConstructor.newInstance(LogFactory.class.getName());
                }
            } catch (Throwable t) {
                logConstructor = null;
            }

        } catch (Throwable t) {
            // skip
            //System.out.println(t.getMessage());
        }
    }

    private static void tryImplementation(Class<? extends Log> implClass) {
        setImplementation(implClass);
    }


    public static Log getLog(Class clazz) {
        return getLog(clazz.getName());
    }

    public static Log getLog(String loggerName) {
        try {
            return logConstructor.newInstance(loggerName);
        } catch (Throwable t) {
            throw new RuntimeException("Error creating logger for logger '" + loggerName + "'.  Cause: " + t, t);
        }
    }

    public static synchronized void userCustomClass(Class<? extends Log> clazz) {
        setImplementation(clazz);
    }

    private static void setImplementation(Class<? extends Log> implClass) {
        if (logConstructor != null) {
            return;
        }
        try {
            Constructor<? extends Log> candidate = implClass.getConstructor(String.class);
            Log log = candidate.newInstance(LogFactory.class.getName());
            if (log.isDebugEnabled()) {
                log.debug("Logging initialized using '" + implClass + "' adapter.");
            }
            logConstructor = candidate;
        } catch (Throwable t) {
            // skip
            //throw new RuntimeException("Error setting Log implementation.  Cause: " + t, t);
        }
    }
}
