package ink.ptms.cronus.util;

import com.ilummc.tlib.util.Strings;
import me.skymc.taboolib.common.inject.TInject;
import me.skymc.taboolib.cooldown.seconds.CooldownPack2;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;

import java.util.Optional;

/**
 * @Author 坏黑
 * @Since 2019-05-11 15:42
 */
public class Utils {

    @TInject
    static CooldownPack2 actionCooldown = new CooldownPack2("Cronus:Action", 100);

    public static boolean isActionCooldown(CommandSender sender) {
        return actionCooldown.isCooldown(sender.getName(), 0);
    }

    public static boolean isNull(ItemStack item) {
        return item == null || item.getType().equals(Material.AIR);
    }

    public static void addItem(Player player, ItemStack item) {
        player.getInventory().addItem(item).values().forEach(e -> player.getWorld().dropItem(player.getLocation(), e));
    }

    public static String NonNull(String in) {
        return Strings.isBlank(in) ? "-" : in;
    }

    public static ItemStack NonNull(ItemStack itemStack) {
        return Optional.ofNullable(itemStack).orElse(new ItemStack(Material.STONE));
    }

    public static org.bukkit.inventory.ItemStack getUsingItem(Player player, Material material) {
        return player.getItemInHand().getType() == material ? player.getItemInHand() : player.getInventory().getItemInOffHand();
    }

    public static String toSimple(String in) {
        return in.length() > 20 ? in.substring(0, in.length() - (in.length() - 10)) + "..." + in.substring(in.length() - 7) : in;
    }

    public static String fromLocation(Location location) {
        return location.getWorld().getName()
                + "," + (isInt(location.getX()) ? NumberConversions.toInt(location.getX()) : location.getX())
                + "," + (isInt(location.getY()) ? NumberConversions.toInt(location.getY()) : location.getY())
                + "," + (isInt(location.getZ()) ? NumberConversions.toInt(location.getZ()) : location.getZ()) ;
    }

    public static boolean next(int page, int size, int entry) {
        return size / (double) entry > page + 1;
    }

    public static boolean isInt(double in) {
        return NumberConversions.toInt(in) == in;
    }

    public static boolean isInt(String in) {
        try {
            Integer.parseInt(in);
            return true;
        } catch (Throwable ignored) {
        }
        return false;
    }

    public static boolean isDouble(String in) {
        try {
            Double.parseDouble(in);
            return true;
        } catch (Throwable ignored) {
        }
        return false;
    }

    public static boolean isBoolean(String in) {
        try {
            Boolean.parseBoolean(in);
            return true;
        } catch (Throwable ignored) {
        }
        return false;
    }
}
