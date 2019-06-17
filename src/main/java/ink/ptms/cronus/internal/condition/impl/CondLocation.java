package ink.ptms.cronus.internal.condition.impl;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.Condition;
import me.skymc.taboolib.inventory.TEquipment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.NumberConversions;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:12
 */
@Cond(name = "location", pattern = "location (?<symbol>\\S+) (?<location>.+)")
public class CondLocation extends Condition {

    private String symbol;
    private Location location;

    @Override
    public void init(Matcher matcher, String text) {
        symbol = matcher.group("symbol");
        location = BukkitParser.toLocation(matcher.group("location"));
    }

    @Override
    public boolean isValid(Player player, DataQuest quest, Event event) {
        switch (symbol) {
            case "=":
            case "==":
                return location.inSelect(player.getLocation());
            case "!=":
                return !location.inSelect(player.getLocation());
        }
        return false;
    }

    @Override
    public String toString() {
        return "CondLocation{" +
                "symbol='" + symbol + '\'' +
                ", location=" + location +
                '}';
    }
}
