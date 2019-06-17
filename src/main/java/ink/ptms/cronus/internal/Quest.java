package ink.ptms.cronus.internal;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ilummc.tlib.resources.TLocale;
import ink.ptms.cronus.database.data.time.Time;
import ink.ptms.cronus.database.data.time.TimeType;
import ink.ptms.cronus.internal.program.Action;
import ink.ptms.cronus.internal.program.Actionable;
import ink.ptms.cronus.internal.program.QuestEffect;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.util.StringDate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.NumberConversions;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author 坏黑
 * @Since 2019-05-24 0:00
 */
public class Quest extends Actionable {

    protected ConfigurationSection config;
    protected String id;
    protected String label;
    protected String display;
    protected Time timeout;
    protected long cooldown;
    protected List<String> keyword;
    protected List<QuestStage> stage = Lists.newArrayList();

    public Quest(ConfigurationSection conf) {
        this.config = conf;
        this.id = conf.getName();
        this.label = conf.getString("label");
        this.display = conf.getString("display", id);
        this.keyword = conf.getStringList("keyword");
        if (conf.contains("cooldown")) {
            this.cooldown = conf.getString("cooldown").equalsIgnoreCase("never") || conf.getString("cooldown").equals("-1") ? -1 : StringDate.parse(conf.getString("cooldown"));
        }
        if (conf.contains("timeout")) {
            this.timeout = Time.parse(conf.getString("timeout").toLowerCase());
        }
    }

    public String getFirstStage() {
        return stage.size() > 0 ? stage.get(0).getId() : null;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public List<String> getKeyword() {
        return keyword;
    }

    public String getLabel() {
        return label;
    }

    public Time getTimeout() {
        return timeout;
    }

    public long getCooldown() {
        return cooldown;
    }

    public List<QuestStage> getStage() {
        return stage;
    }

    public ConfigurationSection getConfig() {
        return config;
    }

    @Override
    public String toString() {
        return "Quest{" +
                "config=" + config +
                ", id='" + id + '\'' +
                ", display='" + display + '\'' +
                ", timeout=" + timeout +
                ", cooldown=" + cooldown +
                ", keyword=" + keyword +
                ", stage=" + stage +
                ", action=" + action +
                '}';
    }
}
