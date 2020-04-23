package de.marvin2k0.bridge;

import de.marvin2k0.bridge.commands.AdminCommands;
import de.marvinleiers.gameapi.GameAPI;
import de.marvinleiers.gameapi.logger.LogLevel;
import de.marvinleiers.gameapi.logger.Logger;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


public class BridgePlugin extends JavaPlugin
{
    private static BridgePlugin instance;

    private Logger logger;
    private GameAPI api;

    @Override
    public void onEnable()
    {
        instance = this;

        logger = GameAPI.getLog();

        setUpConfig();

        this.getCommand("bridge").setExecutor(new AdminCommands());

        api = new GameAPI(this);
    }

    private void setUpConfig()
    {
        getConfig().options().copyDefaults(true);
        getConfig().addDefault("onlyplayers", "&cThis command is only for players!");
        getConfig().addDefault("noperm", "&cYou don't have permission to do that!");
        getConfig().addDefault("nonum", "&cPlease only enter numbers for the slots.");
        getConfig().addDefault("gamealreadyexists", "&cError: %game% already exists!");
        getConfig().addDefault("nogame", "&cError: %game% does not exist.");
        getConfig().addDefault("spawnset", "&aSpawn %spawn% set.");
        getConfig().addDefault("spawnremove", "&aSpawn %spawn% has been removed.");

        saveConfig();
    }

    public String get(String path)
    {
        String msg = null;

        try
        {
            msg = getConfig().getString(path);
        }
        catch (NullPointerException e)
        {
            logger.log(LogLevel.ERROR, "Message " + path + " not found!");
        }

        return msg.contains("&") ? ChatColor.translateAlternateColorCodes('&', msg) : msg;
    }

    public static BridgePlugin getInstance()
    {
        return instance;
    }
}
