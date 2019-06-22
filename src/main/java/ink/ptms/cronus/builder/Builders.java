package ink.ptms.cronus.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.builder.element.BuilderQuest;
import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.internal.version.MaterialControl;
import ink.ptms.cronus.uranus.annotations.Auto;
import me.skymc.taboolib.TabooLibLoader;
import me.skymc.taboolib.common.configuration.TConfiguration;
import me.skymc.taboolib.common.function.TFunction;
import me.skymc.taboolib.common.inject.TInject;
import me.skymc.taboolib.inventory.builder.v2.ClickTask;
import me.skymc.taboolib.inventory.builder.v2.CloseTask;
import me.skymc.taboolib.inventory.builder.v2.MenuBuilder;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-06-18 20:09
 */
@TFunction(enable = "init")
public class Builders {

    @TInject("builder.yml")
    private static TConfiguration conf;
    private static Map<String, BuilderQuest> quest = Maps.newHashMap();
    private static List<TaskEntry> taskEntries = Lists.newArrayList();

    static void init() {
        TabooLibLoader.getPluginClasses(Cronus.getInst()).ifPresent(classes -> {
            for (Class pClass : classes) {
                // task
                if (pClass.isAnnotationPresent(Auto.class) && TaskEntry.class.isAssignableFrom(pClass)) {
                    try {
                        taskEntries.add((TaskEntry) pClass.newInstance());
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }

    public static Inventory normal(String title, ClickTask click, CloseTask close) {
        return MenuBuilder.builder(Cronus.getInst())
                .title(title)
                .rows(6)
                .put('#', MaterialControl.BLACK_STAINED_GLASS_PANE.parseItem())
                .put('$', MaterialControl.BLUE_STAINED_GLASS_PANE.parseItem())
                .items(
                        "#########",
                        "$       $",
                        "$       $",
                        "$       $",
                        "$       $",
                        "#########")
                .event(click)
                .close(close)
                .build();
    }

    // *********************************
    //
    //        Getter and Setter
    //
    // *********************************

    public static TConfiguration getConf() {
        return conf;
    }

    public static Map<String, BuilderQuest> getQuest() {
        return quest;
    }

    public static List<TaskEntry> getTaskEntries() {
        return taskEntries;
    }
}
