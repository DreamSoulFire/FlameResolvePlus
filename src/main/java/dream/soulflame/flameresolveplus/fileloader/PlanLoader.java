package dream.soulflame.flameresolveplus.fileloader;

import dream.soulflame.flamecore.utils.FileUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class PlanLoader {

    public static Set<String> resolveItem;
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
        resolveItem = items.getKeys(false);
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
