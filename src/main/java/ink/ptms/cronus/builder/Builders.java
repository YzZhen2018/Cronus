package ink.ptms.cronus.builder;

import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.builder.element.BuilderQuest;
import ink.ptms.cronus.internal.version.MaterialControl;
import me.skymc.taboolib.common.configuration.TConfiguration;
import me.skymc.taboolib.common.inject.TInject;
import me.skymc.taboolib.inventory.builder.v2.*;
import org.bukkit.inventory.Inventory;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-06-18 20:09
 */
public class Builders {

    @TInject("builder.yml")
    private static TConfiguration conf;
    private static Map<String, BuilderQuest> quest = Maps.newHashMap();

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
}
