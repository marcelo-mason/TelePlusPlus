package net.sacredlabyrinth.Phaed.TelePlusPlus;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;

public class TeleHistory
{
    public static HashMap<String, LinkedList<Location>> history = new HashMap<String, LinkedList<Location>>();

    public static void pushLocation(Player player, Location location)
    {
        if (!history.containsKey(player.getName()))
        {
            history.put(player.getName(), new LinkedList<Location>());
        }
        (history.get(player.getName())).addFirst(location);
    }

    public static Location popLocation(Player player)
    {
        if (!history.containsKey(player.getName()))
        {
            return null;
        }
        if ((history.get(player.getName())).size() == 0)
        {
            return null;
        }
        return (history.get(player.getName())).removeFirst();
    }

    public static boolean clearHistory(Player player)
    {
        if (!history.containsKey(player.getName()))
        {
            return false;
        }
        if ((history.get(player.getName())).size() == 0)
        {
            return false;
        }
        (history.get(player.getName())).clear();
        return true;
    }

    public static Location origin(Player player)
    {
        if (!history.containsKey(player.getName()))
        {
            return null;
        }

        LinkedList<Location> locs = history.get(player.getName());

        Location loc = null;

        if (locs != null && locs.size() > 0)
        {
            loc = locs.removeLast();
        }
        clearHistory(player);
        return loc;
    }
}
