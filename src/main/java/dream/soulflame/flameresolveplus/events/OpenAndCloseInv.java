package dream.soulflame.flameresolveplus.events;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static dream.soulflame.flamecore.utils.SendUtil.reColor;
import static dream.soulflame.flamecore.utils.SpecialUtil.actions;
import static dream.soulflame.flameresolveplus.fileloader.ConfigLoader.getConfigFile;
import static dream.soulflame.flameresolveplus.fileloader.GuiLoader.guiTitle;
import static dream.soulflame.flameresolveplus.fileloader.LangLoader.getLangFile;
import static dream.soulflame.flameresolveplus.inventories.ResolveInv.buttonSet;
import static dream.soulflame.flameresolveplus.inventories.ResolveInv.customSet;

public class OpenAndCloseInv implements Listener {

    @EventHandler
    public static void openInv(InventoryOpenEvent e) {
        HumanEntity player = e.getPlayer();
        Inventory inventory = e.getInventory();
        if (!player.getType().equals(EntityType.PLAYER)) return;
        if (!inventory.getTitle().equalsIgnoreCase(reColor(guiTitle))) return;
        if (getConfigFile().getBoolean("Message.OpenMsg.Enable", false))
            actions(player, getLangFile().getStringList("Gui.OpenMsg"));
    }

    @EventHandler
    public static void closeInv(InventoryCloseEvent e) {
        HumanEntity human = e.getPlayer();
        Inventory inventory = e.getInventory();
        boolean have = false;
        if (!human.getType().equals(EntityType.PLAYER)) return;
        Player player = (Player) human;
        if (!inventory.getTitle().equalsIgnoreCase(reColor(guiTitle))) return;
        if (getConfigFile().getBoolean("Message.CloseMsg.Enable", false))
            actions(player, getLangFile().getStringList("Gui.CloseMsg"));
        for (int i = 0;i <= inventory.getSize() - 1;i++) {
            ItemStack _item = inventory.getItem(i);
            if (_item == null || _item.getType().equals(Material.AIR)) continue;
            ItemMeta _itemMeta = _item.getItemMeta();
            if (_itemMeta == null) continue;
            if (customSet.contains(i) || buttonSet.contains(i)) continue;
            player.getInventory().addItem(_item);
            have = true;
        }
        if (!have) return;
        actions(player, getLangFile().getStringList("Gui.HaveItemMsg"));
    }
}
