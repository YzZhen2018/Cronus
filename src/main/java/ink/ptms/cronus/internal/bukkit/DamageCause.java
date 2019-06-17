package ink.ptms.cronus.internal.bukkit;

import com.google.common.collect.Lists;
import com.ilummc.tlib.logger.TLogger;
import me.skymc.taboolib.common.inject.TInject;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-06-07 20:11
 */
public class DamageCause {

    @TInject
    private static TLogger logger;
    private List<EntityDamageEvent.DamageCause> data = Lists.newArrayList();

    public DamageCause(String in) {
        for (String state : in.split(";")) {
            try {
                data.add(EntityDamageEvent.DamageCause.valueOf(state.toUpperCase()));
            } catch (Throwable ignored) {
                logger.error("DamageCause \"" + state + "\" parsing failed.");
            }
        }
    }

    public boolean isSelect(EntityDamageEvent.DamageCause cause) {
        return data.contains(cause);
    }

    @Override
    public String toString() {
        return "DamageCause{" +
                "data=" + data +
                '}';
    }
}
