package ink.ptms.cronus.command;

import com.ilummc.tlib.resources.TLocale;
import me.skymc.taboolib.commands.internal.BaseMainCommand;
import me.skymc.taboolib.common.inject.TInject;
import me.skymc.taboolib.cooldown.seconds.CooldownPack2;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-05-31 21:59
 */
public abstract class CronusCommand extends BaseMainCommand {

    @TInject
    private static CooldownPack2 cooldown = new CooldownPack2("Cronus:CommandSound", 100);
    private static String normal = "§7§l[§f§lCronus§7§l] §7";
    private static String error = "§c§l[§4§lCronus§c§l] §c";

    public static void normal(CommandSender sender, String args) {
        sender.sendMessage(normal + TLocale.Translate.setColored(args));
        // 音效
        if (sender instanceof Player && !cooldown.isCooldown(sender.getName(), 0)) {
            ((Player) sender).playSound(((Player) sender).getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 2f);
        }
    }

    public static void error(CommandSender sender, String args) {
        sender.sendMessage(error + TLocale.Translate.setColored(args));
        // 音效
        if (sender instanceof Player && !cooldown.isCooldown(sender.getName(), 0)) {
            ((Player) sender).playSound(((Player) sender).getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        }
    }

    @Override
    public String getCommandTitle() {
        return "§e§l----- §6§lCronus Commands §e§l-----";
    }
}
