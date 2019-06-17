package ink.ptms.cronus.event;

import ink.ptms.cronus.service.dialog.DialogPack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CronusDialogNextEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private DialogPack pack;
    private Player player;

    private boolean cancelled;

    public CronusDialogNextEvent(DialogPack pack, Player player) {
        this.pack = pack;
        this.player = player;
    }

    public static CronusDialogNextEvent call(DialogPack pack, Player player) {
        CronusDialogNextEvent event = new CronusDialogNextEvent(pack, player);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public DialogPack getPack() {
        return pack;
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
