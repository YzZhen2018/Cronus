package ink.ptms.cronus.internal.task.other;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.event.EventPeriod;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.UnEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "location")
public class TaskLocation extends UnEvent {

    private int radius;
    private Location location;

    public TaskLocation(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        radius = NumberConversions.toInt(data.getOrDefault("radius", 1));
        location = data.containsKey("location") ? BukkitParser.toLocation(data.get("location")) : null;
    }

    @Override
    public boolean isValid(Player player, DataQuest dataQuest, EventPeriod event) {
        return location != null && location.isSelectWorld(player.getLocation()) && (location.getMode() == Location.Mode.AREA ? location.inSelect(player.getLocation()) : location.toBukkit().distance(player.getLocation()) <= radius);
    }

    @Override
    public String toString() {
        return "TaskLocation{" +
                "radius=" + radius +
                ", location=" + location +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", guide=" + guide +
                ", action=" + action +
                '}';
    }
}
