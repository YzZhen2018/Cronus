package ink.ptms.cronus.internal;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.database.data.DataQuest;
import me.skymc.taboolib.common.schedule.TSchedule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-31 23:03
 */
public class Refresher {

    @TSchedule(period = 20, async = true)
    static void refresh() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            DataPlayer playerData = CronusAPI.getData(player);
            for (Map.Entry<String, DataQuest> entry : playerData.getQuest().entrySet()) {
                if (!playerData.isQuestCompleted(entry.getKey())) {
                    Quest quest = entry.getValue().getQuest();
                    if (quest.getTimeout() != null && quest.getTimeout().isTimeout(entry.getValue())) {
                        playerData.failureQuest(player, quest);
                    }
                }
            }
        }
    }

}
