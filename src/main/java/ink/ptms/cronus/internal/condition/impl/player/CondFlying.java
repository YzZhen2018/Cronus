package ink.ptms.cronus.internal.condition.impl.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.special.CondBoolean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * @Author 坏黑
 * @Since 2019-06-17 20:21
 */
@Cond(name = "player.flying", pattern = "player\\.fly(ing)?", example = "player.flying")
public class CondFlying extends CondBoolean {

    @Override
    public boolean getBoolean(Player player, DataQuest quest, Event event) {
        return player.isFlying();
    }

    @Override
    public String toString() {
        return "CondFlying{" +
                "negative=" + negative +
                '}';
    }
}
