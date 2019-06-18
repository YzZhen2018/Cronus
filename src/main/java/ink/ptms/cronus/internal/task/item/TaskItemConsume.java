package ink.ptms.cronus.internal.task.item;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "item_consume")
public class TaskItemConsume extends Countable {

    private ItemStack item;

    public TaskItemConsume(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        item = data.containsKey("item") ? BukkitParser.toItemStack(data.get("item")) : null;
    }

    @Override
    public boolean isValid(Player player, DataQuest dataQuest, Event event) {
        PlayerItemConsumeEvent e = ((PlayerItemConsumeEvent) event);
        return item == null || item.isItem(e.getItem());
    }

    @Override
    public String toString() {
        return "TaskItemConsume{" +
                "count=" + count +
                ", item=" + item +
                ", action=" + action +
                ", config=" + config +
                ", condition=" + condition +
                '}';
    }
}
