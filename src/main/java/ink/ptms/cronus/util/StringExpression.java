package ink.ptms.cronus.util;

import com.ilummc.tlib.logger.TLogger;
import me.skymc.taboolib.common.inject.TInject;
import org.bukkit.util.NumberConversions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author 坏黑
 * @Since 2019-06-07 23:51
 */
public class StringExpression {

    @TInject
    private static TLogger logger;
    private static Pattern pattern = Pattern.compile("(?<symbol>>|>=|<|<=|=|==)[ ]?(?<number>.+)");
    private String symbol;
    private double number;

    public StringExpression(Object in) {
        Matcher matcher = pattern.matcher(String.valueOf(in));
        if (!matcher.find()) {
            logger.error("StringExpression \"" + in + "\" parsing failed.");
            return;
        }
        symbol = matcher.group("symbol");
        number = NumberConversions.toDouble(matcher.group("number"));
    }
    
    public boolean isSelect(double number) {
        switch (symbol) {
            case ">":
                return number > this.number;
            case ">=":
                return number >= this.number;
            case "<":
                return number < this.number;
            case "<=":
                return number <= this.number;
            case "=":
            case "==":
                return number == this.number;
            default:
                return true;
        }
    }

    @Override
    public String toString() {
        return "StringExpression{" +
                "symbol='" + symbol + '\'' +
                ", number=" + number +
                '}';
    }
}
