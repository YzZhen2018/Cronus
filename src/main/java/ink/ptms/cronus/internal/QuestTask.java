package ink.ptms.cronus.internal;

import com.ilummc.tlib.logger.TLogger;
import com.ilummc.tlib.resources.TLocale;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.service.guide.GuideWayCache;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.internal.condition.ConditionParser;
import ink.ptms.cronus.internal.program.Actionable;
import me.skymc.taboolib.common.inject.TInject;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-24 0:24
 */
public abstract class QuestTask extends Actionable {

    @TInject
    protected static TLogger logger;
    protected String id;
    protected ConfigurationSection config;
    protected Condition condition;
    protected GuideWayCache guide;

    public QuestTask(ConfigurationSection config) {
        this.id = config.getName();
        this.config = config;
        Object condition = config.get("condition");
        if (condition instanceof Condition) {
            this.condition = (Condition) condition;
        } else if (condition instanceof String) {
            this.condition = ConditionParser.parse((String) condition);
        }
        if (config.contains("data")) {
            init(config.getConfigurationSection("data").getValues(false));
        }
        if (config.contains("guide")) {
            Location target = BukkitParser.toLocation(config.getString("guide.target"));
            if (target.isBukkit()) {
                guide = new GuideWayCache(config.getInt("guide.distance", 2), target, TLocale.Translate.setColored(config.getStringList("guide.text")));
            } else {
                logger.error("Guide Target \"" + config.getString("guide.target") + "\" parsing failed.");
            }
        }
    }

    abstract public void init(Map<String, Object> data);

    abstract public boolean isCompleted(DataQuest dataQuest);

    abstract public boolean isValid(Player player, DataQuest dataQuest, Event event);

    abstract public void next(Player player, DataQuest dataQuest, Event event);

    public void complete(DataQuest dataQuest) {
    }

    public void reset(DataQuest dataQuest) {
    }

    public String getId() {
        return id;
    }

    public ConfigurationSection getConfig() {
        return config;
    }

    public Condition getCondition() {
        return condition;
    }

    public GuideWayCache getGuide() {
        return guide;
    }
}
