package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.asm.AsmHandler;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.util.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_trade")
public class TaskPlayerTrade extends Countable {

    private ItemStack item;
    private ItemStack item1;
    private ItemStack item2;
    private ItemStack result;

    public TaskPlayerTrade(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        item = data.containsKey("item") ? BukkitParser.toItemStack(data.get("item")) : null;
        item1 = data.containsKey("item1") ? BukkitParser.toItemStack(data.get("item1")) : null;
        item2 = data.containsKey("item2") ? BukkitParser.toItemStack(data.get("item2")) : null;
        result = data.containsKey("result") ? BukkitParser.toItemStack(data.get("result")) : null;
    }

    @Override
    public boolean isValid(Player player, DataQuest dataQuest, Event event) {
        org.bukkit.inventory.ItemStack[] recipe = AsmHandler.getImpl().getRecipe(((InventoryClickEvent) event).getInventory());
        return recipe != null && (item == null || item.isItem(Utils.NonNull(recipe[0])) || item.isItem(Utils.NonNull(recipe[1]))) && (item1 == null || item1.isItem(Utils.NonNull(recipe[0]))) && (item2 == null || item2.isItem(Utils.NonNull(recipe[1]))) && (result == null || result.isItem(Utils.NonNull(recipe[2])));
    }

    @Override
    public String toString() {
        return "TaskPlayerTrade{" +
                "count=" + count +
                ", item=" + item +
                ", item1=" + item1 +
                ", item2=" + item2 +
                ", result=" + result +
                ", action=" + action +
                ", config=" + config +
                ", condition=" + condition +
                ", guide=" + guide +
                '}';
    }
}
