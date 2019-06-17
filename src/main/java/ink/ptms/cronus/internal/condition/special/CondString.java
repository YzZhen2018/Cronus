package ink.ptms.cronus.internal.condition.special;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.util.StringExpression;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:12
 */
public abstract class CondString extends Condition {

    protected String symbol;
    protected String string;

    @Override
    public void init(Matcher matcher, String text) {
        symbol = String.valueOf(matcher.group("symbol"));
        string = String.valueOf(matcher.group("string"));
    }

    @Override
    public boolean isValid(Player player, DataQuest quest, Event event) {
        switch (symbol) {
            case "=":
            case "==":
                return string.equals(String.valueOf(getString(player, quest, event)));
            case "≈":
                return string.equalsIgnoreCase(String.valueOf(getString(player, quest, event)));
            default:
                return false;
        }
    }

    abstract public String getString(Player player, DataQuest quest, Event event);

}
