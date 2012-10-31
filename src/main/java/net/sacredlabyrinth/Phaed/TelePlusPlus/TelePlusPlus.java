package net.sacredlabyrinth.Phaed.TelePlusPlus;

import net.sacredlabyrinth.Phaed.TelePlusPlus.listeners.TPEntityListener;
import net.sacredlabyrinth.Phaed.TelePlusPlus.listeners.TPPlayerListener;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.vanish.VanishPlugin;

import java.util.logging.Logger;

public class TelePlusPlus extends JavaPlugin
{
    private TPPlayerListener playerListener;
    private TPEntityListener entityListener;
    public PermissionsManager pm;
    public SettingsManager sm;
    public RequestsManager rm;
    public TeleportManager tm;
    public ToggleManager tgm;
    public GlassedManager gm;
    public CommandManager cm;
    public MoverManager mm;
    public ItemManager im;
    public VanishPlugin vanishPlugin;

    public static Logger log;

    public void onEnable()
    {
        playerListener = new TPPlayerListener(this);
        entityListener = new TPEntityListener(this);
        pm = new PermissionsManager(this);
        sm = new SettingsManager(this);
        rm = new RequestsManager(this);
        tm = new TeleportManager(this);
        cm = new CommandManager(this);
        tgm = new ToggleManager(this);
        gm = new GlassedManager(this);
        im = new ItemManager(this);
        mm = new MoverManager();

        setupVanishPlugin();
        getServer().getPluginManager().registerEvents(playerListener, this);
        getServer().getPluginManager().registerEvents(entityListener, this);

        log = Logger.getLogger("Minecraft");

        getCommand("tp").setExecutor(cm);
    }

    private void setupVanishPlugin()
    {
        Plugin plugin = getServer().getPluginManager().getPlugin("VanishNoPacket");

        if (vanishPlugin == null)
        {
            if (plugin != null)
            {
                vanishPlugin = ((VanishPlugin) plugin);
            }
        }
    }

    public void onDisable()
    {
    }

    /*
    * Fake main to allow us to run from netbeans
    */
    public static void main(String[] args)
    {
    }
}
