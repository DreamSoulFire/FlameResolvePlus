package dream.soulflame.flameresolveplus.fileloader;

import dream.soulflame.flameresolveplus.FlameResolvePlus;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static dream.soulflame.flamecore.utils.SpecialUtil.actions;
import static dream.soulflame.flameresolveplus.fileloader.LangLoader.*;

public class PlayerDataLoader {

    private static ConfigurationSection resolverSec;
    private static Set<String> keySet;

    /**
     * 创建一个新的玩家数据
     * @param sender 需要创建数据的玩家
     */
    public static void createNewData(CommandSender sender) {
        for (String createMsg : LangLoader.getLangFile().getStringList("Resolver.CreateFile"))
            actions(sender, createMsg.replace("<player>", sender.getName()));
        File file = new File(FlameResolvePlus.getPlugin().getDataFolder() + "/PlayerData", sender.getName() + ".yml");
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
     * @param sender 玩家
     * @return 玩家数据文件
     */
    public static File getPlayerFile(CommandSender sender) {
        File file = new File(FlameResolvePlus.getPlugin().getDataFolder() + "/PlayerData", sender + ".yml");
        if (!file.exists()) createNewData(sender);
        return file;
    }

    /**
     * 获取玩家的具体数据
     * @param sender 玩家
     * @return yaml配置
     */
    public static YamlConfiguration getPlayerData(CommandSender sender) {
        File file = getPlayerFile(sender);
        return YamlConfiguration.loadConfiguration(file);
    }

    /**
     * 获取玩家当前等级
     * @param sender 玩家
     * @return 数字
     */
    public static int getLevel(CommandSender sender) {
        return getPlayerData(sender).getInt("Level", 0);
    }

    /**
     *
     * @param level 等级
     * @param playerData 玩家配置
     * @param sender 玩家名
     */
    private static void levelChange(int level, YamlConfiguration playerData, CommandSender sender) {
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
            playerData.save(getPlayerFile(sender));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 增加玩家当前等级
     * @param sender 被发送信息的玩家
     * @param target 被增加等级的玩家
     * @param arg 需要增加的等级
     */
    public static void addLevel(CommandSender sender, CommandSender target, String arg) {
        int level = getLevel(target);
        int addLevel = Integer.parseInt(arg);
        YamlConfiguration playerData = getPlayerData(target);
        level += addLevel;
        levelChange(level, playerData, target);
        for (String msg : LangLoader.add)
            actions(sender, msg.replace("<value>", arg));
    }

    /**
     * 设置玩家当前等级
     * @param sender 被发送信息的玩家
     * @param target 被设置等级的玩家
     * @param arg 需要设置的等级
     */
    public static void setLevel(CommandSender sender, CommandSender target, String arg) {
        int level = getLevel(target);
        int newLevel = Integer.parseInt(arg);
        if (newLevel == level) {
            actions(sender, equal);
            return;
        }
        YamlConfiguration playerData = getPlayerData(target);
        levelChange(newLevel, playerData, target);
        for (String msg : LangLoader.set)
            actions(sender, msg.replace("<value>", arg));
    }

    /**
     * 减少玩家当前等级
     * @param sender 被发送信息的玩家
     * @param target 被减少等级的玩家
     * @param arg 需要减去的等级
     */
    public static void delLevel(CommandSender sender, CommandSender target, String arg) {
        int level = getLevel(target);
        int delLevel = Integer.parseInt(arg);
        level -= delLevel;
        if (level < 0) {
            actions(sender, lowTheZero);
            return;
        }
        YamlConfiguration playerData = getPlayerData(target);
        levelChange(level, playerData, target);
        for (String msg : LangLoader.del)
            actions(sender, msg.replace("<value>", arg));
    }

    /**
     * 清除玩家的所有数据
     * @param sender 玩家
     */
    public static void clear(CommandSender sender) {
        createNewData(sender);
        for (String clear : LangLoader.getLangFile().getStringList("Resolver.Clear"))
            actions(sender, clear.replace("<sender>", sender.getName()));
    }

    /**
     * 获取玩家的下一级
     * @param sender 玩家
     * @return 玩家下一级
     */
    public static int getNextLevel(CommandSender sender) {
        return getPlayerData(sender).getInt("NextLevel", 0);
    }

    /**
     * 获取玩家当前经验值
     * @param sender 玩家
     * @return 数字
     */
    public static int getExp(CommandSender sender) {
        return getPlayerData(sender).getInt("Exp", 0);
    }

    /**
     * 增加玩家当前经验值
     * @param sender 被发送信息的玩家
     * @param target 被增加经验的玩家
     * @param arg 需要增加的经验
     */
    public static void addExp(CommandSender sender, CommandSender target, String arg) {
        int exp = getExp(target);
        int addExp = Integer.parseInt(arg);
        int maxExp = getMaxExp(target);
        exp += addExp;
        if (exp > maxExp) {
            actions(sender, upTheMax);
            return;
        }
        YamlConfiguration playerData = getPlayerData(target);
        if (exp == maxExp) {
            addLevel(sender, target, String.valueOf(1));
            return;
        }
        playerData.set("Exp", exp);
        try {
            playerData.save(getPlayerFile(target));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String msg : LangLoader.add)
            actions(sender, msg.replace("<value>", arg));
    }

    /**
     * 设置玩家当前经验值
     * @param sender 被发送信息的玩家
     * @param target 被设置经验的玩家
     * @param arg 直接设置的经验
     */
    public static void setExp(CommandSender sender, CommandSender target, String arg) {
        int exp = getExp(target);
        int maxExp = getMaxExp(target);
        int newExp = Integer.parseInt(arg);
        if (exp == newExp) {
            actions(sender, equal);
            return;
        }
        if (newExp > maxExp) {
            actions(sender, upTheMax);
            return;
        }
        YamlConfiguration playerData = getPlayerData(target);
        if (newExp == maxExp) {
            addLevel(sender, target, String.valueOf(1));
            return;
        }
        getPlayerData(target).set("Exp", newExp);
        try {
            playerData.save(getPlayerFile(target));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String msg : LangLoader.set)
            actions(sender, msg.replace("<value>", arg));
    }

    /**
     * 减少玩家当前经验值
     * @param sender 被发送信息的玩家
     * @param target 被减少经验的玩家
     * @param arg 需要减去的经验
     */
    public static void delExp(CommandSender sender, CommandSender target, String arg) {
        int exp = getExp(target);
        int delExp = Integer.parseInt(arg);
        exp -= delExp;
        if (exp < 0) {
            actions(sender, lowTheZero);
            return;
        }
        YamlConfiguration playerData = getPlayerData(target);
        getPlayerData(target).set("Exp", exp);
        try {
            playerData.save(getPlayerFile(target));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String msg : LangLoader.del)
            actions(sender, msg.replace("<value>", arg));
    }

    /**
     * 获取玩家当前等级的最大经验值
     * @param sender 玩家
     * @return 数字
     */
    public static int getMaxExp(CommandSender sender) {
        return getPlayerData(sender).getInt("MaxExp", 0);
    }

    /**
     * 获取玩家当前的增幅
     * @param sender 玩家
     * @return 数字
     */
    public static int getBuff(CommandSender sender) {
        return getPlayerData(sender).getInt("Buff", 0);
    }

    /**
     * 获取玩家当前的称号
     * @param sender 玩家
     * @return 字符串
     */
    public static String getPrefix(CommandSender sender) {
        return getPlayerData(sender).getString("Prefix", "");
    }

}
