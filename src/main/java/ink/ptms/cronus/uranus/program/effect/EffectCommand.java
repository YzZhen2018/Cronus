package ink.ptms.cronus.uranus.program.effect;

import com.ilummc.tlib.logger.TLogger;
import com.ilummc.tlib.resources.TLocale;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.FunctionParser;
import ink.ptms.cronus.uranus.program.Program;
import me.skymc.taboolib.commands.TabooLibExecuteCommand;
import me.skymc.taboolib.common.inject.TInject;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto
public class EffectCommand extends Effect {

    @TInject
    private static TLogger logger;
    private String type;
    private String value;

    @Override
    public String pattern() {
        return "command\\.(?<type>\\S+) (?<value>.+)";
    }

    @Override
    public void match(Matcher matcher) {
        type = matcher.group("type");
        value = matcher.group("value");
    }

    @Override
    public void eval(Program program) {
        String parsed = TLocale.Translate.setColored(FunctionParser.parseAll(program, value));
        switch (type.toLowerCase()) {
            case "server":
            case "console":
                TabooLibExecuteCommand.dispatchCommand(Bukkit.getConsoleSender(), parsed);
                break;
            case "player":
            case "sender":
                TabooLibExecuteCommand.dispatchCommand(program.getSender(), parsed);
                break;
            case "player.op":
            case "sender.op":
                boolean isOp = program.getSender().isOp();
                program.getSender().setOp(true);
                try {
                    TabooLibExecuteCommand.dispatchCommand(program.getSender(), parsed);
                } catch (Exception t) {
                    t.printStackTrace();
                }
                program.getSender().setOp(isOp);
                break;
            default:
                logger.warn("Invalid type: " + type);
                break;
        }
    }

    @Override
    public String toString() {
        return "EffectCommand{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
