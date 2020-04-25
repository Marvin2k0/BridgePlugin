package de.marvin2k0.bridge.listener;

import de.marvin2k0.bridge.BridgePlugin;
import de.marvin2k0.bridge.game.Game;
import de.marvinleiers.gameapi.events.PlayerGameJoinEvent;
import de.marvinleiers.gameapi.events.PlayerGameQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameListener implements Listener
{
    @EventHandler
    public void onJoin(PlayerGameJoinEvent event)
    {
        Game game = (Game) event.getGame();
        game.sendMessage(BridgePlugin.getInstance().get("gamejoin").replace("%player%", event.getPlayer().getName())
         + " (" + game.getPlayers().size() + "/" + game.getMode().getSpawns() + ")");
    }

    @EventHandler
    public void onLeave(PlayerGameQuitEvent event)
    {
        Game game = (Game) event.getGame();

        game.sendMessage(BridgePlugin.getInstance().get("gameleave").replace("%player%", event.getPlayer().getName()) + " (" + game.getPlayers().size() + "/" + game.getMode().getSpawns() + ")");
        event.getPlayer().sendMessage(BridgePlugin.getInstance().get("gameleave").replace("%player%", event.getPlayer().getName()) + " (" + game.getPlayers().size() + "/" + game.getMode().getSpawns() + ")");
    }
}
