package ink.ptms.cronus.builder.task.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.builder.Builders;
import ink.ptms.cronus.builder.element.BuilderTaskData;
import ink.ptms.cronus.builder.task.Cache;
import ink.ptms.cronus.builder.task.TaskData;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.version.MaterialControl;
import ink.ptms.cronus.util.Utils;
import me.skymc.taboolib.common.util.SimpleIterator;
import me.skymc.taboolib.inventory.InventoryUtil;
import me.skymc.taboolib.inventory.builder.ItemBuilder;
import me.skymc.taboolib.inventory.builder.v2.ClickType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-06-22 22:24
 */
public class Block extends TaskData {

    private List<MaterialControl> selected = Lists.newArrayList();
    private Map<Integer, ItemStack> map = Maps.newHashMap();
    private boolean toggle;

    public Block(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public String getKey() {
        return "block";
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.GRASS)
                .name("§7方块类型")
                .lore(
                        "",
                        "§f" + (data == null ? "无" : data)
                ).build();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        open(0);
    }

    @Override
    public Object defaultValue() {
        return null;
    }

    public void open(int page) {
        toggle = true;
        Inventory inventory = Builders.normal("实例选择 : 方块类型",
                c -> {
                    if (c.getClickType() == ClickType.CLICK) {
                        c.castClick().setCancelled(true);
                        if (c.castClick().getRawSlot() == 46 && MaterialControl.GREEN_STAINED_GLASS_PANE.isSameMaterial(c.castClick().getCurrentItem())) {
                            open(page - 1);
                        } else if (c.castClick().getRawSlot() == 52 && MaterialControl.GREEN_STAINED_GLASS_PANE.isSameMaterial(c.castClick().getCurrentItem())) {
                            open(page + 1);
                        } else if (c.castClick().getRawSlot() == 49) {
                            toggle = true;
                            builderTaskData.open();
                        }
                    }
                },
                c -> {
                    if (!toggle) {
                        Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> {
                            builderTaskData.open();
                        }, 1);
                    }
                });
        ink.ptms.cronus.internal.bukkit.Block block = BukkitParser.toBlock(data);
        List<ItemStack> iterator = new SimpleIterator(Cache.BLOCKS).listIterator(page * 28, (page + 1) * 28);
        for (int i = 0; i < iterator.size(); i++) {
            ItemStack parseItem = iterator.get(i).clone();
            try {
                System.out.println(i + ": " + parseItem.getType());
                if (block.isSelect(parseItem)) {
                    inventory.setItem(InventoryUtil.SLOT_OF_CENTENTS.get(i), new ItemBuilder(parseItem).lore("", "§8取消").shiny().build());
                } else {
                    inventory.setItem(InventoryUtil.SLOT_OF_CENTENTS.get(i), new ItemBuilder(parseItem).lore("", "§8选择").build());
                }
                map.put(InventoryUtil.SLOT_OF_CENTENTS.get(i), parseItem);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        if (page > 0) {
            inventory.setItem(46, new ItemBuilder(MaterialControl.GREEN_STAINED_GLASS_PANE.parseItem()).name("§a上一页").lore("", "§7点击").build());
        }
        if (Utils.next(page, Cache.BLOCKS.size(), 28)) {
            inventory.setItem(52, new ItemBuilder(MaterialControl.GREEN_STAINED_GLASS_PANE.parseItem()).name("§a下一页").lore("", "§7点击").build());
        }
        inventory.setItem(49, new ItemBuilder(MaterialControl.RED_STAINED_GLASS_PANE.parseItem()).name("§c上级目录").lore("", "§7点击").build());
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        toggle = false;
    }
}
