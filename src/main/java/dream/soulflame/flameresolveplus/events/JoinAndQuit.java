package dream.soulflame.flameresolveplus.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static dream.soulflame.flameresolveplus.fileloader.PlayerDataLoader.*;

public class JoinAndQuit implements Listener {

    @EventHandler
    public static void join(PlayerJoinEvent e) {
        getPlayerData(e.getPlayer());
    }

    @EventHandler
    public static void leave(PlayerQuitEvent e) {
        getPlayerData(e.getPlayer());
    }

}
