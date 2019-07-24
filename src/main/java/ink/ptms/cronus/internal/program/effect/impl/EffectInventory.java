package ink.ptms.cronus.internal.program.effect.impl;

import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto
public class EffectInventory extends Effect {

    @TInject
    private static TLogger logger;
    private String action;
    private String symbol;
    private String value;

    @Override
    public String pattern() {
        return "inventory\\.(?<action>\\S+)( (?<symbol>\\S+) (?<value>.+))?";
    }

    @Override
    public String getExample() {
        return "inventory.[action] <symbol> <value>";
    }

    @Override
    public void match(Matcher matcher) {
        action = String.valueOf(matcher.group("action")).toLowerCase();
        symbol = String.valueOf(matcher.group("symbol")).toLowerCase();
        value = matcher.group("value");
    }

    @Override
    public void eval(Program program) {
        if (program instanceof QuestProgram) {
            Player player = ((QuestProgram) program).getPlayer();
            switch (action) {
                case "close": {
                    player.closeInventory();
                    break;
                }
                default:
                    logger.warn("Invalid Action: " + action + " " + symbol + " " + value);
                    break;
            }
        }
    }

    @Override
    public String toString() {
        return "EffectInventory{" +
                "action='" + action + '\'' +
                ", symbol='" + symbol + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
