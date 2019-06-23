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
public class PotionEffect {

    @TInject
    private static TLogger logger;
    private static Pattern pattern = Pattern.compile("(?<potion>\\S+)[ ]?(?<expression>.+)");
    private List<Point> points;

    public PotionEffect(String in) {
        points = Arrays.stream(in.split("[,;]")).map(Point::new).collect(Collectors.toList());
    }

    public boolean isSelect(org.bukkit.potion.PotionEffectType potionType, int level) {
        return points.stream().anyMatch(b -> b.isSelect(potionType, level));
    }

    public class Point {

        private org.bukkit.potion.PotionEffectType potionType;
        private StringExpression expression;

        public Point(String in) {
            Matcher matcher = pattern.matcher(in);
            if (!matcher.find()) {
                logger.error("PotionEffect \"" + in + "\" parsing failed.");
                return;
            }
            potionType = ItemUtils.asPotionEffectType(matcher.group("potion").toUpperCase());
            if (potionType == null) {
                logger.error("PotionEffect \"" + in + "\" parsing failed.");
                return;
            }
            expression = new StringExpression(matcher.group("expression"));
        }

        public boolean isSelect(org.bukkit.potion.PotionEffectType potionType, int level) {
            return potionType.equals(this.potionType) && expression.isSelect(level);
        }

        @Override
        public String toString() {
            return "Point{" +
                    "potionType=" + potionType +
                    ", expression=" + expression +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PotionEffect{" +
                "points=" + points +
                '}';
    }
}
