package dream.soulflame.flameresolveplus.inventories;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

import static dream.soulflame.flamecore.utils.ItemUtil.*;
import static dream.soulflame.flamecore.utils.SendUtil.reColor;
import static dream.soulflame.flameresolveplus.fileloader.GuiLoader.*;

public class ResolveInv {

    private static ItemStack customItem;
    private static ItemStack buttonItem;
    public static Set<Integer> customSet = new HashSet<>();
    public static Set<Integer> buttonSet = new HashSet<>();
    public static Inventory resolve;
    public static void loadItem() {
        ConfigurationSection items = getGuiFile().getConfigurationSection("Items");
        Set<String> keys = items.getKeys(false);
        customSet.clear();
        for (String key : keys) {
            if (key.equalsIgnoreCase("Button")) continue;
            ConfigurationSection section = items.getConfigurationSection(key);
            customItem = spawnItem(
                    section.getString("Material", ""),
                    section.getStringList("Lore"),
                    section.getString("Name", ""));
            splitUtil(section.getStringList("Slot"), customSet);
        }
        buttonSet.clear();
        buttonItem = spawnItem(
                getGuiFile().getString("Items.Button.Material", ""),
                getGuiFile().getStringList("Items.Button.Lore"),
                getGuiFile().getString("Items.Button.Name", ""));
        splitUtil(getGuiFile().getStringList("Items.Button.Slot"), buttonSet);
    }

    /**
     *
     * @param player 玩家
     */
    public static void openInv(Player player) {
        int size = getGuiFile().getInt("Size", 54);
        Inventory resolve = Bukkit.createInventory(player, size, reColor(guiTitle));
        ResolveInv.resolve = resolve;
        putItem(resolve, customSet, customItem);
        putItem(resolve, buttonSet, buttonItem);
        player.openInventory(resolve);
    }

}
