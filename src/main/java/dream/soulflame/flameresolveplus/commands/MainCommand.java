package dream.soulflame.flameresolveplus.commands;

import dream.soulflame.flamecore.utils.SendUtil;
import dream.soulflame.flameresolveplus.FlameResolvePlus;
import dream.soulflame.flameresolveplus.fileloader.*;
import dream.soulflame.flameresolveplus.inventories.ResolveInv;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.List;

import static dream.soulflame.flamecore.utils.SendUtil.*;
import static dream.soulflame.flamecore.utils.SpecialUtil.actions;
import static dream.soulflame.flameresolveplus.fileloader.LangLoader.*;
import static dream.soulflame.flameresolveplus.fileloader.PlayerDataLoader.*;
import static org.bukkit.Bukkit.getConsoleSender;

public class MainCommand implements TabExecutor {
    private void reloadAll() {
        String prefixMsg = "&cFlame&eResolve&bPlus";
        String splitLine = "&b====================================";
        FlameResolvePlus plugin = FlameResolvePlus.getPlugin();
        long preTime = System.currentTimeMillis();
        String[] pre = {
                splitLine,
                prefixMsg + " &7-> &6插件&e重载&a开始",
                ""
        };
        for (String preReload : pre) getConsoleSender().sendMessage(reColor(preReload));
        ConfigLoader.reload(plugin);
        GuiLoader.reload(plugin);
        LangLoader.reload(plugin);
        PlanLoader.reload(plugin);
        ResolveInv.loadItem();
        long finishTime = System.currentTimeMillis();
        long time = finishTime - preTime;
        String[] finish = {"&6插件&e重载&3完成 &9耗时&f: &a" + time + " &dms", splitLine};
        for (String finishReload : finish) getConsoleSender().sendMessage(reColor(finishReload));
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
                    if (player.hasPermission("flameresolveplus.command.clear"))
                        PlayerDataLoader.clear(args[1]);
                    else actions(sender, noPermission);
                    return true;
                } else PlayerDataLoader.clear(args[1]);
                return true;
            }

            //info指令
            if ("info".equalsIgnoreCase(args[0])) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("flameresolveplus.command.info")) {
                        String prefix = getPrefix(args[1]);
                        int level = getLevel(args[1]);
                        int nextLevel = getNextLevel(args[1]);
                        int exp = getExp(args[1]);
                        int maxExp = getMaxExp(args[1]);
                        int buff = getBuff(args[1]);
                        for (String msg : LangLoader.getLangFile().getStringList("Resolver.Info"))
                            player.sendMessage(msg
                                    .replace("<player>", args[1])
                                    .replace("<prefix>", prefix)
                                    .replace("<level>", String.valueOf(level))
                                    .replace("<nextlevel>", String.valueOf(nextLevel))
                                    .replace("<exp>", String.valueOf(exp))
                                    .replace("<maxexp>", String.valueOf(maxExp))
                                    .replace("<buff>", String.valueOf(buff)));
                    }
                    else actions(sender, noPermission);
                    return true;
                } else {
                    String prefix = getPrefix(args[1]);
                    int level = getLevel(args[1]);
                    int nextLevel = getNextLevel(args[1]);
                    int exp = getExp(args[1]);
                    int maxExp = getMaxExp(args[1]);
                    int buff = getBuff(args[1]);
                    for (String msg : LangLoader.getLangFile().getStringList("Resolver.Info"))
                        SendUtil.message(msg
                                .replace("<player>", args[1])
                                .replace("<prefix>", prefix)
                                .replace("<level>", String.valueOf(level))
                                .replace("<nextlevel>", String.valueOf(nextLevel))
                                .replace("<exp>", String.valueOf(exp))
                                .replace("<maxexp>", String.valueOf(maxExp))
                                .replace("<buff>", String.valueOf(buff))
                        );
                }
                return true;
            }

            if (args.length == 3) {
                //level指令
                if ("level".equalsIgnoreCase(args[0])) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        String name = player.getName();
                        if (player.hasPermission("flameresolveplus.command.level")) {
                            if ("add".equalsIgnoreCase(args[1])) {
                                PlayerDataLoader.addLevel(name, args[2]);
                                for (String msg : LangLoader.add)
                                    SendUtil.message(player, reName(player, msg).replace("<value>", args[2]), 0);
                            } else if ("set".equalsIgnoreCase(args[1])) {
                                PlayerDataLoader.setLevel(name, args[2]);
                                for (String msg : LangLoader.set)
                                    SendUtil.message(player, reName(player, msg).replace("<value>", args[2]), 0);
                            } else if ("del".equalsIgnoreCase(args[1])) {
                                PlayerDataLoader.delLevel(name, args[2]);
                                for (String msg : LangLoader.del)
                                    SendUtil.message(player, reName(player, msg).replace("<value>", args[2]), 0);
                            } else actions(sender, argsNoEnough);
                        }
                        else actions(sender, noPermission);
                        return true;
                    } else {
                        String name = sender.getName();
                        if ("add".equalsIgnoreCase(args[1])) {
                            PlayerDataLoader.addLevel(name, args[2]);
                            for (String msg : LangLoader.add)
                                SendUtil.message(msg.replace("<player>", name).replace("<value>", args[2]));
                        } else if ("set".equalsIgnoreCase(args[1])) {
                            PlayerDataLoader.setLevel(name, args[2]);
                            for (String msg : LangLoader.set)
                                SendUtil.message(msg.replace("<player>", name).replace("<value>", args[2]));
                        } else if ("del".equalsIgnoreCase(args[1])) {
                            PlayerDataLoader.delLevel(name, args[2]);
                            for (String msg : LangLoader.del)
                                SendUtil.message(msg.replace("<player>", name).replace("<value>", args[2]));
                        } else actions(sender, argsNoEnough);
                    }
                    return true;
                }

                //exp指令
                if ("exp".equalsIgnoreCase(args[0])) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        String name = player.getName();
                        if (player.hasPermission("flameresolveplus.command.exp")) {
                            if ("add".equalsIgnoreCase(args[1])) {
                                PlayerDataLoader.addExp(name, args[2]);
                                for (String msg : LangLoader.add)
                                    SendUtil.message(player, reName(player, msg).replace("<value>", args[2]), 0);
                            } else if ("set".equalsIgnoreCase(args[1])) {
                                PlayerDataLoader.setExp(name, args[2]);
                                for (String msg : LangLoader.set)
                                    SendUtil.message(player, reName(player, msg).replace("<value>", args[2]), 0);
                            } else if ("del".equalsIgnoreCase(args[1])) {
                                PlayerDataLoader.delExp(name, args[2]);
                                for (String msg : LangLoader.del)
                                    SendUtil.message(player, reName(player, msg).replace("<value>", args[2]), 0);
                            } else actions(sender, argsNoEnough);
                        }
                        else actions(sender, noPermission);
                        return true;
                    } else {
                        String name = sender.getName();
                        if ("add".equalsIgnoreCase(args[1])) {
                            PlayerDataLoader.addExp(name, args[2]);
                            for (String msg : LangLoader.add)
                                SendUtil.message(msg.replace("<player>", name).replace("<value>", args[2]));
                        } else if ("set".equalsIgnoreCase(args[1])) {
                            PlayerDataLoader.setExp(name, args[2]);
                            for (String msg : LangLoader.set)
                                SendUtil.message(msg.replace("<player>", name).replace("<value>", args[2]));
                        } else if ("del".equalsIgnoreCase(args[1])) {
                            PlayerDataLoader.delExp(name, args[2]);
                            for (String msg : LangLoader.del)
                                SendUtil.message(msg.replace("<player>", name).replace("<value>", args[2]));
                        } else actions(sender, argsNoEnough);
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
                            if ("add".equalsIgnoreCase(args[1])) {
                                PlayerDataLoader.addLevel(args[3], args[2]);
                                for (String msg : LangLoader.add)
                                    SendUtil.message(player, reName(player, msg).replace("<value>", args[2]), 0);
                            } else if ("set".equalsIgnoreCase(args[1])) {
                                PlayerDataLoader.setLevel(args[3], args[2]);
                                for (String msg : LangLoader.set)
                                    SendUtil.message(player, reName(player, msg).replace("<value>", args[2]), 0);
                            } else if ("del".equalsIgnoreCase(args[1])) {
                                PlayerDataLoader.delLevel(args[3], args[2]);
                                for (String msg : LangLoader.del)
                                    SendUtil.message(player, reName(player, msg).replace("<value>", args[2]), 0);
                            } else actions(sender, argsNoEnough);
                        }
                        else actions(sender, noPermission);
                        return true;
                    } else {
                        if ("add".equalsIgnoreCase(args[1])) {
                            PlayerDataLoader.addLevel(args[3], args[2]);
                            for (String msg : LangLoader.add)
                                SendUtil.message(msg.replace("<player>", args[3]).replace("<value>", args[2]));
                        } else if ("set".equalsIgnoreCase(args[1])) {
                            PlayerDataLoader.setLevel(args[3], args[2]);
                            for (String msg : LangLoader.set)
                                SendUtil.message(msg.replace("<player>", args[3]).replace("<value>", args[2]));
                        } else if ("del".equalsIgnoreCase(args[1])) {
                            PlayerDataLoader.delLevel(args[3], args[2]);
                            for (String msg : LangLoader.del)
                                SendUtil.message(msg.replace("<player>", args[3]).replace("<value>", args[2]));
                        } else actions(sender, argsNoEnough);
                    }
                    return true;
                }

                //exp指令
                if ("exp".equalsIgnoreCase(args[0])) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (player.hasPermission("flameresolveplus.command.exp")) {
                            if ("add".equalsIgnoreCase(args[1])) {
                                PlayerDataLoader.addExp(args[3], args[2]);
                                for (String msg : LangLoader.add)
                                    SendUtil.message(player, reName(player, msg).replace("<value>", args[2]), 0);
                            } else if ("set".equalsIgnoreCase(args[1])) {
                                PlayerDataLoader.setExp(args[3], args[2]);
                                for (String msg : LangLoader.set)
                                    SendUtil.message(player, reName(player, msg).replace("<value>", args[2]), 0);
                            } else if ("del".equalsIgnoreCase(args[1])) {
                                PlayerDataLoader.delExp(args[3], args[2]);
                                for (String msg : LangLoader.del)
                                    SendUtil.message(player, reName(player, msg).replace("<value>", args[2]), 0);
                            } else actions(sender, argsNoEnough);
                        }
                        else actions(sender, noPermission);
                        return true;
                    } else {
                        if ("add".equalsIgnoreCase(args[1])) {
                            PlayerDataLoader.addExp(args[3], args[2]);
                            for (String msg : LangLoader.add)
                                SendUtil.message(msg.replace("<player>", args[3]).replace("<value>", args[2]));
                        } else if ("set".equalsIgnoreCase(args[1])) {
                            PlayerDataLoader.setExp(args[3], args[2]);
                            for (String msg : LangLoader.set)
                                SendUtil.message(msg.replace("<player>", args[3]).replace("<value>", args[2]));
                        } else if ("del".equalsIgnoreCase(args[1])) {
                            PlayerDataLoader.delExp(args[3], args[2]);
                            for (String msg : LangLoader.del)
                                SendUtil.message(msg.replace("<player>", args[3]).replace("<value>", args[2]));
                        } else actions(sender, argsNoEnough);
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
