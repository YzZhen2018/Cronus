package ink.ptms.cronus.event;

import ink.ptms.cronus.service.dialog.DialogPack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CronusDialogEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private DialogPack pack;
    private Entity target;
    private Player player;

    private boolean cancelled;

    public CronusDialogEvent(DialogPack pack, Entity target, Player player) {
        this.pack = pack;
        this.target = target;
        this.player = player;
    }

    public static CronusDialogEvent call(DialogPack pack, Entity target, Player player) {
        CronusDialogEvent event = new CronusDialogEvent(pack, target, player);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public DialogPack getPack() {
        return pack;
    }

    public Entity getTarget() {
        return target;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPack(DialogPack pack) {
        this.pack = pack;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
