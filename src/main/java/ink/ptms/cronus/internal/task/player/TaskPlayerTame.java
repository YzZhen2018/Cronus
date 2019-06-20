package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Entity;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTameEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_tame")
public class TaskPlayerTame extends Countable<EntityTameEvent> {

    private Entity entity;

    public TaskPlayerTame(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        entity = data.containsKey("entity") ? BukkitParser.toEntity(data.get("entity")) : null;
    }

    @Override
    public boolean isValid(Player player, DataQuest dataQuest, EntityTameEvent e) {
        return (entity == null || entity.isSelect(e.getEntity()));
    }

    @Override
    public String toString() {
        return "TaskPlayerTame{" +
                "count=" + count +
                ", entity=" + entity +
                ", action=" + action +
                ", config=" + config +
                ", condition=" + condition +
                ", guide=" + guide +
                '}';
    }
}
