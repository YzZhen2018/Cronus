package ink.ptms.cronus.internal.condition.special;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.util.StringExpression;
import ink.ptms.cronus.util.StringNumber;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:12
 */
public abstract class CondNumber extends Condition {

    protected StringExpression expression;

    @Override
    public void init(Matcher matcher, String text) {
        expression = new StringExpression(matcher.group("expression"));
    }

    @Override
    public boolean isValid(Player player, DataQuest quest, Event event) {
        return expression.isSelect(getNumber(player, quest, event).doubleValue());
    }

    abstract public Number getNumber(Player player, DataQuest quest, Event event);

}
