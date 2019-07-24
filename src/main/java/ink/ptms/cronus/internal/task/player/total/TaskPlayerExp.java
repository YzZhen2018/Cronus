package ink.ptms.cronus.internal.task.player.total;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.task.special.Uncountable;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.util.StringExpression;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_exp")
public class TaskPlayerExp extends Uncountable<PlayerExpChangeEvent> {

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
    public boolean check(Player player, DataQuest dataQuest, PlayerExpChangeEvent e) {
        return xp == null || xp.isSelect(getCount(player, dataQuest, e));
    }

    @Override
    public double getCount(Player player, DataQuest dataQuest, PlayerExpChangeEvent e) {
        return e.getAmount();
    }

    @Override
    public String toString() {
        return "TaskPlayerExp{" +
                "xp=" + xp +
                ", total=" + total +
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
