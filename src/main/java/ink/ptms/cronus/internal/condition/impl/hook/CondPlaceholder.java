package ink.ptms.cronus.internal.condition.impl.hook;

import com.ilummc.tlib.resources.TLocale;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.util.StringExpression;
import me.skymc.taboolib.other.NumberUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.NumberConversions;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:12
 */
@Cond(name = "placeholder", pattern = "(placeholder|papi) (((?<placeholder1>\\S+) (?<expression>.+))|(?<placeholder2>.+))", example = "placeholder [placeholder] [expression]")
public class CondPlaceholder extends Condition {

    private boolean negative;
    private boolean booleanMode;
    private String placeholder;
    private StringExpression expression;

    @Override
    public void init(Matcher matcher, String text) {
        if (booleanMode = (matcher.group("placeholder1") == null)) {
            negative = text.startsWith("!");
            placeholder = matcher.group("placeholder2");
        } else {
            expression = new StringExpression(matcher.group("expression"));
            placeholder = matcher.group("placeholder1");
        }
    }

    @Override
    public boolean check(Player player, DataQuest quest, Event event) {
        String v = TLocale.Translate.setPlaceholders(player, placeholder);
        if (booleanMode) {
            return negative == NumberUtils.getBooleanAbbreviation(v);
        } else {
            return expression.getNumber().isNumber() ? expression.isSelect(NumberConversions.toDouble(v)) : expression.isSelect(v);
        }
    }

    @Override
    public String translate() {
        if (!negative) {
            return TLocale.asString("translate-condition-placeholder0", placeholder + " " + expression.translate());
        } else {
            return TLocale.asString("translate-condition-placeholder1", placeholder + " " + expression.translate());
        }
    }

    @Override
    public String toString() {
        return "CondPlaceholder{" +
                "negative=" + negative +
                ", booleanMode=" + booleanMode +
                ", placeholder='" + placeholder + '\'' +
                ", expression=" + expression +
                '}';
    }
}
