package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class GlassedManager
{
    private TelePlusPlus plugin;
    private HashMap<String, Vector> glassed = new HashMap<String, Vector>();
    private HashMap<String, Integer> fallDamageImmune = new HashMap<String, Integer>();

    public GlassedManager(TelePlusPlus plugin)
    {
        this.plugin = plugin;
    }

    public boolean isGlassed(Player player)
    {
        return glassed.containsKey(player.getName());
    }

    public boolean isGlassedBlock(Player player, Block block)
    {
        if (!glassed.containsKey(player.getName()))
        {
            return false;
        }

        if (glassed.get(player.getName()).equals(block.getLocation().toVector()))
        {
            return true;
        }

        return false;
    }

    public boolean addGlassed(Player player, Block block)
    {
        plugin.gm.removeGlassedNotImmunity(player);

        if (!block.getType().equals(Material.AIR))
        {
            if (plugin.sm.fallImmunity)
            {
                fallDamageImmune.put(player.getName(), startImmuneRemovalDelay(player));
            }
            return false;
        }

        if (plugin.sm.clientSideGlass)
        {
            player.sendBlockChange(block.getLocation(), Material.GLASS, (byte) 0);
        }
        else
        {
            block.setType(Material.GLASS);
        }

        glassed.put(player.getName(), block.getLocation().toVector());

        if (plugin.sm.fallImmunity)
        {
            if (fallDamageImmune.containsKey(player.getName()))
            {
                int current = fallDamageImmune.get(player.getName());
                plugin.getServer().getScheduler().cancelTask(current);
            }

            fallDamageImmune.put(player.getName(), startImmuneRemovalDelay(player));
        }
        return true;
    }

    public void removeGlassed(Player player)
    {
        if (glassed.containsKey(player.getName()))
        {
            removeGlassedNotImmunity(player);

            if (plugin.sm.fallImmunity)
            {
                fallDamageImmune.put(player.getName(), startImmuneRemovalDelay(player));
            }
        }
    }

    public void removeGlassedNotImmunity(Player player)
    {
        if (glassed.containsKey(player.getName()))
        {
            Vector vec = glassed.get(player.getName());
            Block block = player.getWorld().getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
            if (plugin.sm.clientSideGlass)
            {
                player.sendBlockChange(block.getLocation(), Material.AIR, (byte) 0);
            }
            else
            {
                if (block.getType().equals(Material.GLASS))
                {
                    block.setType(Material.AIR);
                }
            }
            glassed.remove(player.getName());
        }
    }

    public boolean isFallDamageImmune(Player player)
    {
        return fallDamageImmune.containsKey(player.getName());
    }

    public int startImmuneRemovalDelay(Player player)
    {
        final String name = player.getName();

        return plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable()
        {
            public void run()
            {
                fallDamageImmune.remove(name);
            }
        }, plugin.sm.fallImmunitySeconds * 20L);
    }
}
