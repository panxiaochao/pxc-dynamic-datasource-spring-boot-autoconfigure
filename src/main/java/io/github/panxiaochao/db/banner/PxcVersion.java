package io.github.panxiaochao.db.banner;

/**
 * @author Lypxc
 */
public class PxcVersion {
    /**
     * 返回版本信息
     *
     * @return String
     */
    public static String version() {
        Package pkg = PxcVersion.class.getPackage();
        return (pkg != null ? pkg.getImplementationVersion() : "null");
    }
}
