package ink.ptms.cronus.internal.task.player.total;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.special.Uncountable;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.util.StringExpression;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_exp")
public class TaskPlayerExp extends Uncountable {

    private StringExpression xp;

    public TaskPlayerExp(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        xp = data.containsKey("xp") ? new StringExpression(data.get("xp")) : null;
    }

    @Override
    public boolean isValid(Player player, DataQuest dataQuest, Event event) {
        return xp == null || xp.isSelect(getCount(player, dataQuest, event));
    }

    @Override
    public int getCount(Player player, DataQuest dataQuest, Event event) {
        return ((PlayerExpChangeEvent) event).getAmount();
    }
}
