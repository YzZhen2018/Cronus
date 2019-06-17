package ink.ptms.cronus.command;

import com.ilummc.tlib.resources.TLocale;
import me.skymc.taboolib.commands.internal.BaseMainCommand;
import me.skymc.taboolib.other.NumberUtils;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-05-31 21:59
 */
public abstract class CronusCommand extends BaseMainCommand {

    private String normal = "§7§l[§f§lCronus§7§l] §7";
    private String error = "§c§l[§4§lCronus§c§l] §c";

    protected void normal(CommandSender sender, String args) {
        sender.sendMessage(normal + TLocale.Translate.setColored(args));
        // 音效
        if (sender instanceof Player) {
            ((Player) sender).playSound(((Player) sender).getLocation(), Sound.ENTITY_VILLAGER_TRADING, 1f, (float) NumberUtils.getRandomDouble(0, 2));
        }
    }

    protected void error(CommandSender sender, String args) {
        sender.sendMessage(error + TLocale.Translate.setColored(args));
        // 音效
        if (sender instanceof Player) {
            ((Player) sender).playSound(((Player) sender).getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, (float) NumberUtils.getRandomDouble(0, 2));
        }
    }

    @Override
    public String getCommandTitle() {
        return "§e§l----- §6§lCronus Commands §e§l-----";
    }
}
