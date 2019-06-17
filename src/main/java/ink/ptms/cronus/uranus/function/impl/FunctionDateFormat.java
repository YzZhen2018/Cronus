package ink.ptms.cronus.uranus.function.impl;

import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;
import org.bukkit.util.NumberConversions;

import java.text.SimpleDateFormat;

/**
 * @Author 坏黑
 * @Since 2019-05-12 15:24
 */
@Auto
public class FunctionDateFormat extends Function {

    private SimpleDateFormat normal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String getName() {
        return "date.format";
    }

    @Override
    public Object eval(Program program, String... args) {
        try {
            return (args.length > 1 ? new SimpleDateFormat(args[1]) : normal).format(NumberConversions.toLong(args[0]));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return "<Invalid-Format>";
    }
}
