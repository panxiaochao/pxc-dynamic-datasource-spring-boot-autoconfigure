package io.github.panxiaochao.datasource.core.holder;

import java.util.ArrayList;
import java.util.List;

/**
 * 切换数据源工具上下文类
 *
 * @author Lypxc
 */
public class DynamicDataSourceContextHolder {
    /**
     * TODO: 后续需要增加线程传递性
     */
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
    public static boolean containsKey(String key) {
        return DATA_SOURCE_LIST.contains(key);
    }
}
