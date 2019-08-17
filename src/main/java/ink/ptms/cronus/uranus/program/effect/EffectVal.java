package ink.ptms.cronus.uranus.program.effect;

import com.google.common.collect.Lists;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.FunctionParser;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.util.Strumber;
import ink.ptms.cronus.util.Utils;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.NumberConversions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 15:24
 */
@Auto
public class EffectVal extends Effect {

    @TInject
    private static TLogger logger;
    private static YamlConfiguration data = new YamlConfiguration();
    private String name;
    private String symbol;
    private String value;

    @Override
    public String pattern() {
        return "val\\.(?<name>\\S+) (?<symbol>\\S+)( (?<value>.+))?";
    }

    @Override
    public String getExample() {
        return "val.[name] [symbol] [content]";
    }

    @Override
    public void match(Matcher matcher) {
        name = matcher.group("name");
        value = matcher.group("value");
        symbol = matcher.group("symbol");
    }

    @Override
    public void eval(Program program) {
        eval(program, value, symbol, name, data);
    }

    public static void eval(Program program, String value, String symbol, String name, YamlConfiguration data) {
        String parsed = value == null ? "" : FunctionParser.parseAll(program, value);
        // 赋值
        if (symbol.equals("=")) {
            if (parsed.equals("[]")) {
                data.set(name, Lists.newArrayList());
            } else if (parsed.equalsIgnoreCase("null")) {
                data.set(name, null);
            } else {
                data.set(name, new Strumber(parsed).get());
            }
        }
        // 设置
        else if (symbol.startsWith("=:")) {
            // 列表检查
            if (data.isList(name)) {
                List list = data.getList(name);
                try {
                    list.set(NumberConversions.toInt(symbol.substring("=:".length())), parsed);
                } catch (Throwable t) {
                    logger.warn("Invalid: " + t.getMessage());
                }
                data.set(name, list);
            }
        }
        // 增加
        else if (symbol.equals("+")) {
            if (data.contains(name)) {
                // 列表检查
                if (data.isList(name)) {
                    List list = data.getList(name);
                    list.add(parsed);
                    data.set(name, list);
                    return;
                }
                // 文本检查
                data.set(name, new Strumber(data.getString(name)).add(parsed).get());
            } else {
                data.set(name, new Strumber(parsed).get());
            }
        }
        // 插入
        else if (symbol.startsWith("+:")) {
            // 列表检查
            if (data.isList(name)) {
                List list = data.getList(name);
                try {
                    list.add(NumberConversions.toInt(symbol.substring("+:".length())), parsed);
                } catch (Throwable t) {
                    logger.warn("Invalid: " + t.getMessage());
                }
                data.set(name, list);
            }
        }
        // 减少
        else if (symbol.equals("-")) {
            if (data.contains(name)) {
                // 列表检查
                if (data.isList(name)) {
                    List list = data.getList(name);
                    list.remove(parsed);
                    data.set(name, list);
                    return;
                }
                // 文本检查
                data.set(name, new Strumber(data.getString(name)).add(parsed).get());
            } else if (Utils.isDouble(parsed)) {
                data.set(name, new Strumber(0).subtract(parsed).get());
            }
        }
        // 替换
        else if (symbol.startsWith("r:")) {
            if (data.contains(name)) {
                String key = symbol.substring("r:".length());
                // 列表检查
                if (data.isList(name)) {
                    List list = data.getList(name);
                    for (int i = 0; i < list.size(); i++) {
                        list.set(i, String.valueOf(list.get(i)).replace(key, parsed));
                    }
                    data.set(name, list);
                    return;
                }
                // 文本检查
                data.set(name, data.getString(name).replace(key, parsed));
            }
        } else {
            logger.warn("Invalid Symbol: " + symbol);
        }
    }

    @Override
    public String toString() {
        return "EffectVal{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public static int getInt(String name, int def) {
        return data.getInt(name, def);
    }

    public static int getInt(String name) {
        return data.getInt(name, 0);
    }

    public static double getDouble(String name, double def) {
        return data.getDouble(name, def);
    }

    public static double getDouble(String name) {
        return data.getDouble(name, 0);
    }

    public static boolean getBoolean(String name, boolean def) {
        return data.getBoolean(name, def);
    }

    public static boolean getBoolean(String name) {
        return data.getBoolean(name, false);
    }

    public static String getString(String name) {
        return data.getString(name);
    }

    public static String getString(String name, String def) {
        return data.getString(name, def);
    }

    public static Object get(String name) {
        return data.get(name);
    }

    public static Object get(String name, Object def) {
        return data.get(name, def);
    }

    public static boolean contains(String name) {
        return data.contains(name);
    }

    public static Set<String> getKeys(String name) {
        ConfigurationSection section = data.getConfigurationSection(name);
        return section != null ? section.getKeys(false) : new HashSet<>();
    }
}
