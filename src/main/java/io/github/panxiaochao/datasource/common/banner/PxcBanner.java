package io.github.panxiaochao.datasource.common.banner;

/**
 * <p> 启动图案
 *
 * @author LyPxc
 */
public class PxcBanner {
    private static final String BOTTOM_INFO = ":: PXC-DDS ::";

    /**
     * 打印banner
     */
    public static void printBanner() {
        Package pkg = PxcBanner.class.getPackage();
        String version = (pkg != null ? pkg.getImplementationVersion() : "null");
        System.out.println(bannerInfo(version));
    }

    /**
     * <p>生成字符串网址：https://www.bootschool.net/ascii
     * <p>字体选择：stick-letters、straight
     *
     * @return String
     */
    private static String bannerInfo(String version) {
        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append(" __     __     __  __  __ \n");
        textBuilder.append("|__)\\_//   __ |  \\|  \\(_  \n");
        textBuilder.append("|   / \\\\__    |__/|__/__) ");

        version = version != null ? " (v" + version + ")" : "";
        int strapLineSize = (textBuilder.length() / 3) - (BOTTOM_INFO.length() + version.length()) - 2;

        textBuilder.append(BOTTOM_INFO);
        for (int i = 0; i < strapLineSize; i++) {
            textBuilder.append(" ");
        }
        textBuilder.append(version);
        textBuilder.append("\n");
        return textBuilder.toString();
    }
}
