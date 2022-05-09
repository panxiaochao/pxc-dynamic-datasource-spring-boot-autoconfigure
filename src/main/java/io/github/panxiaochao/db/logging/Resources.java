package io.github.panxiaochao.db.logging;

/**
 * {@code Resources}
 * <p>
 *
 * @author Lypxc
 * @since 2022/1/7
 */
public class Resources extends Object {

    private static ClassLoader defaultClassLoader;

    private Resources() {
    }

    /**
     * Returns the default classloader (may be null).
     *
     * @return The default classloader
     */
    public static ClassLoader getDefaultClassLoader() {
        return defaultClassLoader;
    }

    /**
     * Sets the default classloader
     *
     * @param defaultClassLoader - the new default ClassLoader
     */
    public static void setDefaultClassLoader(ClassLoader defaultClassLoader) {
        defaultClassLoader = defaultClassLoader;
    }

    /**
     * Loads a class
     * loadClass 只会把class加载到jvm
     * Class.forName 会把class加载到jvm，也会执行class中的static代码块
     *
     * @param className - the class to load
     * @return The loaded class
     * @throws ClassNotFoundException If the class cannot be found (duh!)
     */
    public static Class<?> classForName(String className) throws ClassNotFoundException {
        Class<?> clazz = null;
        try {
            clazz = getClassLoader().loadClass(className);
        } catch (Exception e) {
            // Ignore. Failsafe below.
        }
        if (clazz == null) {
            clazz = Class.forName(className);
        }
        return clazz;
    }

    private static ClassLoader getClassLoader() {
        if (defaultClassLoader != null) {
            return defaultClassLoader;
        } else {
            return Thread.currentThread().getContextClassLoader();
        }
    }
}
