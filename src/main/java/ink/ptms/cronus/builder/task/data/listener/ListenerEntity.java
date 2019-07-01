package ink.ptms.cronus.builder.task.data.listener;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.builder.task.data.Entity;
import ink.ptms.cronus.service.selector.EntitySelector;
import me.skymc.taboolib.listener.TListener;
import me.skymc.taboolib.message.ChatCatcher;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.LinkedList;

/**
 * @Author 坏黑
 * @Since 2019-07-01 13:41
 */
@TListener
public class ListenerEntity implements Listener {

    @EventHandler
    public void e(PlayerInteractAtEntityEvent e) {
        if (e.getHand() == EquipmentSlot.HAND && e.getPlayer().getItemInHand().getType() == Material.NETHER_STAR) {
            LinkedList<ChatCatcher.Catcher> catchers = ChatCatcher.getPlayerdata().get(e.getPlayer().getName());
            for (ChatCatcher.Catcher catcher : catchers) {
                if (catcher instanceof Entity.EntitySelect) {
                    e.setCancelled(true);
                    e.getPlayer().chat(Cronus.getCronusService().getService(EntitySelector.class).fromEntity(e.getRightClicked()));
                }
            }
        }
    }
}
