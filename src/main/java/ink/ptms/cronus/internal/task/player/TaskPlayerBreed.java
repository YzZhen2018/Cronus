package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.Countable;
import org.bukkit.Bukkit;
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

    private String mother;
    private String father;
    private String entity;
    private ItemStack item;

    public TaskPlayerBreed(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        mother = data.containsKey("mother") ? String.valueOf(data.get("mother")) : null;
        father = data.containsKey("father") ? String.valueOf(data.get("father")) : null;
        entity = data.containsKey("entity") ? String.valueOf(data.get("entity")) : null;
        item = data.containsKey("item") ? BukkitParser.toItemStack(data.get("item")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, EntityBreedEvent e) {
        return (item == null || item.isItem(e.getBredWith())) && (mother == null || entitySelector.isSelect(e.getMother(), mother)) && (father == null || entitySelector.isSelect(e.getFather(), father)) && (entity == null || entitySelector.isSelect(e.getEntity(), entity));
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
                ", conditionRestart=" + conditionRestart +
                ", guide=" + guide +
                ", action=" + action +
                '}';
    }
}
