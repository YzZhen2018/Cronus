package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Entity;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.vehicle.VehicleEnterEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_vehicle_enter")
public class TaskPlayerVehicleEnter extends Countable<VehicleEnterEvent> {

    private Entity vehicle;

    public TaskPlayerVehicleEnter(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        vehicle = data.containsKey("vehicle") ? BukkitParser.toEntity(data.get("vehicle")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, VehicleEnterEvent event) {
        return vehicle == null || vehicle.isSelect(event.getVehicle());
    }

    @Override
    public String toString() {
        return "TaskPlayerVehicleEnter{" +
                "vehicle=" + vehicle +
                ", count=" + count +
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
