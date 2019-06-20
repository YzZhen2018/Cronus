package ink.ptms.cronus.internal.condition.impl.hook;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.Condition;
import me.skymc.taboolib.support.SupportWorldGuard;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:12
 */
@Cond(name = "worldguard", pattern = "worldguard (?<symbol>\\S+) (?<name>.+)", example = "worldguard [symbol] [name]")
public class CondWorldGuard extends Condition {

    private String symbol;
    private String name;

    @Override
    public void init(Matcher matcher, String text) {
        symbol = matcher.group("symbol");
        name = matcher.group("name");
    }

    @Override
    public boolean check(Player player, DataQuest quest, Event event) {
        return symbol.startsWith("=") == SupportWorldGuard.INSTANCE.getRegionsAtLocation(player.getWorld(), player.getLocation()).stream().anyMatch(name::equalsIgnoreCase);
    }

    @Override
    public String toString() {
        return "CondWorldGuard{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
