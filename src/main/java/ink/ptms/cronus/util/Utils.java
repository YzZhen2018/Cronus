package ink.ptms.cronus.util;

import com.ilummc.tlib.util.Strings;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * @Author 坏黑
 * @Since 2019-05-11 15:42
 */
public class Utils {

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
        if (in.length() > 20) {
            return in.substring(0, in.length() - (in.length() - 10)) + "..." + in.substring(in.length() - 7);
        }
        return in;
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
