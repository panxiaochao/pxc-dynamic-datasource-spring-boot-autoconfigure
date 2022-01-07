package io.github.panxiaochao.banner;

/**
 * @author Lypxc
 */
public class PxcVersion {
    public PxcVersion() {
    }

    /**
     * @return PxcVersion
     */
    public static PxcVersion creat() {
        return new PxcVersion();
    }

    /**
     * 返回版本信息
     *
     * @return String
     */
    public String version() {
        Package pkg = PxcVersion.class.getPackage();
        return (pkg != null ? pkg.getImplementationVersion() : "null");
    }
}
