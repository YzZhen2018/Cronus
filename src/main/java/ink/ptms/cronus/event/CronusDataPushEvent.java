package ink.ptms.cronus.event;

import ink.ptms.cronus.database.data.DataPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CronusDataPushEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private DataPlayer dataPlayer;

    public CronusDataPushEvent(Player player, DataPlayer dataPlayer) {
        this.player = player;
        this.dataPlayer = dataPlayer;
    }

    public static CronusDataPushEvent call(Player who, DataPlayer dataPlayer) {
        CronusDataPushEvent event = new CronusDataPushEvent(who, dataPlayer);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    public DataPlayer getDataPlayer() {
        return dataPlayer;
    }

    public Player getPlayer() {
        return this.player;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
