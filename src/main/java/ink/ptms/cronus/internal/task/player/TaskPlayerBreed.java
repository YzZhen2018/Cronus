package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Entity;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.util.NumberConversions;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_breed")
public class TaskPlayerBreed extends Countable<EntityBreedEvent> {

    private int count;
    private Entity mother;
    private Entity father;
    private Entity entity;
    private ItemStack item;

    public TaskPlayerBreed(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        count = NumberConversions.toInt(data.getOrDefault("count", 1));
        mother = data.containsKey("mother") ? BukkitParser.toEntity(data.get("mother")) : null;
        father = data.containsKey("father") ? BukkitParser.toEntity(data.get("father")) : null;
        entity = data.containsKey("entity") ? BukkitParser.toEntity(data.get("entity")) : null;
        item = data.containsKey("item") ? BukkitParser.toItemStack(data.get("item")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, EntityBreedEvent e) {
        return (item == null || item.isItem(e.getBredWith())) && (mother == null || mother.isSelect(e.getMother())) && (father == null || father.isSelect(e.getFather())) && (entity == null || entity.isSelect(e.getEntity()));
    }

    @Override
    public String toString() {
        return "TaskPlayerBreed{" +
                "count=" + count +
                ", mother=" + mother +
                ", father=" + father +
                ", entity=" + entity +
                ", item=" + item +
                ", count=" + count +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", guide=" + guide +
                ", action=" + action +
                '}';
    }
}
