package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import net.sacredlabyrinth.Phaed.TelePlusPlus.ChatBlock;
import net.sacredlabyrinth.Phaed.TelePlusPlus.Helper;
import net.sacredlabyrinth.Phaed.TelePlusPlus.Request;
import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Vector;

public class RequestsManager
{
    private TelePlusPlus plugin;
    private Vector<Request> requests = new Vector<Request>();
    private Vector<Request> purged = new Vector<Request>();
    private HashMap<String, Request> taken = new HashMap<String, Request>();

    public RequestsManager(TelePlusPlus plugin)
    {
        this.plugin = plugin;
        requestAgeCounter();
    }

    public void addRequest(Player player, String reason, int x, int y, int z)
    {
        Location loc = player.getLocation();
        loc.setX(x);
        loc.setY(y);
        loc.setZ(z);

        Request req = new Request(player.getName(), reason, loc);

        requests.add(req);
        shoutRequest(req);
    }

    public void addRequest(Player player, String reason, Player targetplayer)
    {
        Request req = new Request(player.getName(), reason, targetplayer.getName());

        requests.add(req);
        shoutRequest(req);
    }

    public Request takeRequest(CommandSender sender)
    {
        if (taken.containsKey(sender.getName()))
        {
            return taken.get(sender.getName());
        }

        if (requests.size() > 0)
        {
            Request req = requests.get(0);
            requests.remove(0);
            taken.put(sender.getName(), req);
            return req;
        }

        return null;
    }

    public void finishTakenRequest(Request req)
    {
        taken.remove(req);
    }

    public Request retrieveTakenRequest(CommandSender sender)
    {
        if (taken.containsKey(sender.getName()))
        {
            Request req = taken.get(sender.getName());
            taken.remove(req);
            return req;
        }
        return null;
    }

    public void flush()
    {
        for (Request req : purged)
        {
            requests.remove(req);
        }
        purged.clear();
    }

    public int requestAgeCounter()
    {
        return plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
        {
            public void run()
            {
                for (Request req : requests)
                {
                    req.incrementMinutes();

                    if (req.getMinutes() == plugin.sm.purgeRequestMinutes)
                    {
                        purged.add(req);
                    }
                }
                flush();
            }
        }, 60 * 20L);
    }

    public boolean existRequestTakers()
    {
        Player[] online = plugin.getServer().getOnlinePlayers();

        for (Player player : online)
        {
            if (plugin.pm.hasPermission(player, plugin.pm.take) && !plugin.sm.disableRequest)
            {
                return true;
            }
        }
        return false;
    }

    public void shoutRequest(Request req)
    {
        Player[] online = plugin.getServer().getOnlinePlayers();

        for (Player player : online)
        {
            if (plugin.pm.hasPermission(player, plugin.pm.take) && !plugin.sm.disableRequest)
            {
                ChatBlock.sendMessage(player, ChatColor.DARK_PURPLE + "[tp] " + ChatColor.WHITE + "[" + req.getPlayerName() + "] " + ChatColor.YELLOW + "requests tp to " + ChatColor.WHITE + (req.getLocation() != null ? Helper.formatLocation(req.getLocation()) : "[" + req.getTargetName() + "]"));
                ChatBlock.sendMessage(player, ChatColor.DARK_PURPLE + "[tp] " + ChatColor.YELLOW + "Reason: " + req.getReason());
                ChatBlock.sendMessage(player, ChatColor.DARK_PURPLE + "[tp] " + ChatColor.YELLOW + "/tp take");
            }
        }
    }
}
