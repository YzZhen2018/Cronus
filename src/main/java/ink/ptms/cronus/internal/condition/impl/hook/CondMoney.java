package ink.ptms.cronus.internal.condition.impl.hook;

import com.ilummc.tlib.resources.TLocale;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.special.CondNumber;
import me.skymc.taboolib.economy.EcoUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * @Author 坏黑
 * @Since 2019-06-17 20:21
 */
@Cond(name = "player.money", pattern = "player\\.money (?<expression>.+)", example = "player.money [expression]")
public class CondMoney extends CondNumber {

    @Override
    public Number getNumber(Player player, DataQuest quest, Event event) {
        return EcoUtils.exists() ? EcoUtils.get(player) : 0;
    }

    @Override
    public String translate() {
        return TLocale.asString("translate-condition-money", expression.translate());
    }

    @Override
    public String toString() {
        return "CondMoney{" +
                "expression=" + expression +
                '}';
    }
}
