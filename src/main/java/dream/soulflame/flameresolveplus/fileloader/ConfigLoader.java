package dream.soulflame.flameresolveplus.fileloader;

import dream.soulflame.flamecore.utils.FileUtil;
import dream.soulflame.flameresolveplus.FlameResolvePlus;

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

    public static void load() {
        configFile = new FileUtil(FlameResolvePlus.getPlugin(), "config");
    }

    public static void loadData() {
        cantPassEnable = configFile.getBoolean("Message.CantPassConditionMsg.Enable", false);
    }

    public static void reload() {
        load();
        loadData();
    }

}
