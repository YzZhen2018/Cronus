package ink.ptms.cronus.service.dialog;

import ink.ptms.cronus.event.CronusInitDialogEvent;
import ink.ptms.cronus.internal.program.NoneProgram;
import ink.ptms.cronus.internal.program.QuestEffect;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.service.dialog.impl.DisplayMenu;
import io.izzel.taboolib.module.locale.TLocale;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-06-10 17:36
 */
public class DialogGroup {

    private ConfigurationSection config;
    private String id;
    private String title;
    private String target;
    private DialogPack dialog;
    private DialogDisplay display;
    private QuestEffect open;
    private QuestEffect close;

    public DialogGroup(ConfigurationSection config) {
        this.config = config;
        this.id = config.getName();
        this.title = TLocale.Translate.setColored(config.getString("title", "对话"));
        this.target = config.getString("target");
        this.dialog = new DialogPack(this, config.getConfigurationSection("dialog").getValues(false));
        this.display = new DisplayMenu();
        this.open = config.contains("open") ? new QuestEffect(config.getStringList("open")) : null;
        this.close = config.contains("close") ? new QuestEffect(config.getStringList("close")) : null;
        CronusInitDialogEvent.call(this);
    }

    public void openEval(Player player) {
        if (open != null) {
            new NoneProgram(player).eval(open.getEffect());
        }
    }

    public void openEval(QuestProgram program) {
        if (open != null) {
            open.eval(program.getPlayer(), program.getDataQuest());
        }
    }

    public void closeEval(Player player) {
        if (close != null) {
            new NoneProgram(player).eval(close.getEffect());
        }
    }

    public void closeEval(QuestProgram program) {
        if (close != null) {
            close.eval(program.getPlayer(), program.getDataQuest());
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public DialogPack getDialog() {
        return dialog;
    }

    public void setDialog(DialogPack dialog) {
        this.dialog = dialog;
    }

    public DialogDisplay getDisplay() {
        return display;
    }

    public void setDisplay(DialogDisplay display) {
        this.display = display;
    }

    public QuestEffect getOpen() {
        return open;
    }

    public void setOpen(QuestEffect open) {
        this.open = open;
    }

    public QuestEffect getClose() {
        return close;
    }

    public void setClose(QuestEffect close) {
        this.close = close;
    }

    public ConfigurationSection getConfig() {
        return config;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "DialogGroup{" +
                "config=" + config +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", target='" + target + '\'' +
                ", dialog=" + dialog +
                ", display=" + display +
                ", open=" + open +
                ", close=" + close +
                '}';
    }
}
