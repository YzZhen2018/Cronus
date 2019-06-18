package ink.ptms.cronus.internal;

import com.google.common.collect.Lists;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.program.Actionable;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:25
 */
public class QuestStage extends Actionable {

    protected List<QuestTask> task = Lists.newArrayList();
    protected String id;
    protected List<List<String>> content = Lists.newArrayList();
    protected List<List<String>> contentCompleted = Lists.newArrayList();
    protected List<String> contentGlobal;
    protected ConfigurationSection config;

    public QuestStage(ConfigurationSection config) {
        this.id = config.getName();
        this.config = config;
        this.contentGlobal = config.getStringList("content-global");
        ConfigurationSection content = config.getConfigurationSection("content");
        if (content != null) {
            content.getKeys(false).forEach(id -> this.content.add(content.getStringList(id)));
        }
        ConfigurationSection contentCompleted = config.getConfigurationSection("content-completed");
        if (contentCompleted != null) {
            contentCompleted.getKeys(false).forEach(id -> this.contentCompleted.add(contentCompleted.getStringList(id)));
        }
    }

    public boolean isCompleted(DataQuest quest) {
        return task.stream().allMatch(t -> t.isCompleted(quest));
    }

    public String getId() {
        return id;
    }

    public List<QuestTask> getTask() {
        return task;
    }

    public List<List<String>> getContent() {
        return content;
    }

    public List<List<String>> getContentCompleted() {
        return contentCompleted;
    }

    public List<String> getContentGlobal() {
        return contentGlobal;
    }

    public ConfigurationSection getConfig() {
        return config;
    }

    @Override
    public String toString() {
        return "QuestStage{" +
                "task=" + task +
                ", id='" + id + '\'' +
                ", content=" + content +
                ", contentCompleted=" + contentCompleted +
                ", config=" + config +
                ", action=" + action +
                '}';
    }
}
