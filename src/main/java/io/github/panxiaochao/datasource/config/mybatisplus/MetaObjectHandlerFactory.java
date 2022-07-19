package io.github.panxiaochao.datasource.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import java.lang.reflect.Constructor;

/**
 * {@code MetaObjectHandlerFactory}
 * <p>
 *
 * @author Mr_LyPxc
 * @since 2022-05-08
 */
public final class MetaObjectHandlerFactory {
    private static Constructor<? extends MetaObjectHandler> metaObjectConstructor;

    private MetaObjectHandlerFactory() {
        // disable construction
    }

    static {
        tryImplementation(MyMetaObjectHandler.class);
    }

    public static synchronized void userCustomClass(Class<? extends MetaObjectHandler> clazz) {
        setImplementation(clazz);
    }

    private static void tryImplementation(Class<? extends MetaObjectHandler> implClass) {
        setImplementation(implClass);
    }

    private static void setImplementation(Class<? extends MetaObjectHandler> implClass) {
        if (metaObjectConstructor != null) {
            return;
        }
        try {
            Constructor<? extends MetaObjectHandler> candidate = implClass.getConstructor();
            metaObjectConstructor = candidate;
        } catch (Throwable t) {
            // skip
            //throw new RuntimeException("Error setting MetaObjectHandler implementation.  Cause: " + t, t);
        }
    }

}
