package ink.ptms.cronus.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CronusReloadDialogEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public static CronusReloadDialogEvent call() {
        CronusReloadDialogEvent event = new CronusReloadDialogEvent();
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
