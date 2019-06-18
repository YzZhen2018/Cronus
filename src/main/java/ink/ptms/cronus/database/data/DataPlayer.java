package ink.ptms.cronus.database.data;

import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.event.CronusQuestAcceptEvent;
import ink.ptms.cronus.event.CronusQuestFailureEvent;
import ink.ptms.cronus.internal.Quest;
import ink.ptms.cronus.internal.program.Action;
import ink.ptms.cronus.internal.program.QuestProgram;
import me.skymc.taboolib.common.serialize.DoNotSerialize;
import me.skymc.taboolib.common.serialize.TSerializable;
import me.skymc.taboolib.common.serialize.TSerializer;
import me.skymc.taboolib.common.serialize.TSerializerElementGeneral;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-24 0:29
 */
public class DataPlayer implements TSerializable {

    @DoNotSerialize
    private Player player;
    @DoNotSerialize
    private YamlConfiguration dataTemp = new YamlConfiguration();
    // 全局数据
    private YamlConfiguration dataGlobal = new YamlConfiguration();
    // 任务数据
    private Map<String, DataQuest> quest = Maps.newConcurrentMap();
    // 任务完成记录
    private Map<String, Long> questCompleted = Maps.newConcurrentMap();

    public DataPlayer(Player player) {
        this.player = player;
    }

    public DataQuest getQuest(String questId) {
        for (Map.Entry<String, DataQuest> entry : quest.entrySet()) {
            Quest quest = entry.getValue().getQuest();
            if (quest != null && (questId.equals(quest.getId()) || questId.equals(quest.getLabel()))) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void clearDataTemp() {
        dataTemp = new YamlConfiguration();
    }

    public void clearDataGlobal() {
        dataGlobal = new YamlConfiguration();
    }

    public void acceptQuest(Player player, Quest quest) {
        CronusQuestAcceptEvent call = CronusQuestAcceptEvent.call(player, quest);
        if (!call.isCancelled()) {
            // 获取数据
            DataQuest dataQuest = this.quest.computeIfAbsent(call.getQuest().getId(), d -> new DataQuest(call.getQuest().getId(), call.getQuest().getFirstStage()));
            // 执行动作
            call.getQuest().eval(new QuestProgram(player, dataQuest), Action.ACCEPT);
        }
    }

    public void failureQuest(Player player, Quest quest) {
        DataQuest dataQuest = this.quest.remove(quest.getId());
        if (dataQuest != null) {
            CronusQuestFailureEvent.call(player, quest);
            // 执行动作
            quest.eval(new QuestProgram(player, dataQuest), Action.FAILURE);
        }
    }

    public boolean isQuestCompleted(String id) {
        return questCompleted.getOrDefault(id, 0L) > 0;
    }

    public void setQuestCompleted(String id, boolean var) {
        if (var) {
            questCompleted.put(id, System.currentTimeMillis());
        } else {
            questCompleted.remove(id);
        }
    }

    @Override
    public void read(String fieldName, String value) {
        switch (fieldName) {
            case "quest":
                TSerializer.readMap(quest, value, TSerializerElementGeneral.STRING.getSerializer(), DataQuestSerializer.INSTANCE);
                break;
            case "questCompleted":
                TSerializer.readMap(questCompleted, value, TSerializerElementGeneral.STRING, TSerializerElementGeneral.LONG);
                break;
            case "dataGlobal":
                try {
                    dataGlobal.loadFromString(value);
                } catch (InvalidConfigurationException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public String write(String fieldName, Object value) {
        switch (fieldName) {
            case "quest":
                return TSerializer.writeMap(quest, TSerializerElementGeneral.STRING.getSerializer(), DataQuestSerializer.INSTANCE);
            case "questCompleted":
                return TSerializer.writeMap(questCompleted, TSerializerElementGeneral.STRING, TSerializerElementGeneral.LONG);
            case "dataGlobal":
                return dataGlobal.saveToString();
        }
        return null;
    }

    public DataPlayer push() {
        Cronus.getCronusService().async(() -> Cronus.getCronusService().getDatabase().upload(player, this));
        return this;
    }

    // *********************************
    //
    //        Getter and Setter
    //
    // *********************************

    public Player getPlayer() {
        return player;
    }

    public Map<String, DataQuest> getQuest() {
        return quest;
    }

    public Map<String, Long> getQuestCompleted() {
        return questCompleted;
    }

    public YamlConfiguration getDataGlobal() {
        return dataGlobal;
    }

    public YamlConfiguration getDataTemp() {
        return dataTemp;
    }
}
