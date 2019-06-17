package ink.ptms.cronus.internal.task.other;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.NumberConversions;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "location")
public class TaskLocation extends QuestTask {

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
    public boolean isCompleted(DataQuest dataQuest) {
        return dataQuest.getDataStage().getInt(getId() + ".find") > 0;
    }

    @Override
    public boolean isValid(Player player, DataQuest dataQuest, Event event) {
        return location != null && location.isSelectWorld(player.getLocation()) && (location.getMode() == Location.Mode.AREA ? location.inSelect(player.getLocation()) : location.toBukkit().distance(player.getLocation()) <= radius);
    }

    @Override
    public void next(Player player, DataQuest dataQuest, Event event) {
        dataQuest.getDataStage().set(getId() + ".find", 1);
    }

    @Override
    public void complete(DataQuest dataQuest) {
        dataQuest.getDataStage().set(getId() + ".find", 1);
    }

    @Override
    public void reset(DataQuest dataQuest) {
        dataQuest.getDataStage().set(getId() + ".find", 0);
    }

    @Override
    public String toString() {
        return "TaskLocationFind{" +
                "location=" + location +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", guide=" + guide +
                ", action=" + action +
                '}';
    }
}
