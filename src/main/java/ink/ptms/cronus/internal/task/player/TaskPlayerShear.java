package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Entity;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.util.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerShearEntityEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_shear")
public class TaskPlayerShear extends Countable {

    private Entity entity;
    private ItemStack item;

    public TaskPlayerShear(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        item = data.containsKey("item") ? BukkitParser.toItemStack(data.get("item")) : null;
        entity = data.containsKey("entity") ? BukkitParser.toEntity(data.get("entity")) : null;
    }

    @Override
    public boolean isValid(Player player, DataQuest dataQuest, Event event) {
        PlayerShearEntityEvent e = ((PlayerShearEntityEvent) event);
        return (item == null || item.isItem(Utils.NonNull(Utils.getUsingItem(e.getPlayer(), Material.SHEARS)))) && (entity == null || entity.isSelect(e.getEntity()));
    }

    @Override
    public String toString() {
        return "TaskPlayerShear{" +
                "count=" + count +
                ", entity=" + entity +
                ", item=" + item +
                ", action=" + action +
                ", config=" + config +
                ", condition=" + condition +
                ", guide=" + guide +
                '}';
    }
}
