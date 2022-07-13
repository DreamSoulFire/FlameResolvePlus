package dream.soulflame.flameresolveplus.events;

import dream.soulflame.flameresolveplus.FlameResolvePlus;
import dream.soulflame.flameresolveplus.fileloader.PlayerDataLoader;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

import static dream.soulflame.flamecore.utils.CommandUtil.commands;
import static dream.soulflame.flamecore.utils.SendUtil.*;
import static dream.soulflame.flamecore.utils.SpecialUtil.*;
import static dream.soulflame.flameresolveplus.inventories.ResolveInv.*;
import static dream.soulflame.flameresolveplus.fileloader.ConfigLoader.*;
import static dream.soulflame.flameresolveplus.fileloader.ConfigLoader.cantPassEnable;
import static dream.soulflame.flameresolveplus.fileloader.GuiLoader.guiTitle;
import static dream.soulflame.flameresolveplus.fileloader.LangLoader.*;
import static dream.soulflame.flameresolveplus.fileloader.PlanLoader.*;

public class ClickInv implements Listener {

    private static final Random random = new Random();
    private static final String splitChar = "<->";

    @EventHandler
    public static void clickTrashBinEvent(InventoryClickEvent e) {
        HumanEntity whoClicked = e.getWhoClicked();
        if (!(whoClicked instanceof Player)) return;
        Player player = ((Player) whoClicked).getPlayer();
        int rawSlot = e.getRawSlot();
        Inventory topInventory = e.getView().getTopInventory();
        if (!topInventory.getTitle().equalsIgnoreCase(reColor(guiTitle))) return;
        int invSize = topInventory.getSize() - 1;
        if (rawSlot > invSize) return;
        if (rawSlot < 0) return;
        if (customSet.contains(rawSlot)) {
            e.setCancelled(true);
            return;
        }
        boolean canResolve = false;
        if (buttonSet.contains(rawSlot)) {
            e.setCancelled(true);
            for (int i = 0;i <= topInventory.getSize() - 1;i++) {
                ItemStack _item = topInventory.getItem(i);
                if (_item == null || _item.getType().equals(Material.AIR)) continue;
                ItemMeta _itemMeta = _item.getItemMeta();
                if (_itemMeta == null) continue;
                if (customSet.contains(i) || buttonSet.contains(i)) continue;
                int _amount = _item.getAmount();
                for (String keys : itemKeys) {
                    ConfigurationSection section = items.getConfigurationSection(keys);
                    String check = section.getString("Check", "");
                    double chance = section.getDouble("Chance", 0.0);
                    List<String> conditions = section.getStringList("Condition");
                    String take = section.getString("Take", "");
                    if (!check.contains(splitChar)) {
                        for (String error : configError) actions(player, error);
                        return;
                    }
                    if (!checkCondition(player, conditions, splitChar, configError)) {
                        if (cantPassEnable) actions(player, getLangFile().getStringList("Gui.CantPassConditionMsg"));
                        continue;
                    }
                    if (!takeCurrency(player, take, splitChar, FlameResolvePlus.getPlugin(), configError)) {
                        if (getConfigFile().getBoolean("Message.CantTakeMsg.Enable", false)) actions(player, getLangFile().getStringList("Gui.CantTakeMsg"));
                        continue;
                    }
                    String[] split = check.split(splitChar);
                    if (getConfigFile().getBoolean("Permission.Enable", false))
                        for (String perList : getConfigFile().getStringList("Permission.List")) {
                            if (!perList.contains(splitChar)) {
                                for (String error : configError) actions(player, error);
                                return;
                            }
                            String[] perSplit = perList.split(splitChar);
                            if (perSplit.length < 2) {
                                for (String error : configError) actions(player, error);
                                return;
                            }
                            if (!player.hasPermission(perSplit[0])) continue;
                            chance += Double.parseDouble(perSplit[1]);
                        }
                    if (split[0].equalsIgnoreCase("name")) {
                        if (!_itemMeta.hasDisplayName()) continue;
                        String _name = _itemMeta.getDisplayName();
                        if (!stripColor(_name).contains(split[1])) continue;
                        canResolve = true;
                    }
                    if (split[0].equalsIgnoreCase("lore")) {
                        if (!_itemMeta.hasLore()) continue;
                        for (String _lore : stripColor(_itemMeta.getLore())) {
                            if (!_lore.contains(split[1])) continue;
                            canResolve = true;
                        }
                    }
                    if (canResolve) {
                        for (int j = 0; j < _amount; j++) {
                            int exp = section.getInt("Exp", 0);
                            PlayerDataLoader.addExp(player, player, String.valueOf(exp));
                            double randomChance = random.nextDouble();
                            if (chance / 100 < randomChance) {
                                actions(player, getLangFile().getStringList("Gui.ResolveFailMsg"));
                                commands(player, section.getStringList("FailCommands"));
                                continue;
                            }
                            commands(player, section.getStringList("Commands"));
                        }
                        topInventory.setItem(i, new ItemStack(Material.AIR));
                        _item.setAmount(0);
                    }
                    if (getConfigFile().getBoolean("Message.ResolveMsg.Enable", false))
                        for (String resolve : getLangFile().getStringList("Gui.ResolveMsg"))
                            actions(player, resolve
                                    .replace("<item>", _itemMeta.getDisplayName())
                                    .replace("<amount>", String.valueOf(_amount)));
                }
            }
        }
    }
}
