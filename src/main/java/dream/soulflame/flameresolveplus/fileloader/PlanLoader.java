package dream.soulflame.flameresolveplus.fileloader;

import dream.soulflame.flamecore.utils.FileUtil;
import dream.soulflame.flameresolveplus.FlameResolvePlus;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Set;

public class PlanLoader {

    public static ConfigurationSection items;
    public static Set<String> itemKeys;

    private static FileUtil planFile;

    public static void load() {
        planFile = new FileUtil(FlameResolvePlus.getPlugin(), "plans");
    }

    public static void loadData() {
        items = planFile.getConfigurationSection("");
        itemKeys = items.getKeys(false);
    }

    public static void reload() {
        load();
        loadData();
    }
}
