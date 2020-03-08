package ink.ptms.cronus.internal.hook;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.Quest;
import ink.ptms.cronus.internal.QuestStage;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.program.NoneProgram;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.uranus.function.FunctionParser;
import io.izzel.taboolib.module.inject.THook;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-07-26 0:41
 */
@THook
public class HookPlaceholderAPI extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "cronus";
    }

    @Override
    public String getPlugin() {
        return "Cronus";
    }

    @Override
    public String getAuthor() {
        return "坏黑";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    /*
         # [] = required
         # <> = optional

         全局变量: cronus_server_val_[key]
         全局玩家变量: cronus_player_val_[key]
         临时玩家变量: cronus_player_var_[key]
         全局任务变量: cronus_quest_val_[id]:[key]
         临时任务变量: cronus_quest_var_[id]:[key]
         当前任务阶段: cronus_quest_stage_[id]
         任务接受: cronus_accepted_[id]
         任务完成: cronus_completed_[id]:<task>
         条目状态: cronus_progress_[id]:[task]
         函数识别: cronus_function_[function]
     */
    @Override
    public String onPlaceholderRequest(Player player, String s) {
        DataPlayer dataPlayer = CronusAPI.getData(player);
        String in = s.toLowerCase();
        if (in.startsWith("server_val_")) {
            return String.valueOf(Cronus.getCronusService().getDatabase().getGlobalVariable(s.substring("server_val_".length())));
        }
        if (in.startsWith("player_val_")) {
            return String.valueOf(dataPlayer.getDataGlobal().get(s.substring("player_val_".length())));
        }
        if (in.startsWith("player_var_")) {
            return String.valueOf(dataPlayer.getDataTemp().get(s.substring("player_var_".length())));
        }
        if (in.startsWith("quest_val_")) {
            String[] v = s.substring("quest_val_".length()).split(":");
            DataQuest quest = dataPlayer.getQuest().get(v[0]);
            return quest == null && v.length == 1 ? "-" : String.valueOf(quest.getDataQuest().get(v[1]));
        }
        if (in.startsWith("quest_var_")) {
            String[] v = s.substring("quest_var_".length()).split(":");
            DataQuest quest = dataPlayer.getQuest().get(v[0]);
            return quest == null && v.length == 1 ? "-" : String.valueOf(quest.getDataStage().get(v[1]));
        }
        if (in.startsWith("quest_stage_index")) {
            DataQuest dataQuest = dataPlayer.getQuest().get(s.substring("quest_stage_".length()));
            Quest quest = dataQuest.getQuest();
            if (quest == null) {
                return "<Error-Quest>";
            }
            for (int i = 0; i < quest.getStage().size(); i++) {
                if (dataQuest.getCurrentStage().equals(quest.getStage().get(i).getId())) {
                    return String.valueOf(i);
                }
            }
            return "-1";
        }
        if (in.startsWith("quest_stage_")) {
            DataQuest quest = dataPlayer.getQuest().get(s.substring("quest_stage_".length()));
            return quest == null ? "-" : quest.getCurrentStage();
        }
        if (in.startsWith("function_")) {
            return FunctionParser.parseAll(new NoneProgram(player), s.substring("function_".length()));
        }
        if (in.startsWith("accepted_")) {
            return dataPlayer.getQuest().containsKey(s.substring("accepted_".length())) ? "true" : "false";
        }
        if (in.startsWith("completed_")) {
            String[] v = s.substring("completed_".length()).split(":");
            if (v.length == 1) {
                return dataPlayer.getQuestCompleted().containsKey(v[0]) ? "true" : "false";
            } else  {
                DataQuest dataQuest = dataPlayer.getQuest(v[0]);
                if (dataQuest == null) {
                    return "<Error-Quest>";
                }
                QuestStage questStage = dataQuest.getStage();
                if (questStage == null) {
                    return "<Error-Stage>";
                }
                for (QuestTask questTask : questStage.getTask()) {
                    if (questTask.getId().equals(v[1])) {
                        return questTask.isCompleted(dataQuest) ? "true" : "false";
                    }
                }
            }
            return "false";
        }
        if (in.startsWith("progress")) {
            String[] v = s.substring("progress_".length()).split(":");
            if (v.length == 2) {
                DataQuest dataQuest = dataPlayer.getQuest(v[0]);
                if (dataQuest == null) {
                    return "<Error-Quest>";
                }
                QuestStage questStage = dataQuest.getStage();
                if (questStage == null) {
                    return "<Error-Stage>";
                }
                for (QuestTask questTask : questStage.getTask()) {
                    if (questTask.getId().equals(v[1]) && questTask instanceof Countable) {
                        return String.valueOf(((Countable) questTask).getCount(dataQuest));
                    }
                }
            } else {
                return "<Error-Argument>";
            }
            return "-1";
        }
        return "<Error-Placeholder>";
    }
}
