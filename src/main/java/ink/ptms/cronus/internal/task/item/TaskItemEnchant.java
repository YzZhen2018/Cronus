package ink.ptms.cronus.internal.task.item;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Block;
import ink.ptms.cronus.internal.bukkit.Enchantment;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "item_enchant")
public class TaskItemEnchant extends Countable {

    private Block block;
    private ItemStack item;
    private Enchantment enchant;

    public TaskItemEnchant(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        block = data.containsKey("block") ? BukkitParser.toBlock(data.get("block")) : null;
        item = data.containsKey("item") ? BukkitParser.toItemStack(data.get("item")) : null;
        enchant = data.containsKey("enchant") ? BukkitParser.toEnchantment(data.get("enchant")) : null;
    }

    @Override
    public boolean isValid(Player player, DataQuest dataQuest, Event event) {
        EnchantItemEvent e = ((EnchantItemEvent) event);
        return (block == null || block.isSelect(e.getEnchantBlock()))
                && (item == null || item.isItem(e.getItem()))
                && (enchant == null || e.getEnchantsToAdd().entrySet().stream().anyMatch(a -> enchant.isSelect(a.getKey(), a.getValue())));
    }

    @Override
    public String toString() {
        return "TaskItemEnchant{" +
                "count=" + count +
                ", block=" + block +
                ", item=" + item +
                ", enchant=" + enchant +
                ", action=" + action +
                ", config=" + config +
                ", condition=" + condition +
                '}';
    }
}
