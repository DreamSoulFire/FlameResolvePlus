package dream.soulflame.flameresolveplus.fileloader;

import dream.soulflame.flamecore.utils.FileUtil;
import org.bukkit.plugin.Plugin;

public class ConfigLoader {
    public static boolean cantPassEnable;

    private static FileUtil configFile;

    /**
     *
     * @return configFile 静态变量
     */
    public static FileUtil getConfigFile() {
        return configFile;
    }

    /**
     *
     * @param plugin 所需插件
     */
    public static void load(Plugin plugin) {
        configFile = new FileUtil(plugin, "config");
    }

    public static void loadData() {
        cantPassEnable = configFile.getBoolean("Message.CantPassConditionMsg.Enable", false);
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
