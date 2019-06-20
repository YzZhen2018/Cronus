package ink.ptms.cronus.internal.program.effect;

import com.ilummc.tlib.logger.TLogger;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;
import me.skymc.taboolib.common.inject.TInject;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
public class EffectNull extends Effect {

    @TInject
    private static TLogger logger;
    private String value;

    public EffectNull(String value) {
        this.value = value;
    }

    @Override
    public String pattern() {
        return "null";
    }

    @Override
    public String getExample() {
        return "null";
    }

    @Override
    public void match(Matcher matcher) {
    }

    @Override
    public void eval(Program program) {
        logger.error("Effect \"" + value + "\" not available.");
    }
}
