package de.marvin2k0.bridge.listener;

import de.marvin2k0.bridge.BridgePlugin;
import de.marvinleiers.gameapi.GameAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignListener implements Listener
{
    BridgePlugin plugin = BridgePlugin.getInstance();

    @EventHandler
    public void onSign(SignChangeEvent event)
    {
        Player player = event.getPlayer();

        if (!player.hasPermission("bw.sign"))
            return;

        String line1 = event.getLine(0);
        String line2 = event.getLine(1);

        if (line1 != null && line1.equalsIgnoreCase("[BridgeWars]") && line2 != null && GameAPI.getGameManager().exists(line2))
        {
            event.setLine(0, plugin.get("prefix"));
        }
    }
}
