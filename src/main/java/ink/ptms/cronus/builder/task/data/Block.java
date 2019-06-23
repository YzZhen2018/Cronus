package ink.ptms.cronus.builder.task.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ilummc.tlib.util.Strings;
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
import me.skymc.taboolib.inventory.ItemUtils;
import me.skymc.taboolib.inventory.builder.ItemBuilder;
import me.skymc.taboolib.inventory.builder.v2.ClickType;
import me.skymc.taboolib.inventory.builder.v2.MenuBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-06-22 22:24
 */
public class Block extends TaskData {

    protected List<MaterialControl> selected = Lists.newArrayList();
    protected Map<Integer, MaterialControl> mapSelect = Maps.newHashMap();
    protected Map<Integer, MaterialControl> mapDelete = Maps.newHashMap();
    private boolean toggle;

    public Block(Player player, BuilderTaskData builderTaskData) {
        super(player, builderTaskData);
    }

    @Override
    public void setData(Object data) {
        this.data = data;
        this.selected = Lists.newArrayList();
        // 导入数据
        if (data == null || Strings.isEmpty((String) data)) {
            return;
        }
        ink.ptms.cronus.internal.bukkit.Block block = BukkitParser.toBlock(data);
        for (ink.ptms.cronus.internal.bukkit.Block.Point point : block.getPoints()) {
            selected.add(MaterialControl.matchMaterialControl(point.getName(), (byte) point.getData()));
        }
    }

    @Override
    public String getKey() {
        return "block";
    }

    @Override
    public ItemStack getItem() {
        List<String> lore = Lists.newArrayList("");
        if (selected.isEmpty()) {
            lore.add("§f无");
        }
        for (int i = 0; i < selected.size() && i < 8; i++) {
            lore.add("§f" + ItemUtils.getCustomName(selected.get(i).parseItem()));
        }
        if (selected.size() > 8) {
            lore.add("§f...");
        }
        lore.add("§8§m                  ");
        lore.add("§7选择: §8左键");
        lore.add("§7导入: §8右键");
        lore.add("§7删除: §8中键");
        return new ItemBuilder(MaterialControl.GRASS_BLOCK.parseMaterial())
                .name("§7方块类型")
                .lore(lore).build();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        // 删除
        if (e.getClick().isCreativeAction()) {
            openDelete(0);
        }
        // 选择
        else if (e.isLeftClick()) {
            openSelect(0);
        }
        // 导入
        else if (e.isRightClick()) {
            player.openInventory(MenuBuilder.builder(Cronus.getInst())
                    .title("实例选择 : 方块导入")
                    .rows(3)
                    .items()
                    .close(c -> {
                        int i = 0;
                        for (ItemStack itemStack : c.getInventory()) {
                            if (ItemUtils.isNull(itemStack) || !itemStack.getType().isBlock()) {
                                continue;
                            }
                            MaterialControl materialControl = MaterialControl.fromItem(itemStack);
                            if (materialControl != null && !selected.contains(materialControl)) {
                                selected.add(materialControl);
                                i++;
                            }
                        }
                        if (i > 0) {
                            player.sendMessage("§7§l[§f§lCronus§7§l] §7导入 §f" + i + " §7种方块.");
                        }
                        Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> builderTaskData.open(), 1);
                    }).build());
        }
    }

    @Override
    public Object defaultValue() {
        return null;
    }

    public void save() {
        saveData(selected.stream().map(s -> MaterialControl.isNewVersion() ? s.name() : s.name() + ":" + s.getData()).collect(Collectors.joining(",")));
    }

    public void openSelect(int page) {
        toggle = true;
        Inventory inventory = Builders.normal("实例选择 : 方块类型",
                c -> {
                    if (c.getClickType() == ClickType.CLICK) {
                        c.castClick().setCancelled(true);
                        if (c.castClick().getRawSlot() == 46 && MaterialControl.GREEN_STAINED_GLASS_PANE.isSimilar(c.castClick().getCurrentItem())) {
                            openSelect(page - 1);
                        } else if (c.castClick().getRawSlot() == 52 && MaterialControl.GREEN_STAINED_GLASS_PANE.isSimilar(c.castClick().getCurrentItem())) {
                            openSelect(page + 1);
                        } else if (c.castClick().getRawSlot() == 49) {
                            toggle = true;
                            save();
                            builderTaskData.open();
                        } else {
                            MaterialControl selectItem = mapSelect.get(c.castClick().getRawSlot());
                            if (selectItem != null) {
                                if (selected.contains(selectItem)) {
                                    selected.remove(selectItem);
                                } else {
                                    selected.add(selectItem);
                                }
                                openSelect(page);
                            }
                        }
                    }
                },
                c -> {
                    if (!toggle) {
                        Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> {
                            save();
                            builderTaskData.open();
                        }, 1);
                    }
                });
        List<ItemStack> iterator = new SimpleIterator(Cache.BLOCKS).listIterator(page * 28, (page + 1) * 28);
        for (int i = 0; i < iterator.size(); i++) {
            ItemStack parseItem = iterator.get(i).clone();
            try {
                MaterialControl fromItem = MaterialControl.fromItem(parseItem);
                if (selected.contains(fromItem)) {
                    inventory.setItem(InventoryUtil.SLOT_OF_CENTENTS.get(i), new ItemBuilder(parseItem).lore("", "§8取消").shiny().build());
                } else {
                    inventory.setItem(InventoryUtil.SLOT_OF_CENTENTS.get(i), new ItemBuilder(parseItem).lore("", "§8选择").build());
                }
                mapSelect.put(InventoryUtil.SLOT_OF_CENTENTS.get(i), fromItem);
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

    public void openDelete(int page) {
        toggle = true;
        Inventory inventory = Builders.normal("实例删除 : 方块类型",
                c -> {
                    if (c.getClickType() == ClickType.CLICK) {
                        c.castClick().setCancelled(true);
                        if (c.castClick().getRawSlot() == 46 && MaterialControl.GREEN_STAINED_GLASS_PANE.isSimilar(c.castClick().getCurrentItem())) {
                            openDelete(page - 1);
                        } else if (c.castClick().getRawSlot() == 52 && MaterialControl.GREEN_STAINED_GLASS_PANE.isSimilar(c.castClick().getCurrentItem())) {
                            openDelete(page + 1);
                        } else if (c.castClick().getRawSlot() == 49) {
                            toggle = true;
                            save();
                            builderTaskData.open();
                        } else {
                            MaterialControl material = mapDelete.get(c.castClick().getRawSlot());
                            if (material != null) {
                                selected.remove(material);
                                openDelete(page);
                            }
                        }
                    }
                },
                c -> {
                    if (!toggle) {
                        Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> {
                            save();
                            builderTaskData.open();
                        }, 1);
                    }
                });
        List<MaterialControl> iterator = new SimpleIterator(selected).listIterator(page * 28, (page + 1) * 28);
        for (int i = 0; i < iterator.size(); i++) {
            try {
                ItemStack parseItem = iterator.get(i).parseItem();
                inventory.setItem(InventoryUtil.SLOT_OF_CENTENTS.get(i), new ItemBuilder(parseItem).lore("", "§8删除").build());
                mapDelete.put(InventoryUtil.SLOT_OF_CENTENTS.get(i), iterator.get(i));
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        if (page > 0) {
            inventory.setItem(46, new ItemBuilder(MaterialControl.GREEN_STAINED_GLASS_PANE.parseItem()).name("§a上一页").lore("", "§7点击").build());
        }
        if (Utils.next(page, selected.size(), 28)) {
            inventory.setItem(52, new ItemBuilder(MaterialControl.GREEN_STAINED_GLASS_PANE.parseItem()).name("§a下一页").lore("", "§7点击").build());
        }
        inventory.setItem(49, new ItemBuilder(MaterialControl.RED_STAINED_GLASS_PANE.parseItem()).name("§c上级目录").lore("", "§7点击").build());
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        toggle = false;
    }
}
