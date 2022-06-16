package dream.soulflame.flameresolveplus.fileloader;

import dream.soulflame.flamecore.utils.FileUtil;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class LangLoader {

    public static List<String> noPermission;
    public static List<String> reloadMsg;
    public static List<String> cantConsoleMsg;
    public static List<String> argsNoEnough;
    public static List<String> configError;
    public static List<String> add;
    public static List<String> set;
    public static List<String> del;
    public static List<String> upTheMax;
    public static List<String> equal;
    public static List<String> lowTheZero;

    private static FileUtil langFile;

    /**
     *
     * @return langFile 静态变量
     */
    public static FileUtil getLangFile() {
        return langFile;
    }

    /**
     *
     * @param plugin 所需插件
     */
    public static void load(Plugin plugin) {
        langFile = new FileUtil(plugin, "lang");
    }

    public static void loadData() {
        noPermission = langFile.getStringList("Command.NoPermission");
        reloadMsg = langFile.getStringList("Command.Reload");
        cantConsoleMsg = langFile.getStringList("Command.CantConsole");
        argsNoEnough = langFile.getStringList("Command.ArgsNoEnough");

        configError = langFile.getStringList("Config.Error");

        add = langFile.getStringList("Resolver.Add");
        set = langFile.getStringList("Resolver.Set");
        del = langFile.getStringList("Resolver.Del");
        upTheMax = langFile.getStringList("Resolver.UpTheMax");
        equal = langFile.getStringList("Resolver.Equal");
        lowTheZero = langFile.getStringList("Resolver.LowTheZero");
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
