package ink.ptms.cronus.internal.condition.impl.argument;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.Condition;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:12
 */
@Cond(name = "tag", pattern = "tag\\.(?<tag>.+)", example = "tag.[content]")
public class CondTag extends Condition {

    private boolean negative;
    private String tag;

    @Override
    public void init(Matcher matcher, String text) {
        negative = text.startsWith("!");
        tag = String.valueOf(matcher.group("tag"));
    }

    @Override
    public boolean check(Player player, DataQuest quest, Event event) {
        return negative == player.getScoreboardTags().contains(tag);
    }

    @Override
    public String toString() {
        return "CondTag{" +
                "negative=" + negative +
                ", tag='" + tag + '\'' +
                '}';
    }
}
