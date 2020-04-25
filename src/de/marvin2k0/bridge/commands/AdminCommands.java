package de.marvin2k0.bridge.commands;

import de.marvin2k0.bridge.BridgePlugin;
import de.marvinleiers.gameapi.GameAPI;
import de.marvin2k0.bridge.game.Game;
import de.marvinleiers.gameapi.manage.GameAlreadyExistsExeption;
import de.marvinleiers.gameapi.manage.GameManager;
import de.marvinleiers.gameapi.manage.TooManySpawnsException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;

public class AdminCommands implements CommandExecutor
{
    BridgePlugin plugin = BridgePlugin.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(plugin.get("onlyplayers"));

            return true;
        }

        final Player player = (Player) sender;

        if (args.length == 0)
        {
            getHelp(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("leave"))
        {
            GameManager gameManager = GameManager.getGameManager();

            for (Game game : gameManager.getGames())
            {
                if (game.getPlayers().contains(player))
                {
                    game.leave(player);
                    return true;
                }
            }

            player.sendMessage(plugin.get("notingame"));

            return true;
        }

        if (!player.hasPermission("bridge.admin"))
        {
            player.sendMessage(plugin.get("noperm"));

            return true;
        }

        /* TODO:
           - /bridge sign <game name> - add lobby where players can choose team
           - startgame / endgame
           - game listener
           - kits
           - shop
         */

        if (args[0].equalsIgnoreCase("create"))
        {
            if (args.length != 3)
            {
                getHelp(player);

                return true;
            }

            try
            {
                Game.Mode mode = Game.Mode.getFromString(args[2]);

                if (mode == null)
                {
                    player.sendMessage(plugin.get("modes").replace("%input%", args[2]));

                    return true;
                }

                Game game = new Game(args[1], mode);
                player.sendMessage(plugin.get("gamecreated"));
            }
            catch (GameAlreadyExistsExeption e)
            {
                player.sendMessage(plugin.get("gamealreadyexists").replace("%game%", args[1]));
            }
            catch (NumberFormatException e)
            {
                player.sendMessage(plugin.get("nonum"));
            }

            return true;
        }
        else if (args[0].equalsIgnoreCase("add"))
        {
            if (args.length != 3)
            {
                getHelp(player);

                return true;
            }

            String game = args[1];

            if (GameAPI.getGameManager().exists(game))
            {
                try
                {
                    int spawnsLeft = GameAPI.getSpawnManager().setSpawn(game, args[2], player.getLocation());
                    player.sendMessage(plugin.get("spawnset").replace("%spawn%", args[2]).replace("%left%", spawnsLeft + ""));
                }
                catch (TooManySpawnsException e)
                {
                    player.sendMessage(plugin.get("nospawnsleft"));
                }
            }
            else
            {
                player.sendMessage(plugin.get("nogame").replace("%game%", game));
            }

            return true;
        }
        else if (args[0].equalsIgnoreCase("remove"))
        {
            if (args.length != 3)
            {
                getHelp(player);

                return true;
            }

            String game = args[1];

            if (GameAPI.getGameManager().exists(game))
            {
                GameAPI.getSpawnManager().removeSpawn(game, args[2]);

                player.sendMessage(plugin.get("spawnremove").replace("%spawn%", args[2]));
            }
            else
            {
                player.sendMessage(plugin.get("nogame").replace("%game%", game));
            }
        }
        else if (args[0].equalsIgnoreCase("lobby"))
        {
            if (args.length != 3)
            {
                getHelp(player);

                return true;
            }

            String game = args[1];

            if (GameAPI.getGameManager().exists(game))
            {
                Game gameobj = GameAPI.getGameManager().getGameFromName(game);
                gameobj.setLobby(player.getLocation());

                player.sendMessage(plugin.get("lobbyset") + " for " + gameobj.getName());
            }
            else
            {
                player.sendMessage(plugin.get("nogame").replace("%game%", game));
            }

            return true;
        }

        getHelp(player);
        return true;
    }

    private void getHelp(Player player)
    {
        player.sendMessage("§9==========" + plugin.get("prefix") + "§9==========");
        player.sendMessage("§a/b create <game> <mode>§9- §aCreates a game");
        player.sendMessage("§a/b add <game> <spawn> §9- §aAdds a spawn to the game.");
        player.sendMessage("§a/b remove <game> <spawn> §9- §aRemoves the spawn from the game.");
        player.sendMessage("§9==========" + plugin.get("prefix") + "§9==========");
    }
}
