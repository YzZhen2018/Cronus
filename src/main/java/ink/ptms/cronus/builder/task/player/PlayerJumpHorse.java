package ink.ptms.cronus.builder.task.player;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.builder.task.data.expression.ExpressionPower;
import ink.ptms.cronus.builder.task.data.entity.EntityHorse;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.player.TaskPlayerJumpHorse;
import ink.ptms.cronus.internal.version.MaterialControl;
import ink.ptms.cronus.uranus.annotations.Auto;
import io.izzel.taboolib.util.item.ItemBuilder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 坏黑
 * @Since 2019-06-22 22:19
 */
@Auto
public class PlayerJumpHorse extends TaskEntry {

    public PlayerJumpHorse() {
        objective.add(Count.class);
        objective.add(ExpressionPower.class);
        objective.add(EntityHorse.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.IRON_HORSE_ARMOR.parseMaterial()).name("§f骑乘跳跃").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskPlayerJumpHorse.class;
    }
}
