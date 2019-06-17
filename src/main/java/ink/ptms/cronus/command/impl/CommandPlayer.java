package ink.ptms.cronus.command.impl;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.QuestBook;
import me.skymc.taboolib.commands.builder.SimpleCommandBuilder;
import me.skymc.taboolib.common.inject.TInject;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-06-09 23:57
 */
public class CommandPlayer {

    @TInject
    static SimpleCommandBuilder open = SimpleCommandBuilder.create("CronusQuestOpen", null)
            .execute((sender, args) -> {
                if (sender instanceof Player) {
                    DataQuest dataQuest = CronusAPI.getData((Player) sender).getQuest(args[0]);
                    if (dataQuest != null) {
                        dataQuest.open((Player) sender);
                    }
                }
                return true;
            });

    @TInject
    static SimpleCommandBuilder book = SimpleCommandBuilder.create("CronusQuestBook", null)
            .execute((sender, args) -> {
                if (sender instanceof Player) {
                    QuestBook questBook = Cronus.getCronusService().getRegisteredQuestBook().get(args[0]);
                    if (questBook != null) {
                        questBook.open((Player) sender);
                    }
                }
                return true;
            });

    @TInject
    static SimpleCommandBuilder quit = SimpleCommandBuilder.create("CronusQuestQuit", null)
            .execute((sender, args) -> {
                if (sender instanceof Player) {
                    DataPlayer dataPlayer = CronusAPI.getData((Player) sender);
                    DataQuest dataQuest = dataPlayer.getQuest(args[0]);
                    if (dataQuest != null) {
                        dataPlayer.failureQuest((Player) sender, dataQuest.getQuest());
                        dataPlayer.push();
                    }
                }
                return true;
            });

}
