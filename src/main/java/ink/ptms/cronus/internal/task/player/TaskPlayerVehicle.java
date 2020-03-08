package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.Countable;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_vehicle")
public class TaskPlayerVehicle extends Countable<VehicleMoveEvent> {

    private String vehicle;

    public TaskPlayerVehicle(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        vehicle = data.containsKey("vehicle") ? String.valueOf(data.get("vehicle")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, VehicleMoveEvent e) {
        return vehicle == null || entitySelector.isSelect(e.getVehicle(), vehicle);
    }

    @Override
    public String toString() {
        return "TaskPlayerVehicle{" +
                "vehicle='" + vehicle + '\'' +
                ", count=" + count +
                ", entitySelector=" + entitySelector +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", conditionRestart=" + conditionRestart +
                ", guide=" + guide +
                ", status='" + status + '\'' +
                ", action=" + action +
                '}';
    }
}
