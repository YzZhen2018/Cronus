package ink.ptms.cronus.util;

import org.bukkit.util.NumberConversions;

/**
 * @Author 坏黑
 * @Since 2019-05-29 23:58
 */
public class StringDate {

    public static long parse(String in) {
        if (in == null) {
            return 0;
        }
        if (in.equalsIgnoreCase("never") || in.equalsIgnoreCase("-1")) {
            return -1;
        }
        long time = 0;
        StringBuilder current = new StringBuilder();
        for (String charAt : in.toLowerCase().split("")) {
            if (Utils.isInt(charAt)) {
                current.append(charAt);
            } else {
                switch (charAt) {
                    case "d":
                        time += NumberConversions.toInt(current.toString()) * 24L * 60L * 60L * 1000L;
                        break;
                    case "h":
                        time += NumberConversions.toInt(current.toString()) * 60L * 60L * 1000L;
                        break;
                    case "m":
                        time += NumberConversions.toInt(current.toString()) * 60L * 1000L;
                        break;
                    case "s":
                        time += NumberConversions.toInt(current.toString()) * 1000L;
                        break;
                }
                current = new StringBuilder();
            }
        }
        return time;
    }
}
