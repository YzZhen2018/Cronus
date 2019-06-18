package ink.ptms.cronus.internal.condition;

import ink.ptms.cronus.database.data.DataQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:10
 */
public abstract class Condition {

    abstract public void init(Matcher matcher, String text);

    abstract public boolean isValid(Player player, DataQuest quest, Event event);
}
