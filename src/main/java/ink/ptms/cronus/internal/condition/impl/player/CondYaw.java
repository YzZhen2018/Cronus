package ink.ptms.cronus.internal.condition.impl.player;

import com.ilummc.tlib.resources.TLocale;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.special.CondNumber;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * @Author 坏黑
 * @Since 2019-06-17 20:21
 */
@Cond(name = "player.food", pattern = "player\\.yaw (?<expression>.+)", example = "player.yaw [expression]")
public class CondYaw extends CondNumber {

    @Override
    public Number getNumber(Player player, DataQuest quest, Event event) {
        return player.getLocation().getYaw();
    }

    @Override
    public String translate() {
        return TLocale.asString("translate-condition-yaw", expression.translate());
    }

    @Override
    public String toString() {
        return "CondYaw{" +
                "expression=" + expression +
                '}';
    }
}
