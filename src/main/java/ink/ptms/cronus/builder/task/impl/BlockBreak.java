package ink.ptms.cronus.builder.task.impl;

import ink.ptms.cronus.builder.task.TaskEntry;
import ink.ptms.cronus.builder.task.data.Block;
import ink.ptms.cronus.builder.task.data.Count;
import ink.ptms.cronus.internal.QuestTask;
import ink.ptms.cronus.internal.task.block.TaskBlockBreak;
import ink.ptms.cronus.uranus.annotations.Auto;
import me.skymc.taboolib.inventory.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 坏黑
 * @Since 2019-06-22 22:19
 */
@Auto
public class BlockBreak extends TaskEntry {

    public BlockBreak() {
        objective.add(Count.class);
        objective.add(Block.class);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.WOOD_PICKAXE).name("§f方块破坏").lore("", "§7点击选择").flags(ItemFlag.values()).build();
    }

    @Override
    public Class<? extends QuestTask> getTask() {
        return TaskBlockBreak.class;
    }
}
