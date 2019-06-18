package ink.ptms.cronus.internal.condition.impl;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.special.CondNumber;
import me.skymc.taboolib.other.NumberUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * @Author 坏黑
 * @Since 2019-06-17 20:21
 *
 * random > 0.5
 */
@Cond(name = "random", pattern = "random (?<expression>.+)", example = "random [expression]")
public class CondRandom extends CondNumber {

    @Override
    public Number getNumber(Player player, DataQuest quest, Event event) {
        return NumberUtils.getRandom().nextDouble();
    }

    @Override
    public String toString() {
        return "CondRandom{" +
                "expression=" + expression +
                '}';
    }
}
