package ink.ptms.cronus.builder.task.player;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Entity;
import ink.ptms.cronus.builder.task.data.count.CountMove;
import ink.ptms.cronus.builder.task.data.entity.EntityVehicle;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.player.TaskPlayerRide;
import ink.ptms.cronus.internal.task.player.TaskPlayerVehicle;
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
public class PlayerVehicle extends TaskEntry {

    public PlayerVehicle() {
        objective.add(CountMove.class);
        objective.add(EntityVehicle.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(MaterialControl.MINECART.parseMaterial()).name("§f乘坐矿车").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskPlayerVehicle.class;
    }
}
