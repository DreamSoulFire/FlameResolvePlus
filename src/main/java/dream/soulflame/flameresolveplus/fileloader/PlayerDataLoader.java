package dream.soulflame.flameresolveplus.fileloader;

import dream.soulflame.flamecore.FlameCore;
import dream.soulflame.flamecore.utils.SendUtil;
import dream.soulflame.flameresolveplus.FlameResolvePlus;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class PlayerDataLoader {

    private static ConfigurationSection resolverSec;
    private static Set<String> keySet;

    /**
     * 创建一个新的玩家数据
     * @param name 需要创建数据的玩家
     */
    public static void createNewData(String name) {
        for (String createMsg : LangLoader.getLangFile().getStringList("Resolver.CreateFile"))
            SendUtil.message(FlameCore.getPlugin().getConfig().getString("Prefix") + createMsg.replace("<player>", name));
        File file = new File(FlameResolvePlus.getPlugin().getDataFolder() + "/PlayerData", name + ".yml");
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        resolverSec = ConfigLoader.getConfigFile().getConfigurationSection("Resolver");
        keySet = resolverSec.getKeys(false);
        int maxExp = ConfigLoader.getConfigFile().getInt("Resolver.1.Exp");
        for (String key : keySet) {
            int level = Integer.parseInt(key);
            if (level != 0) continue;
            ConfigurationSection section = resolverSec.getConfigurationSection(key);
            int exp = section.getInt("Exp", 0);
            int buff = section.getInt("Buff", 0);
            String prefix = section.getString("Prefix", "");
            yamlConfiguration.set("Level", level);
            yamlConfiguration.set("NextLevel", level + 1);
            yamlConfiguration.set("Exp", exp);
            yamlConfiguration.set("MaxExp", maxExp);
            yamlConfiguration.set("Buff", buff);
            yamlConfiguration.set("Prefix", prefix);
        }
        try {
            yamlConfiguration.save(file);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 获取玩家配置文件
     * @param name 玩家
     * @return 玩家数据文件
     */
    public static File getPlayerFile(String name) {
        File file = new File(FlameResolvePlus.getPlugin().getDataFolder() + "/PlayerData", name + ".yml");
        if (!file.exists()) createNewData(name);
        return file;
    }

    /**
     * 获取玩家的具体数据
     * @param name 玩家
     * @return yaml配置
     */
    public static YamlConfiguration getPlayerData(String name) {
        File file = getPlayerFile(name);
        return YamlConfiguration.loadConfiguration(file);
    }

    /**
     * 获取玩家当前等级
     * @param name 玩家
     * @return 数字
     */
    public static int getLevel(String name) {
        return getPlayerData(name).getInt("Level", 0);
    }

    /**
     *
     * @param level 等级
     * @param playerData 玩家配置
     * @param name 玩家名
     */
    private static void levelChange(int level, YamlConfiguration playerData, String name) {
        int maxLevel = keySet.size() - 1;
        if (level > maxLevel) return;
        playerData.set("Level", level);
        if (level < maxLevel) playerData.set("NextLevel", level + 1);
        else playerData.set("NextLevel", level);
        playerData.set("Exp", 0);
        for (String key : keySet) {
            int keyValue = Integer.parseInt(key);
            ConfigurationSection section = resolverSec.getConfigurationSection(key);
            if (level == keyValue) {
                int buff = section.getInt("Buff", 0);
                String prefix = section.getString("Prefix", "");
                playerData.set("Buff", buff);
                playerData.set("Prefix", prefix);
                continue;
            }
            if (level + 1 != keyValue) continue;
            int maxExp = section.getInt("Exp", 0);
            playerData.set("MaxExp", maxExp);
        }
        if (level == maxLevel) playerData.set("MaxExp", 0);
        try {
            playerData.save(getPlayerFile(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 增加玩家当前等级
     * @param name 玩家
     * @param arg 需要增加的等级
     */
    public static void addLevel(String name, String arg) {
        int level = getLevel(name);
        int addLevel = Integer.parseInt(arg);
        YamlConfiguration playerData = getPlayerData(name);
        level += addLevel;
        levelChange(level, playerData, name);

    }

    /**
     * 设置玩家当前等级
     * @param name 玩家
     * @param arg 需要设置的等级
     */
    public static void setLevel(String name, String arg) {
        int level = getLevel(name);
        int newLevel = Integer.parseInt(arg);
        if (newLevel == level) {
            for (String msg : LangLoader.equal) SendUtil.message(msg);
            return;
        }
        YamlConfiguration playerData = getPlayerData(name);
        levelChange(newLevel, playerData, name);
    }

    /**
     * 减少玩家当前等级
     * @param name 玩家
     * @param arg 需要减去的等级
     */
    public static void delLevel(String name, String arg) {
        int level = getLevel(name);
        int delLevel = Integer.parseInt(arg);
        level -= delLevel;
        if (level < 0) {
            for (String low : LangLoader.lowTheZero) SendUtil.message(low);
            return;
        }
        YamlConfiguration playerData = getPlayerData(name);
        levelChange(level, playerData, name);
    }

    /**
     * 清除玩家的所有数据
     * @param name 玩家
     */
    public static void clear(String name) {
        createNewData(name);
        for (String clear : LangLoader.getLangFile().getStringList("Resolver.Clear"))
            SendUtil.message(clear.replace("<player>", name));
    }

    /**
     * 获取玩家的下一级
     * @param name 玩家
     * @return 玩家下一级
     */
    public static int getNextLevel(String name) {
        return getPlayerData(name).getInt("NextLevel", 0);
    }

    /**
     * 获取玩家当前经验值
     * @param name 玩家
     * @return 数字
     */
    public static int getExp(String name) {
        return getPlayerData(name).getInt("Exp", 0);
    }

    /**
     * 增加玩家当前经验值
     * @param name 玩家
     * @param arg 需要增加的经验
     */
    public static void addExp(String name, String arg) {
        int exp = getExp(name);
        int addExp = Integer.parseInt(arg);
        int maxExp = getMaxExp(name);
        exp += addExp;
        if (exp > maxExp) {
            for (String up : LangLoader.upTheMax) SendUtil.message(up);
            return;
        }
        YamlConfiguration playerData = getPlayerData(name);
        if (exp == maxExp) {
            addLevel(name, String.valueOf(1));
            return;
        }
        playerData.set("Exp", exp);
        try {
            playerData.save(getPlayerFile(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置玩家当前经验值
     * @param name 玩家
     * @param arg 直接设置的经验
     */
    public static void setExp(String name, String arg) {
        int exp = getExp(name);
        int maxExp = getMaxExp(name);
        int newExp = Integer.parseInt(arg);
        if (exp == newExp) {
            for (String msg : LangLoader.equal) SendUtil.message(msg);
            return;
        }
        if (newExp > maxExp) {
            for (String up : LangLoader.upTheMax) SendUtil.message(up);
            return;
        }
        YamlConfiguration playerData = getPlayerData(name);
        if (newExp == maxExp) {
            addLevel(name, String.valueOf(1));
            return;
        }
        getPlayerData(name).set("Exp", newExp);
        try {
            playerData.save(getPlayerFile(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 减少玩家当前经验值
     * @param name 玩家
     * @param arg 需要减去的经验
     */
    public static void delExp(String name, String arg) {
        int exp = getExp(name);
        int delExp = Integer.parseInt(arg);
        exp -= delExp;
        if (exp < 0) {
            for (String low : LangLoader.lowTheZero) SendUtil.message(low);
            return;
        }
        YamlConfiguration playerData = getPlayerData(name);
        getPlayerData(name).set("Exp", exp);
        try {
            playerData.save(getPlayerFile(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取玩家当前等级的最大经验值
     * @param name 玩家
     * @return 数字
     */
    public static int getMaxExp(String name) {
        return getPlayerData(name).getInt("MaxExp", 0);
    }

    /**
     * 获取玩家当前的增幅
     * @param name 玩家
     * @return 数字
     */
    public static int getBuff(String name) {
        return getPlayerData(name).getInt("Buff", 0);
    }

    /**
     * 获取玩家当前的称号
     * @param name 玩家
     * @return 字符串
     */
    public static String getPrefix(String name) {
        return getPlayerData(name).getString("Prefix", "");
    }

}
