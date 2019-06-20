package ink.ptms.cronus.internal.bukkit;

import com.ilummc.tlib.logger.TLogger;
import ink.ptms.cronus.util.StringExpression;
import me.skymc.taboolib.common.inject.TInject;
import me.skymc.taboolib.inventory.ItemUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-05-30 17:16
 */
public class Enchantment {

    @TInject
    private static TLogger logger;
    private static Pattern pattern = Pattern.compile("(?<enchant>\\S+)[ ]?(?<expression>.+)");
    private List<Point> points;

    public Enchantment(String in) {
        points = Arrays.stream(in.split(";")).map(Point::new).collect(Collectors.toList());
    }

    public boolean isSelect(org.bukkit.enchantments.Enchantment enchant, int level) {
        return points.stream().anyMatch(b -> b.isSelect(enchant, level));
    }

    public class Point {

        private org.bukkit.enchantments.Enchantment enchant;
        private StringExpression expression;

        public Point(String in) {
            Matcher matcher = pattern.matcher(in);
            if (!matcher.find()) {
                logger.error("Enchantment \"" + in + "\" parsing failed.");
                return;
            }
            enchant = ItemUtils.asEnchantment(matcher.group("enchant").toUpperCase());
            if (enchant == null) {
                logger.error("Enchantment \"" + in + "\" parsing failed.");
                return;
            }
            expression = new StringExpression(matcher.group("expression"));
        }

        public boolean isSelect(org.bukkit.enchantments.Enchantment enchant, int level) {
            return enchant.equals(this.enchant) && expression.isSelect(level);
        }

        @Override
        public String toString() {
            return "Point{" +
                    "enchant=" + enchant +
                    ", expression=" + expression +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Enchantment{" +
                "points=" + points +
                '}';
    }
}
