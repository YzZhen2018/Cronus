package ink.ptms.cronus.internal.condition.impl.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.special.CondNumber;
import me.skymc.taboolib.cronus.CronusUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * @Author 坏黑
 * @Since 2019-06-17 20:21
 */
@Cond(name = "player.experience", pattern = "player\\.exp(erience)? (?<expression>.+)", example = "player.experience [expression]")
public class CondExperience extends CondNumber {

    @Override
    public Number getNumber(Player player, DataQuest quest, Event event) {
        return CronusUtils.getTotalExperience(player);
    }

    @Override
    public String toString() {
        return "CondExperience{" +
                "expression=" + expression +
                '}';
    }
}
