package ink.ptms.cronus.internal.listener;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.event.CronusDataPushEvent;
import me.skymc.taboolib.events.PlayerLoadedEvent;
import me.skymc.taboolib.listener.TListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @Author 坏黑
 * @Since 2019-05-30 23:11
 */
@TListener
public class ListenerDatabase implements Listener {

    @EventHandler
    public void e(PlayerLoadedEvent e) {
        Cronus.getCronusService().refreshData(e.getPlayer());
    }

    @EventHandler
    public void e(PlayerQuitEvent e) {
        CronusDataPushEvent.call(e.getPlayer(), CronusAPI.getData(e.getPlayer()).push());
    }
}
