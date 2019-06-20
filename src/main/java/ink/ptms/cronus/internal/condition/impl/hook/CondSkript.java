package ink.ptms.cronus.internal.condition.impl.hook;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.service.hook.SkriptHook;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 11:12
 */
@Cond(name = "skript", pattern = "sk(ript)? (?<script>.+)", example = "skript [skript]")
public class CondSkript extends Condition {

    private ch.njol.skript.lang.Condition condition;

    @Override
    public void init(Matcher matcher, String text) {
        SkriptHook.toggleCurrentEvent(true);
        try {
            condition = ch.njol.skript.lang.Condition.parse(matcher.group("script"), null);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        SkriptHook.toggleCurrentEvent(false);
    }

    @Override
    public boolean check(Player player, DataQuest quest, Event event) {
        return condition != null && condition.check(new PlayerCommandPreprocessEvent(player, ""));
    }

    @Override
    public String toString() {
        return "CondSkript{" +
                "condition=" + condition +
                '}';
    }
}
