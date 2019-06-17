package ink.ptms.cronus.service.hook;

import ch.njol.skript.ScriptLoader;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @Author 坏黑
 * @Since 2019-06-17 23:44
 */
public class SkriptHook {

    private static String currentEventName;
    private static Class<? extends Event>[] currentEvents;

    public static void toggleCurrentEvent(boolean var) {
        if (var) {
            currentEventName = ScriptLoader.getCurrentEventName();
            currentEvents = ScriptLoader.getCurrentEvents();
            ScriptLoader.setCurrentEvent("command", PlayerCommandPreprocessEvent.class);
        } else {
            ScriptLoader.setCurrentEvent(currentEventName, currentEvents);
        }
    }

}
