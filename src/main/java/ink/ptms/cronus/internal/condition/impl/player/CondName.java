package ink.ptms.cronus.internal.condition.impl.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.special.CondNumber;
import ink.ptms.cronus.internal.condition.special.CondString;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * @Author 坏黑
 * @Since 2019-06-17 20:21
 */
@Cond(name = "player.name", pattern = "player\\.level (?<symbol>\\S+) (?<string>.+)")
public class CondName extends CondString {

    @Override
    public String getString(Player player, DataQuest quest, Event event) {
        return player.getName();
    }
}
