package ink.ptms.cronus.internal.task.player.total;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.special.Uncountable;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.util.StringExpression;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_level")
public class TaskPlayerLevel extends Uncountable {

    private StringExpression level;

    public TaskPlayerLevel(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        level = data.containsKey("level") ? new StringExpression(data.get("level")) : null;
    }

    @Override
    public boolean isValid(Player player, DataQuest dataQuest, Event event) {
        return level == null || level.isSelect(getCount(player, dataQuest, event));
    }

    @Override
    public int getCount(Player player, DataQuest dataQuest, Event event) {
        return ((PlayerLevelChangeEvent) event).getNewLevel() - ((PlayerLevelChangeEvent) event).getOldLevel();
    }
}
