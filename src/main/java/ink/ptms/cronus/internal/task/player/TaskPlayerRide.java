package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Entity;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_ride")
public class TaskPlayerRide extends Countable<PlayerMoveEvent> {

    private Entity entity;

    public TaskPlayerRide(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        entity = data.containsKey("entity") ? BukkitParser.toEntity(data.get("entity")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, PlayerMoveEvent event) {
        return player.getVehicle() instanceof LivingEntity && (entity == null || entity.isSelect(player.getVehicle()));
    }

    @Override
    public String toString() {
        return "TaskPlayerRide{" +
                "count=" + count +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", guide=" + guide +
                ", action=" + action +
                '}';
    }
}
