package ink.ptms.cronus.builder.element;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.builder.Builders;
import ink.ptms.cronus.internal.version.MaterialControl;
import ink.ptms.cronus.util.Utils;
import me.skymc.taboolib.common.util.SimpleIterator;
import me.skymc.taboolib.inventory.InventoryUtil;
import me.skymc.taboolib.inventory.ItemUtils;
import me.skymc.taboolib.inventory.builder.ItemBuilder;
import me.skymc.taboolib.inventory.builder.v2.ClickType;
import me.skymc.taboolib.inventory.builder.v2.CloseTask;
import me.skymc.taboolib.message.ChatCatcher;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-06-19 0:16
 */
public class BuilderList extends BuilderQuest {

    private String display;
    private List<String> list;
    private List<String> listOrigin;
    private CloseTask close;
    private int page;
    private boolean toggle;
    private boolean append;
    private Map<Integer, Integer> map = Maps.newHashMap();

    public BuilderList(String display, List<String> list) {
        super(null);
        this.display = display;
        this.listOrigin = list;
        this.list = Lists.newArrayList(list);
    }

    public void open(Player player, int page, CloseTask close) {
        this.map.clear();
        this.close = close;
        this.toggle = true;
        this.append = this.list.contains("$append") || this.list.add("$append");
        Inventory inventory = Builders.normal("结构编辑 : " + display, e -> {
            if (e.getClickType() == ClickType.CLICK && !ItemUtils.isNull(e.castClick().getCurrentItem())) {
                e.castClick().setCancelled(true);
                // 上一页
                if (e.castClick().getRawSlot() == 45 && e.castClick().getCurrentItem().equals(MaterialControl.GREEN_STAINED_GLASS_PANE.parseItem())) {
                    open(player, page - 1, close);
                }
                // 下一页
                else if (e.castClick().getRawSlot() == 53 && e.castClick().getCurrentItem().equals(MaterialControl.GREEN_STAINED_GLASS_PANE.parseItem())) {
                    open(player, page + 1, close);
                }
                // 内容
                else if (InventoryUtil.SLOT_OF_CENTENTS.contains(e.castClick().getRawSlot())) {
                    try {
                        int index = map.get(e.castClick().getRawSlot());
                        // 左键
                        if (e.castClick().getClick().isLeftClick()) {
                            editString(e.getClicker(), display, list.get(index).equals("$append") ? "-" : list.get(index), r -> list.set(index, r));
                        }
                        // 右键
                        else if (e.castClick().getClick().isRightClick()) {
                            list.remove(index);
                            open(player, page, close);
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }, c -> {
            if (!toggle) {
                Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> {
                    listOrigin.clear();
                    list.stream().filter(l -> !l.equals("$append")).forEach(listOrigin::add);
                    close.run(c);
                }, 1);
            }
        });
        List<String> list = new SimpleIterator(this.list).listIterator(page * 28, (page + 1) * 28);
        for (int i = 0; i < list.size(); i++) {
            // 追加
            if (list.get(i).equals("$append")) {
                inventory.setItem(InventoryUtil.SLOT_OF_CENTENTS.get(i), new ItemBuilder(Material.MAP)
                        .name("§f增加新的" + display)
                        .lore("", "§7点击")
                        .build());
            }
            // 修改 & 删除
            else {
                inventory.setItem(InventoryUtil.SLOT_OF_CENTENTS.get(i), new ItemBuilder(Material.PAPER)
                        .name("§f" + list.get(i))
                        .lore("", "§7左键修改 §8| §7右键删除")
                        .build());
            }
            map.put(InventoryUtil.SLOT_OF_CENTENTS.get(i), i);
        }
        if (page > 1) {
            inventory.setItem(45, new ItemBuilder(MaterialControl.GREEN_STAINED_GLASS_PANE.parseItem()).name("§7上一页").lore("", "§7点击").build());
        }
        if ((int) Math.ceil((double) this.listOrigin.size() / 28.0D) > page) {
            inventory.setItem(53, new ItemBuilder(MaterialControl.GREEN_STAINED_GLASS_PANE.parseItem()).name("§7下一页").lore("", "§7点击").build());
        }
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        this.toggle = false;
    }

    @Override
    protected void editString(Player player, String display, String origin, EditTask edit) {
        ChatCatcher.call(player, new ChatCatcher.Catcher() {
            @Override
            public ChatCatcher.Catcher before() {
                toggle = true;
                player.closeInventory();
                normal(player, "在对话框中输入新的" + display + ".");
                normal(player, "当前: §f" + Utils.NonNull(origin));
                return this;
            }

            @Override
            public boolean after(String s) {
                edit.run(s);
                open(player, page, close);
                return false;
            }

            @Override
            public void cancel() {
                open(player, page, close);
            }
        });
    }
}
