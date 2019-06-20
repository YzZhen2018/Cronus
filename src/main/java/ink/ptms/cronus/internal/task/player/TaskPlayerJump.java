package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import me.skymc.taboolib.events.PlayerJumpEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_jump")
public class TaskPlayerJump extends Countable<PlayerJumpEvent> {

    public TaskPlayerJump(ConfigurationSection config) {
        super(config);
    }

    @Override
    public boolean isValid(Player player, DataQuest dataQuest, PlayerJumpEvent event) {
        return true;
    }

    @Override
    public String toString() {
        return "TaskPlayerJump{" +
                "count=" + count +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", guide=" + guide +
                ", action=" + action +
                '}';
    }
}
