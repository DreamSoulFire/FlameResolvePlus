package dream.soulflame.flameresolveplus;

import dream.soulflame.flameresolveplus.commands.MainCommand;
import dream.soulflame.flameresolveplus.events.ClickInv;
import dream.soulflame.flameresolveplus.events.JoinAndQuit;
import dream.soulflame.flameresolveplus.events.OpenAndCloseInv;
import dream.soulflame.flameresolveplus.fileloader.ConfigLoader;
import dream.soulflame.flameresolveplus.fileloader.GuiLoader;
import dream.soulflame.flameresolveplus.fileloader.LangLoader;
import dream.soulflame.flameresolveplus.fileloader.PlanLoader;
import dream.soulflame.flameresolveplus.inventories.ResolveInv;
import dream.soulflame.flameresolveplus.utils.PapiHookUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.*;

public final class FlameResolvePlus extends JavaPlugin {

    private static FlameResolvePlus plugin;

    public static FlameResolvePlus getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {

        plugin = this;

        //加载配置文件
        ConfigLoader.reload(plugin);
        GuiLoader.reload(plugin);
        LangLoader.reload(plugin);
        PlanLoader.reload(plugin);

        //加载papi变量
        PapiHookUtil papiHookUtil = new PapiHookUtil();
        papiHookUtil.register();

        //加载界面物品
        ResolveInv.loadItem();

        //注册事件
        getPluginManager().registerEvents(new ClickInv(), this);
        getPluginManager().registerEvents(new OpenAndCloseInv(), this);
        getPluginManager().registerEvents(new JoinAndQuit(), this);

        //注册指令
        getPluginCommand("flameresolveplus").setExecutor(new MainCommand());
        getPluginCommand("flameresolveplus").setTabCompleter(new MainCommand());
    }

    @Override
    public void onDisable() {
        for (Player player : getOnlinePlayers()) player.closeInventory();
    }
}
