package ink.ptms.cronus.internal.program.effect;

import com.ilummc.tlib.logger.TLogger;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.FunctionParser;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;
import me.skymc.taboolib.common.inject.TInject;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 15:24
 */
@Auto
public class EffectPlayer extends Effect {

    @TInject
    private static TLogger logger;
    private String action;
    private String symbol;
    private String value;

    @Override
    public String pattern() {
        return "player\\.(?<action>\\S+) (?<symbol>\\S+)[ ]?(?<value>.+)";
    }

    @Override
    public void match(Matcher matcher) {
        action = matcher.group("action").toLowerCase();
        symbol = matcher.group("symbol").toLowerCase();
        value = matcher.group("value");
    }

    @Override
    public void eval(Program program) {
        if (program instanceof QuestProgram) {
            String parsed = FunctionParser.parseAll(program, value);
            switch (action) {
                case "tag":
                    switch (symbol) {
                        case "+":
                            ((QuestProgram) program).getPlayer().addScoreboardTag(parsed);
                            break;
                        case "-":
                            ((QuestProgram) program).getPlayer().removeScoreboardTag(parsed);
                            break;
                        default:
                            logger.warn("Invalid Symbol: " + symbol + " " + value);
                            break;
                    }
                    break;
                default:
                    logger.warn("Invalid Action: " + action + " " + symbol + " " + value);
                    break;
            }
        }
    }

    @Override
    public String toString() {
        return "EffectPlayer{" +
                "action='" + action + '\'' +
                ", symbol='" + symbol + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
