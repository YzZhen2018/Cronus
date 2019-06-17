package ink.ptms.cronus.uranus.program.effect;

import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.program.Program;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 15:24
 */
@Auto
public class EffectVar extends Effect {

    private String name;
    private String symbol;
    private String value;

    @Override
    public String pattern() {
        return "var\\.(?<name>\\S+) (?<symbol>\\S+)( (?<value>.+))?";
    }

    @Override
    public void match(Matcher matcher) {
        name = matcher.group("name");
        value = matcher.group("value");
        symbol = matcher.group("symbol");
    }

    @Override
    public void eval(Program program) {
        EffectVal.eval(program, value, symbol, name, program.getData());
    }

    @Override
    public String toString() {
        return "EffectVar{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
