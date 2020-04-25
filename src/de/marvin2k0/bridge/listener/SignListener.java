package de.marvin2k0.bridge.listener;

import de.marvin2k0.bridge.BridgePlugin;
import de.marvin2k0.bridge.game.Game;
import de.marvinleiers.gameapi.GameAPI;
import de.marvinleiers.gameapi.manage.GameManager;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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

    @EventHandler
    public void onSignClick(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.WALL_SIGN) && player.hasPermission("bridge.game"))
        {
            Sign sign = (Sign) event.getClickedBlock().getState();
            String line1 = sign.getLine(0);
            String gameName = sign.getLine(1);

            if (GameManager.getGameManager().getGameFromName(gameName) == null)
            {
                player.sendMessage(plugin.get("nogame").replace("%game%", gameName));

                return;
            }

            Game game = GameManager.getGameManager().getGameFromName(gameName);

            try
            {
                GameManager.getGameManager().joinPlayer(game, player);
            }
            catch (Exception e)
            {
                player.sendMessage("§cError: Lobby spawn not set!");
            }
        }
    }
}
