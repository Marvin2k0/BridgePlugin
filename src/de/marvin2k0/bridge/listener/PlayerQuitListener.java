package de.marvin2k0.bridge.listener;

import de.marvin2k0.bridge.game.Game;
import de.marvinleiers.gameapi.manage.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener
{
    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        GameManager gameManager = GameManager.getGameManager();

        for (Game game : gameManager.getGames())
        {
            if (game.getPlayers().contains(event.getPlayer()))
                game.leave(event.getPlayer());
        }
    }
}
