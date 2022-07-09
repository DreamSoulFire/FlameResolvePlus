package dream.soulflame.flameresolveplus.fileloader;

import dream.soulflame.flamecore.utils.FileUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class PlanLoader {

    public static ConfigurationSection items;

    private static FileUtil planFile;

    /**
     *
     * @param plugin 所需插件
     */
    public static void load(Plugin plugin) {
        planFile = new FileUtil(plugin, "plans");
    }

    public static void loadData() {
        items = planFile.getConfigurationSection("");
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
