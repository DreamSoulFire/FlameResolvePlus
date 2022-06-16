package dream.soulflame.flameresolveplus.fileloader;

import dream.soulflame.flamecore.utils.FileUtil;
import org.bukkit.plugin.Plugin;

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

    /**
     *
     * @param plugin 所需插件
     */
    public static void load(Plugin plugin) {
        guiFile = new FileUtil(plugin, "gui");
    }

    public static void loadData() {
        guiTitle = guiFile.getString("Title", "");
    }

    /**
     *
     * @param plugin 所需插件
     */
    public static void reload(Plugin plugin) {
        load(plugin);
        loadData();
    }

}
