package ink.ptms.cronus.internal.special;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.util.StringExpression;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-06-07 19:50
 */
public abstract class Uncountable extends QuestTask {

    protected StringExpression total;

    public Uncountable(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        total = data.containsKey("total") ? new StringExpression(data.get("total")) : null;
    }

    @Override
    public boolean isCompleted(DataQuest dataQuest) {
        return dataQuest.getDataStage().getInt(getId() + ".complete") > 0 || total.isSelect(dataQuest.getDataStage().getInt(getId() + ".total"));
    }

    @Override
    public void next(Player player, DataQuest dataQuest, Event event) {
        dataQuest.getDataStage().set(getId() + ".total", dataQuest.getDataStage().getInt(getId() + ".total") + getCount(player, dataQuest, event));
    }

    @Override
    public void complete(DataQuest dataQuest) {
        dataQuest.getDataStage().set(getId() + ".complete", 1);
    }

    @Override
    public void reset(DataQuest dataQuest) {
        dataQuest.getDataStage().set(getId() + ".complete", 0);
        dataQuest.getDataStage().set(getId() + ".count", 0);
    }

    abstract public int getCount(Player player, DataQuest dataQuest, Event event);
}
