package ink.ptms.cronus.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CronusReloadItemEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public static CronusReloadItemEvent call() {
        CronusReloadItemEvent event = new CronusReloadItemEvent();
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
