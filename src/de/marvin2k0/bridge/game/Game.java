package de.marvin2k0.bridge.game;

import de.marvin2k0.bridge.BridgePlugin;
import de.marvinleiers.gameapi.GameAPI;
import de.marvinleiers.gameapi.game.Arena;
import de.marvinleiers.gameapi.manage.GameAlreadyExistsExeption;
import de.marvinleiers.gameapi.manage.SpawnManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Game extends Arena
{
    private static SpawnManager spawnManager = GameAPI.getSpawnManager();

    private ArrayList<Location> spawns = new ArrayList<>();
    private FileConfiguration config = GameAPI.getGameManager().getConfig();

    private Mode mode;
    private int slots;
    private boolean isRunning;
    private boolean canJoin;

    public Game(String name, Mode mode) throws GameAlreadyExistsExeption
    {
        super(name);

        this.mode = mode;
        this.slots = mode.getSpawns();
        this.isRunning = false;
        this.canJoin = false;

        GameAPI.getGameManager().addGame(this);
    }

    public Game(String name, Mode mode, boolean copy)
    {
        super(name);

        this.mode = mode;
        this.slots = mode.getSpawns();
        this.isRunning = false;
        this.canJoin = false;

         // TODO
        for (String loc : GameAPI.getGameManager().getConfig().getStringList(name + ".spawns"))
        {
            System.out.println(loc);
        }
    }

    public void setLobby(Location location)
    {
        String world = location.getWorld().getName();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        float yaw = location.getYaw();
        float pitch = location.getPitch();

        config.set(getName() + ".lobby.world", world);
        config.set(getName() + ".lobby.x", x);
        config.set(getName() + ".lobby.y", y);
        config.set(getName() + ".lobby.z", z);
        config.set(getName() + ".lobby.yaw", yaw);
        config.set(getName() + ".lobby.pitch", pitch);

        GameAPI.getGameManager().saveConfig();
    }

    public Mode getMode()
    {
        return this.mode;
    }

    public void setMode(Mode mode)
    {
        this.mode = mode;
    }

    public void addSpawn(Location loc)
    {
        if(!spawns.contains(loc))
            spawns.add(loc);
    }

    @Override
    public void onJoin(Player player)
    {
        System.out.println(getPlayers());

        if (!(isRunning))
        {
            isRunning = true;
            canJoin = players.size() <= mode.getSpawns();
        }

        if (canJoin)
        {

            player.teleport(getLobby());
        }
        else
        {
            player.sendMessage("§cError: couldn't join game. " + canJoin + " " + players.size() + " : " + mode.getSpawns());
        }
    }

    public Location getLobby()
    {
        Location location;
        World world = Bukkit.getWorld(config.getString(getName() + ".lobby.world"));

        double x = config.getDouble(getName() + ".lobby.x");
        double y = config.getDouble(getName() + ".lobby.y");
        double z = config.getDouble(getName() + ".lobby.z");

        double yaw = config.getDouble(getName() + ".lobby.yaw");
        double pitch = config.getDouble(getName() + ".lobby.pitch");

        location = new Location(world, x, y, z, (float) yaw, (float) pitch);

        return location;
    }

    @Override
    public void onLeave(Player player)
    {
        System.out.println(getPlayers());
    }

    public boolean isRunning()
    {
        return isRunning;
    }

    public void setRunning(boolean flag)
    {
        this.isRunning = flag;
    }

    public enum Mode
    {
        ONE_ON_ONE("1v1", 2),
        TWO_ON_TWO("2v2", 4),
        FOUR_x_ONE("4x1", 4);

        private String name;
        private int spawns;

        Mode(String name, int spawns)
        {
            this.name = name;
            this.spawns = spawns;
        }

        public String getName()
        {
            return name;
        }

        public int getSpawns()
        {
            return spawns;
        }

        public static Mode getFromString(String str)
        {
            if (str.equalsIgnoreCase("1v1"))
                return Mode.ONE_ON_ONE;
            if (str.equalsIgnoreCase("2v2"))
                return Mode.TWO_ON_TWO;
            if (str.equalsIgnoreCase("4x1"))
                return Mode.FOUR_x_ONE;

            return null;
        }
    }
}
