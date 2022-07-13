package dream.soulflame.flameresolveplus.commands;

import dream.soulflame.flamecore.utils.SendUtil;
import dream.soulflame.flameresolveplus.fileloader.*;
import dream.soulflame.flameresolveplus.inventories.ResolveInv;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.List;

import static dream.soulflame.flamecore.FlameCore.getPlugin;
import static dream.soulflame.flamecore.utils.SendUtil.message;
import static dream.soulflame.flamecore.utils.SpecialUtil.actions;
import static dream.soulflame.flameresolveplus.fileloader.LangLoader.*;

public class MainCommand implements TabExecutor {
    private void reloadAll() {
        String prefixMsg = "&cFlame&eResolve&bPlus";
        String splitLine = "&b====================================";
        long preTime = System.currentTimeMillis();
        String[] pre = {
                splitLine,
                prefixMsg + " &7-> &6插件&e重载&a开始",
                ""
        };
        SendUtil.message(pre);
        ConfigLoader.reload();
        GuiLoader.reload();
        LangLoader.reload();
        PlanLoader.reload();
        ResolveInv.loadItem();
        long finishTime = System.currentTimeMillis();
        long time = finishTime - preTime;
        String[] finish = {"&6插件&e重载&3完成 &9耗时&f: &a" + time + " &dms", splitLine};
        SendUtil.message(finish);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //help指令
        if (args.length == 0 || "help".equalsIgnoreCase(args[0])) {
            if (sender.hasPermission("flameresolveplus.command.help"))
                message(sender, getLangFile().getStringList("Command.Help"));
            else actions(sender, noPermission);
            return true;
        }

        //reload指令
        if ("reload".equalsIgnoreCase(args[0])) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("flameresolveplus.command.reload")) {
                    actions(player, reloadMsg);
                    reloadAll();
                } else actions(sender, noPermission);
                return true;
            } else {
                actions(sender, reloadMsg);
                reloadAll();
            }
            return true;
        }

        //open指令
        if ("open".equalsIgnoreCase(args[0])) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("flameresolveplus.open.resolve")) ResolveInv.openInv(player);
                else actions(sender, noPermission);
            } else for (String cant : cantConsoleMsg) actions(sender, cant);
        }

        //只输入 check, level, exp, clear 指令
        if (args.length == 1)
            if ("check".equalsIgnoreCase(args[0]) ||
                    "level".equalsIgnoreCase(args[0]) ||
                    "exp".equalsIgnoreCase(args[0]) ||
                    "clear".equalsIgnoreCase(args[0]) ||
                    "info".equalsIgnoreCase(args[0])) {
                actions(sender, argsNoEnough);
                return true;
            }

        if (args.length > 1) {

            //check指令
            if ("check".equalsIgnoreCase(args[0])) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    ItemStack handItem = player.getInventory().getItemInMainHand();
                    MaterialData materialData = handItem.getData();
                    Material itemType = materialData.getItemType();
                    if (itemType == Material.AIR) actions(player, getLangFile().getStringList("CheckItem.NoItem"));
                    if (player.hasPermission("flameresolveplus.command.check")) {
                        if ("material".equalsIgnoreCase(args[1])) for (String materialMsg : getLangFile().getStringList("CheckItem.Material"))
                            actions(player, materialMsg.replace("<material>", itemType.name()) + ":" + materialData.getData());
                        else if ("id".equalsIgnoreCase(args[1])) for (String idMsg : getLangFile().getStringList("CheckItem.Id"))
                            actions(player, idMsg.replace("<id>", String.valueOf(itemType.getId())) + ":" + materialData.getData());
                        else actions(sender, argsNoEnough);
                    }
                    else actions(sender, noPermission);
                    return true;
                } else actions(sender, cantConsoleMsg);
                return true;
            }

            //clear指令
            if ("clear".equalsIgnoreCase(args[0])) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("flameresolveplus.command.clear")) {
                        boolean online = false;
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            String name = onlinePlayer.getName();
                            if (!args[1].equalsIgnoreCase(name)) continue;
                            Player target = Bukkit.getPlayer(args[1]);
                            PlayerDataLoader.clear(target);
                            online = true;
                        }
                        if (!online) actions(player, getPlugin().getConfig().getStringList("Command.PlayerOffline"));
                    } else actions(player, noPermission);
                } else {
                    boolean online = false;
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        String name = onlinePlayer.getName();
                        if (!args[1].equalsIgnoreCase(name)) continue;
                        Player target = Bukkit.getPlayer(args[1]);
                        PlayerDataLoader.clear(target);
                        online = true;
                    }
                    if (!online) actions(sender, getPlugin().getConfig().getStringList("Command.PlayerOffline"));
                }
                return true;
            }

            //info指令
            if ("info".equalsIgnoreCase(args[0])) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("flameresolveplus.command.info")) {
                        boolean online = false;
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            String name = onlinePlayer.getName();
                            if (!args[1].equalsIgnoreCase(name)) continue;
                            Player target = Bukkit.getPlayer(args[1]);
                            for (String msg : LangLoader.getLangFile().getStringList("Resolver.Info")) {
                                msg = PlaceholderAPI.setPlaceholders(target, msg);
                                actions(player, msg);
                            }
                            online = true;
                        }
                        if (!online) actions(player, getPlugin().getConfig().getStringList("Command.PlayerOffline"));
                    } else actions(player, noPermission);
                } else {
                    boolean online = false;
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        String name = onlinePlayer.getName();
                        if (!args[1].equalsIgnoreCase(name)) continue;
                        Player target = Bukkit.getPlayer(args[1]);
                        for (String msg : LangLoader.getLangFile().getStringList("Resolver.Info")) {
                            msg = PlaceholderAPI.setPlaceholders(target, msg);
                            actions(sender, msg);
                        }
                        online = true;
                    }
                    if (!online) actions(sender, getPlugin().getConfig().getStringList("Command.PlayerOffline"));
                }
                return true;
            }

            if (args.length == 3) {
                //level指令
                if ("level".equalsIgnoreCase(args[0])) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (player.hasPermission("flameresolveplus.command.level")) {
                            boolean online = false;
                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                String name = onlinePlayer.getName();
                                if (!args[1].equalsIgnoreCase(name)) continue;
                                if ("add".equalsIgnoreCase(args[1]))
                                    PlayerDataLoader.addLevel(player, player, args[2]);
                                else if ("set".equalsIgnoreCase(args[1]))
                                    PlayerDataLoader.setLevel(player, player, args[2]);
                                else if ("del".equalsIgnoreCase(args[1]))
                                    PlayerDataLoader.delLevel(player, player, args[2]);
                                else actions(player, argsNoEnough);
                                online = true;
                            }
                            if (!online) actions(player, getPlugin().getConfig().getStringList("Command.PlayerOffline"));
                        }
                        else actions(player, noPermission);
                        return true;
                    } else {
                        boolean online = false;
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            String name = onlinePlayer.getName();
                            if (!args[1].equalsIgnoreCase(name)) continue;
                            if ("add".equalsIgnoreCase(args[1]))
                                PlayerDataLoader.addLevel(sender, sender, args[2]);
                            else if ("set".equalsIgnoreCase(args[1]))
                                PlayerDataLoader.setLevel(sender, sender, args[2]);
                            else if ("del".equalsIgnoreCase(args[1]))
                                PlayerDataLoader.delLevel(sender, sender, args[2]);
                            else actions(sender, argsNoEnough);
                            online = true;
                        }
                        if (!online) actions(sender, getPlugin().getConfig().getStringList("Command.PlayerOffline"));
                    }
                    return true;
                }

                //exp指令
                if ("exp".equalsIgnoreCase(args[0])) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (player.hasPermission("flameresolveplus.command.exp")) {
                            boolean online = false;
                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                String name = onlinePlayer.getName();
                                if (!args[1].equalsIgnoreCase(name)) continue;
                                if ("add".equalsIgnoreCase(args[1]))
                                    PlayerDataLoader.addExp(player, player, args[2]);
                                else if ("set".equalsIgnoreCase(args[1]))
                                    PlayerDataLoader.setExp(player, player, args[2]);
                                else if ("del".equalsIgnoreCase(args[1])) {
                                    PlayerDataLoader.delExp(player, player, args[2]);
                                } else actions(player, argsNoEnough);
                                online = true;
                            }
                            if (!online) actions(player, getPlugin().getConfig().getStringList("Command.PlayerOffline"));
                        }
                        else actions(player, noPermission);
                        return true;
                    } else {
                        boolean online = false;
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            String name = onlinePlayer.getName();
                            if (!args[1].equalsIgnoreCase(name)) continue;
                            if ("add".equalsIgnoreCase(args[1]))
                                PlayerDataLoader.addExp(sender, sender, args[2]);
                            else if ("set".equalsIgnoreCase(args[1]))
                                PlayerDataLoader.setExp(sender, sender, args[2]);
                            else if ("del".equalsIgnoreCase(args[1])) {
                                PlayerDataLoader.delExp(sender, sender, args[2]);
                            } else actions(sender, argsNoEnough);
                            online = true;
                        }
                        if (!online) actions(sender, getPlugin().getConfig().getStringList("Command.PlayerOffline"));
                    }
                    return true;
                }
            }

            if (args.length == 4) {
                //level指令
                if ("level".equalsIgnoreCase(args[0])) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (player.hasPermission("flameresolveplus.command.level")) {
                            boolean online = false;
                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                String name = onlinePlayer.getName();
                                if (!args[1].equalsIgnoreCase(name)) continue;
                                Player target = Bukkit.getPlayer(args[3]);
                                if ("add".equalsIgnoreCase(args[1]))
                                    PlayerDataLoader.addLevel(player, target, args[2]);
                                else if ("set".equalsIgnoreCase(args[1]))
                                    PlayerDataLoader.setLevel(player, target, args[2]);
                                else if ("del".equalsIgnoreCase(args[1])) {
                                    PlayerDataLoader.delLevel(player, target, args[2]);
                                } else actions(player, argsNoEnough);
                                online = true;
                            }
                            if (!online) actions(player, getPlugin().getConfig().getStringList("Command.PlayerOffline"));
                        }
                        else actions(player, noPermission);
                        return true;
                    } else {
                        boolean online = false;
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            String name = onlinePlayer.getName();
                            if (!args[1].equalsIgnoreCase(name)) continue;
                            Player target = Bukkit.getPlayer(args[3]);
                            if ("add".equalsIgnoreCase(args[1]))
                                PlayerDataLoader.addLevel(sender, target, args[2]);
                            else if ("set".equalsIgnoreCase(args[1]))
                                PlayerDataLoader.setLevel(sender, target, args[2]);
                            else if ("del".equalsIgnoreCase(args[1])) {
                                PlayerDataLoader.delLevel(sender, target, args[2]);
                            } else actions(sender, argsNoEnough);
                            online = true;
                        }
                        if (!online) actions(sender, getPlugin().getConfig().getStringList("Command.PlayerOffline"));
                    }
                    return true;
                }

                //exp指令
                if ("exp".equalsIgnoreCase(args[0])) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (player.hasPermission("flameresolveplus.command.exp")) {
                            boolean online = false;
                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                String name = onlinePlayer.getName();
                                if (!args[1].equalsIgnoreCase(name)) continue;
                                Player target = Bukkit.getPlayer(args[3]);
                                if ("add".equalsIgnoreCase(args[1]))
                                    PlayerDataLoader.addExp(player, target, args[2]);
                                else if ("set".equalsIgnoreCase(args[1]))
                                    PlayerDataLoader.setExp(player, target, args[2]);
                                else if ("del".equalsIgnoreCase(args[1])) {
                                    PlayerDataLoader.delExp(player, target, args[2]);
                                } else actions(player, argsNoEnough);
                                online = true;
                            }
                            if (!online) actions(player, getPlugin().getConfig().getStringList("Command.PlayerOffline"));
                        }
                        else actions(sender, noPermission);
                        return true;
                    } else {
                        boolean online = false;
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            String name = onlinePlayer.getName();
                            if (!args[1].equalsIgnoreCase(name)) continue;
                            Player target = Bukkit.getPlayer(args[3]);
                            if ("add".equalsIgnoreCase(args[1]))
                                PlayerDataLoader.addExp(sender, target, args[2]);
                            else if ("set".equalsIgnoreCase(args[1]))
                                PlayerDataLoader.setExp(sender, target, args[2]);
                            else if ("del".equalsIgnoreCase(args[1])) {
                                PlayerDataLoader.delExp(sender, target, args[2]);
                            } else actions(sender, argsNoEnough);
                            online = true;
                        }
                        if (!online) actions(sender, getPlugin().getConfig().getStringList("Command.PlayerOffline"));
                    }
                    return true;
                }
            }

        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return TabList.returnList(args, args.length);
    }
}
