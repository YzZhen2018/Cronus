package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Entity;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_throw")
public class TaskPlayerThrow extends Countable {

    private Entity projectile;

    public TaskPlayerThrow(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        projectile = data.containsKey("projectile") ? BukkitParser.toEntity(data.get("projectile")) : null;
    }

    @Override
    public boolean isValid(Player player, DataQuest dataQuest, Event event) {
        ProjectileLaunchEvent e = ((ProjectileLaunchEvent) event);
        return (projectile == null || projectile.isSelect(e.getEntity()));
    }

    @Override
    public String toString() {
        return "TaskPlayerThrow{" +
                "projectile=" + projectile +
                ", count=" + count +
                ", action=" + action +
                ", config=" + config +
                ", condition=" + condition +
                ", guide=" + guide +
                '}';
    }
}
