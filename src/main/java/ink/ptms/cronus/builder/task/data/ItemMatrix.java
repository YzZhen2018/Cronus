package ink.ptms.cronus.builder.task.data;

import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.version.MaterialControl;
import me.skymc.taboolib.inventory.ItemUtils;
import me.skymc.taboolib.inventory.builder.ItemBuilder;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-06-30 16:00
 */
public class ItemMatrix extends Item {

    public ItemMatrix(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public org.bukkit.inventory.ItemStack getItem() {
        ink.ptms.cronus.internal.bukkit.ItemStack cronusItem = data == null ? null : BukkitParser.toItemStack(data);
        return new ItemBuilder(MaterialControl.APPLE.parseMaterial())
                .name("§7目标物品 (合成)")
                .lore(
                        "",
                        "§f" + (data == null ? "无" : (cronusItem.getBukkitItem() == null ? cronusItem.asString() : "bukkit:" + ItemUtils.getCustomName(cronusItem.getBukkitItem()))),
                        "§8§m                  ",
                        "§7物品导入: §8左键",
                        "§7模糊判断: §8右键"
                ).build();
    }

    @Override
    public String getKey() {
        return "matrix";
    }
}
