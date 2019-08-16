package ink.ptms.cronus.internal.task.other;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.event.EventPeriod;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.UnEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_val")
public class TaskPlayerVal extends UnEvent {

    private String key;
    private String value;

    public TaskPlayerVal(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        key = String.valueOf(data.getOrDefault("key", "non-name"));
        value = data.containsKey("value") ? String.valueOf(data.get("value")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, EventPeriod event) {
        YamlConfiguration data = CronusAPI.getData(player).getDataGlobal();
        return data.contains(key) && (value == null || String.valueOf(data.get(key)).equals(value));
    }

    @Override
    public String toString() {
        return "TaskPlayerVal{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
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
