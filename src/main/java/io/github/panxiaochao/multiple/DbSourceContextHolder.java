package io.github.panxiaochao.multiple;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr_LyPxc
 */
public class DbSourceContextHolder {
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();
    public static final List<String> DATA_SOURCE_LIST = new ArrayList<>();

    /**
     * 设置数据源
     *
     * @param dbSource 数据源
     */
    public static void set(String dbSource) {
        CONTEXT_HOLDER.set(dbSource);
    }

    /**
     * 取得当前数据源
     *
     * @return String
     */
    public static String get() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清除上下文数据
     */
    public static void clear() {
        CONTEXT_HOLDER.remove();
    }

    /**
     * 是否包含数据库
     *
     * @param key key
     * @return boolean
     */
    public boolean containsKey(String key) {
        return DATA_SOURCE_LIST.contains(key);
    }
}
