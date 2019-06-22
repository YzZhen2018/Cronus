package ink.ptms.cronus.internal.task.item;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Block;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.PrepareAnvilEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "item_repair")
public class TaskItemRepair extends Countable<PrepareAnvilEvent> {

    private Block block;
    private ItemStack item;

    public TaskItemRepair(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        item = data.containsKey("item") ? BukkitParser.toItemStack(data.get("item")) : null;
        block = data.containsKey("block") ? BukkitParser.toBlock(data.get("block")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, PrepareAnvilEvent e) {
        return (item == null || item.isItem(e.getResult())) && (block == null || (e.getInventory().getLocation() != null && block.isSelect(e.getInventory().getLocation().getBlock())));
    }

    @Override
    public String toString() {
        return "TaskItemRepair{" +
                "count=" + count +
                ", block=" + block +
                ", item=" + item +
                ", action=" + action +
                ", config=" + config +
                ", condition=" + condition +
                '}';
    }
}
