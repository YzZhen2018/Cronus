package ink.ptms.cronus.internal.condition.impl.argument;

import com.ilummc.tlib.resources.TLocale;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.function.FunctionParser;
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
@Cond(name = "function", pattern = "func(tion)? (((?<function1>\\S+) (?<expression>.+))|(?<function2>.+))")
public class CondFunction extends Condition {

    private boolean negative;
    private boolean booleanMode;
    private String function;
    private StringExpression expression;

    @Override
    public void init(Matcher matcher, String text) {
        if (booleanMode = (matcher.group("function1") == null)) {
            negative = text.startsWith("!");
            function = matcher.group("function2");
        } else {
            expression = new StringExpression(matcher.group("expression"));
            function = matcher.group("function1");
        }
    }

    @Override
    public boolean isValid(Player player, DataQuest quest, Event event) {
        String v = FunctionParser.parseAll(new QuestProgram(player, quest), function);
        if (booleanMode) {
            return negative == NumberUtils.getBooleanAbbreviation(v);
        } else {
            return expression.getNumber().isNumber() ? expression.isSelect(NumberConversions.toDouble(v)) : expression.isSelect(v);
        }
    }
}
