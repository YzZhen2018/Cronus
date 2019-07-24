package ink.ptms.cronus.internal.task.player.total;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.internal.task.special.Uncountable;
import ink.ptms.cronus.util.StringExpression;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "item_damage")
public class TaskItemDamage extends Uncountable<PlayerItemDamageEvent> {

    private StringExpression damage;

    public TaskItemDamage(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        damage = data.containsKey("damage") ? new StringExpression(data.get("damage")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, PlayerItemDamageEvent e) {
        return damage == null || damage.isSelect(getCount(player, dataQuest, e));
    }

    @Override
    public double getCount(Player player, DataQuest dataQuest, PlayerItemDamageEvent e) {
        return e.getDamage();
    }
}
