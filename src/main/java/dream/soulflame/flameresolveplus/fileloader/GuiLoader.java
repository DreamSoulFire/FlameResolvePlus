package dream.soulflame.flameresolveplus.fileloader;

import dream.soulflame.flamecore.utils.FileUtil;
import dream.soulflame.flameresolveplus.FlameResolvePlus;

public class GuiLoader {

    public static String guiTitle;

    private static FileUtil guiFile;

    /**
     *
     * @return guiFile 静态变量
     */
    public static FileUtil getGuiFile() {
        return guiFile;
    }

    public static void load() {
        guiFile = new FileUtil(FlameResolvePlus.getPlugin(), "gui");
    }

    public static void loadData() {
        guiTitle = guiFile.getString("Title", "");
    }

    public static void reload() {
        load();
        loadData();
    }

}
