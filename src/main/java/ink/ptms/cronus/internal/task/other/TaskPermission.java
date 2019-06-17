package ink.ptms.cronus.internal.task.other;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "permission")
public class TaskPermission extends QuestTask {

    private String permission;

    public TaskPermission(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        permission = String.valueOf(data.getOrDefault("permission", "*"));
    }

    @Override
    public boolean isCompleted(DataQuest dataQuest) {
        return dataQuest.getDataStage().getInt(getId() + ".has") > 0;
    }

    @Override
    public boolean isValid(Player player, DataQuest dataQuest, Event event) {
        return player.hasPermission(permission);
    }

    @Override
    public void next(Player player, DataQuest dataQuest, Event event) {
        dataQuest.getDataStage().set(getId() + ".has", 1);
    }

    @Override
    public void complete(DataQuest dataQuest) {
        dataQuest.getDataStage().set(getId() + ".has", 1);
    }

    @Override
    public void reset(DataQuest dataQuest) {
        dataQuest.getDataStage().set(getId() + ".has", 0);
    }

    @Override
    public String toString() {
        return "TaskPermission{" +
                "permission='" + permission + '\'' +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", guide=" + guide +
                ", action=" + action +
                '}';
    }
}
