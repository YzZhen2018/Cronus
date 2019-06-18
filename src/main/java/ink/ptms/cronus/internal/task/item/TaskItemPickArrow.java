package ink.ptms.cronus.internal.task.item;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerPickupArrowEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "item_pick_arrow")
public class TaskItemPickArrow extends Countable {

    private ItemStack item;

    public TaskItemPickArrow(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        item = data.containsKey("item") ? BukkitParser.toItemStack(data.get("item")) : null;
    }

    @Override
    public boolean isValid(Player player, DataQuest dataQuest, Event event) {
        PlayerPickupArrowEvent e = ((PlayerPickupArrowEvent) event);
        return item == null || item.isItem(e.getItem().getItemStack());
    }

    @Override
    public String toString() {
        return "TaskItemPickArrow{" +
                "count=" + count +
                ", item=" + item +
                ", action=" + action +
                ", config=" + config +
                ", condition=" + condition +
                '}';
    }
}
