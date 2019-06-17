package ink.ptms.cronus.internal.bukkit;

import com.google.common.collect.Lists;
import com.ilummc.tlib.logger.TLogger;
import me.skymc.taboolib.common.inject.TInject;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-06-07 20:11
 */
public class FishState {

    @TInject
    private static TLogger logger;
    private List<PlayerFishEvent.State> data = Lists.newArrayList();

    public FishState(String in) {
        for (String state : in.split(";")) {
            try {
                data.add(PlayerFishEvent.State.valueOf(state.toUpperCase()));
            } catch (Throwable ignored) {
                logger.error("FishState \"" + state + "\" parsing failed.");
            }
        }
    }

    public boolean isSelect(PlayerFishEvent.State state) {
        return data.contains(state);
    }

    @Override
    public String toString() {
        return "FishState{" +
                "data=" + data +
                '}';
    }
}
