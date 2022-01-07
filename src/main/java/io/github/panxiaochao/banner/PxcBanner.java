package io.github.panxiaochao.banner;

/**
 * <p>启动图案
 *
 * @author Mr_LyPxc
 */
public class PxcBanner {
    private static final String PXC_JWT = ":: Pxc-Dynamic-DataSource ::";

    /**
     * 打印banner
     */
    public void printBanner() {
        String version = PxcVersion.creat().version();
        System.out.println(bannerInfo(version));
    }

    /**
     * 生成字符串网址：https://www.bootschool.net/ascii <br/>
     * 字体选择：stick-letters
     *
     * @return String
     */
    private String bannerInfo(String version) {
        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append(" __       __      __                         __      __       ___       __   __        __   __   ___ \n");
        textBuilder.append("|__) \\_/ /  ` __ |  \\ \\ / |\\ |  /\\   |\\/| | /  ` __ |  \\  /\\   |   /\\  /__` /  \\ |  | |__) /  ` |__  \n");
        textBuilder.append("|    / \\ \\__,    |__/  |  | \\| /~~\\  |  | | \\__,    |__/ /~~\\  |  /~~\\ .__/ \\__/ \\__/ |  \\ \\__, |___ \n");

        version = version != null ? " (v" + version + ")" : "";
        int strapLineSize = (textBuilder.length() / 3) - (PXC_JWT.length() + version.length()) - 2;

        textBuilder.append(PXC_JWT);
        for (int i = 0; i < strapLineSize; i++) {
            textBuilder.append(" ");
        }
        textBuilder.append(version);
        textBuilder.append("\n");
        return textBuilder.toString();
    }
}
