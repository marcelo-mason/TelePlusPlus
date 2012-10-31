package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class MoverManager
{
    private HashMap<String, Integer> MovedEntity = new HashMap<String, Integer>();
    private HashMap<String, Location> MovedBlock = new HashMap<String, Location>();

    public void addMovedEntity(Player player, Entity entity)
    {
        clearMovedBlock(player);
        MovedEntity.put(player.getName(), entity.getEntityId());
    }

    public void addMovedBlock(Player player, Block block)
    {
        clearMovedEntity(player);
        MovedBlock.put(player.getName(), block.getLocation());
    }

    public void relocateMovedBlock(Player player, Block block)
    {
        if (MovedBlock.containsKey(player.getName()))
        {
            MovedBlock.put(player.getName(), block.getLocation());
        }
    }

    public boolean clearMovedBlock(Player player)
    {
        if (MovedBlock.containsKey(player.getName()))
        {
            MovedBlock.remove(player.getName());
            return true;
        }
        return false;
    }

    public boolean clearMovedEntity(Player player)
    {
        if (MovedEntity.containsKey(player.getName()))
        {
            MovedEntity.remove(player.getName());
            return true;
        }
        return false;
    }

    public Entity getMovedEntity(Player player)
    {
        if (!MovedEntity.containsKey(player.getName()))
        {
            return null;
        }

        return getEntityById(player.getWorld(), MovedEntity.get(player.getName()));
    }

    public Block getMovedBlock(Player player)
    {
        if (!MovedBlock.containsKey(player.getName()))
        {
            return null;
        }
        return player.getWorld().getBlockAt(MovedBlock.get(player.getName()));
    }

    private Entity getEntityById(World world, int id)
    {
        List<Entity> entities = world.getEntities();

        for (Entity entity : entities)
        {
            if (entity.getEntityId() == id)
            {
                return entity;
            }
        }

        return null;
    }
}
