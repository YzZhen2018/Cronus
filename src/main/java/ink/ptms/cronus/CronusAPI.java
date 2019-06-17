package ink.ptms.cronus;

import com.google.common.collect.Lists;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.event.CronusTaskSuccessEvent;
import ink.ptms.cronus.service.guide.GuideWay;
import ink.ptms.cronus.service.guide.GuideWayData;
import ink.ptms.cronus.internal.QuestStage;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.program.Action;
import ink.ptms.cronus.internal.program.QuestProgram;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-05-24 0:09
 */
public class CronusAPI {

    public static DataPlayer getData(Player player) {
        return Cronus.getCronusService().getPlayerData().getOrDefault(player.getName(), new DataPlayer(player));
    }

    public static void guideHandle(Player player) {
        GuideWay service = Cronus.getCronusService().getService(GuideWay.class);
        service.cancel(player);
        DataPlayer playerData = CronusAPI.getData(player);
        playerData.getQuest().values().forEach(dataQuest -> {
            QuestStage questStage = dataQuest.getStage();
            if (questStage == null) {
                return;
            }
            for (QuestTask questTask : questStage.getTask()) {
                // 尚未完成
                if (!questTask.isCompleted(dataQuest)
                        // 有引导
                        && questTask.getGuide() != null
                        // 同世界
                        && questTask.getGuide().getTarget().isSelectWorld(player.getLocation())) {
                    // 创建引导
                    service.add(player, GuideWayData.create(player, questTask.getGuide().getTarget().toBukkit(), questTask.getGuide().getText(), questTask.getGuide().getDistance()));
                }
            }
        });
    }

    @SafeVarargs
    public static void stageHandle(Player player, Event event, Class<? extends QuestTask>... tasks) {
        if (tasks.length > 0) {
            stageHandle(player, event, Lists.newArrayList(tasks));
        }
    }

    public static void stageHandle(Player player, Event event, List<Class<? extends QuestTask>> tasks) {
        CronusMirror.Data mirror = CronusMirror.getMirror("StageHandle:" + (event == null ? "Period" : event.getEventName()));
        DataPlayer playerData = CronusAPI.getData(player);
        boolean changed = false;
        mirror.start();
        for (DataQuest dataQuest : playerData.getQuest().values()) {
            QuestStage questStage = dataQuest.getStage();
            if (questStage == null) {
                continue;
            }
            for (QuestTask questTask : questStage.getTask()) {
                if (tasks.contains(questTask.getClass())
                        // 尚未完成
                        && !questTask.isCompleted(dataQuest)
                        // 事件检测
                        && questTask.isValid(player, dataQuest, event)
                        // 条件检测
                        && (questTask.getCondition() == null || questTask.getCondition().isValid(player, dataQuest, event))) {
                    // 条目进行
                    questTask.next(player, dataQuest, event);
                    // 条目完成
                    if (questTask.isCompleted(dataQuest)) {
                        CronusTaskSuccessEvent.call(player, dataQuest.getQuest(), questStage, questTask);
                        questTask.eval(new QuestProgram(player, dataQuest), Action.SUCCESS);
                        dataQuest.checkAndComplete(player, dataQuest);
                    }
                    changed = true;
                }
            }
        }
        if (changed) {
            playerData.push();
        }
        mirror.stop();
    }
}
