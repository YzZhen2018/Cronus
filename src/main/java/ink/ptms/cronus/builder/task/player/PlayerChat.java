package ink.ptms.cronus.builder.task.player;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.text.BookContent;
import ink.ptms.cronus.builder.task.data.text.BookPage;
import ink.ptms.cronus.builder.task.data.text.BookTitle;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.player.TaskPlayerChat;
import ink.ptms.cronus.internal.version.MaterialControl;
import ink.ptms.cronus.uranus.annotations.Auto;
import me.skymc.taboolib.inventory.builder.ItemBuilder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 坏黑
 * @Since 2019-06-22 22:19
 */
@Auto
public class PlayerChat extends TaskEntry {

    public PlayerChat() {
        objective.add(BookTitle.class);
        objective.add(BookContent.class);
        objective.add(BookPage.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.NAME_TAG.parseMaterial()).name("§f玩家聊天").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskPlayerChat.class;
    }
}
