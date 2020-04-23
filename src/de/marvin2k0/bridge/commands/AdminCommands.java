package de.marvin2k0.bridge.commands;

import de.marvin2k0.bridge.BridgePlugin;
import de.marvinleiers.gameapi.GameAPI;
import de.marvinleiers.gameapi.game.Game;
import de.marvinleiers.gameapi.manage.GameAlreadyExistsExeption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        if (args.length != 3)
        {
            getHelp(player);

            return true;
        }

        if (args[0].equalsIgnoreCase("create"))
        {
            try
            {
                int slots = Integer.valueOf(args[2]);

                Game game = new Game(args[1], slots);
            }
            catch(GameAlreadyExistsExeption e)
            {
                player.sendMessage(plugin.get("gamealreadyexists").replace("%game%", args[1]));
            }
            catch (Exception e)
            {
                player.sendMessage(plugin.get("nonum"));
            }

            return true;
        }
        else if (args[0].equalsIgnoreCase("add"))
        {
            String game = args[1];

            if (GameAPI.getGameManager().exists(game))
            {
                GameAPI.getSpawnManager().setSpawn(game, args[2], player.getLocation());

                player.sendMessage(plugin.get("spawnset").replace("%spawn%", args[2]));
            }
            else
            {
                player.sendMessage(plugin.get("nogame").replace("%game%", game));
            }

            return true;
        }
        else if (args[0].equalsIgnoreCase("remove"))
        {
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

        return true;
    }

    private void getHelp(Player player)
    {
        player.sendMessage("§9==========§aBridge§9==========");
        player.sendMessage("§a/b create <game> <slots> §9- §aCreates a game");
        player.sendMessage("§a/b add <game> <spawn> §9- §aAdds a spawn to the game.");
        player.sendMessage("§a/b remove <game> <spawn> §9- §aRemoves the spawn from the game.");
        player.sendMessage("§a/b sign <game> §9- §aGives you a join sign for the game.");
        player.sendMessage("§9==========§aBridge§9==========");
    }
}
