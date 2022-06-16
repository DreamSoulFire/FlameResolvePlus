package dream.soulflame.flameresolveplus.utils;

import dream.soulflame.flamecore.utils.SendUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static dream.soulflame.flameresolveplus.fileloader.PlayerDataLoader.*;

public class PapiHookUtil extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "frp";
    }

    @Override
    public @NotNull String getAuthor() {
        return "SoulFlame";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        String name = offlinePlayer.getName();
        String fileName = name + ".yml";
        if (!fileName.equalsIgnoreCase(getPlayerFile(name).getName()))
            return fileName;
        if (params.equalsIgnoreCase("prefix"))
            return SendUtil.reColor(getPrefix(name));
        if (params.equalsIgnoreCase("level"))
            return String.valueOf(getLevel(name));
        if (params.equalsIgnoreCase("nextlevel"))
            return String.valueOf(getNextLevel(name));
        if (params.equalsIgnoreCase("exp"))
            return String.valueOf(getExp(name));
        if (params.equalsIgnoreCase("maxexp"))
            return String.valueOf(getMaxExp(name));
        if (params.equalsIgnoreCase("buff"))
            return String.valueOf(getBuff(name));
        return null;
    }
}
