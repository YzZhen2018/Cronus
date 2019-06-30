package ink.ptms.cronus.service.dialog.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.event.CronusDialogNextEvent;
import ink.ptms.cronus.internal.version.MaterialControl;
import ink.ptms.cronus.service.dialog.DialogDisplay;
import ink.ptms.cronus.service.dialog.DialogPack;
import me.skymc.taboolib.inventory.ItemUtils;
import me.skymc.taboolib.inventory.builder.v2.ClickType;
import me.skymc.taboolib.inventory.builder.v2.MenuBuilder;
import me.skymc.taboolib.javascript.ScriptHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.NumberConversions;

import javax.script.CompiledScript;
import javax.script.SimpleBindings;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-06-10 17:51
 */
public class DisplayMenu extends DialogDisplay {

    private CompiledScript rowsScript;
    private Integer slotMessage;
    private Integer[] slotReply;

    public DisplayMenu() {
        slotMessage = Cronus.getConf().getInt("Settings.dialog-chest.slot-message");
        slotReply = Cronus.getConf().getIntegerList("Settings.dialog-chest.slot-reply").toArray(new Integer[0]);
        rowsScript = ScriptHandler.compile(Cronus.getConf().getString("Settings.dialog-chest.rows-script"));
    }

    public int toRows(Player player, DialogPack dialogPack) {
        try {
            return NumberConversions.toInt(rowsScript.eval(new SimpleBindings(ImmutableMap.of("player", player, "plugin", Cronus.getInst(), "$size", dialogPack.getReply().size()))));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return 0;
    }

    @Override
    public void display(Player player, DialogPack dialogPack) {
        boolean[] closed = new boolean[1];
        Map<Integer, DialogPack> slots = Maps.newHashMap();
        Inventory inventory = MenuBuilder.builder(Cronus.getInst())
                .title(dialogPack.getParent().getTitle())
                .rows(toRows(player, dialogPack))
                .event(e -> {
                    if (e.getClickType() == ClickType.CLICK) {
                        e.castClick().setCancelled(true);
                        DialogPack reply = slots.get(e.castClick().getRawSlot());
                        if (reply != null) {
                            try {
                                CronusDialogNextEvent event = CronusDialogNextEvent.call(reply, e.getClicker());
                                if (event.isCancelled()) {
                                    return;
                                }
                                if (event.getPack().getDialog() != null) {
                                    closed[0] = true;
                                    display(e.getClicker(), event.getPack().getDialog());
                                } else if (event.getPack().getEffect() != null) {
                                    closed[0] = true;
                                    e.getClicker().closeInventory();
                                    event.getPack().effectEval(e.getClicker());
                                }
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }
                    }
                }).close(e -> {
                    if (!closed[0] && dialogPack.getParent().getClose() != null) {
                        dialogPack.getParent().closeEval((Player) e.getPlayer());
                    }
                    slots.clear();
                }).items().build();
        // 异步计算
        Bukkit.getScheduler().runTaskAsynchronously(Cronus.getInst(), () -> {
            // 对话
            inventory.setItem(slotMessage, toItem(MaterialControl.fromString(dialogPack.getConfig().getOrDefault("item", "WRITABLE_BOOK")), dialogPack.getText()));
            List<DialogPack> replies = Lists.newArrayList();
            // 条件
            for (int i = 0; i < slotReply.length && i < dialogPack.getReply().size(); i++) {
                DialogPack reply = dialogPack.getReply().get(i).getPack(player, null);
                // 忽略空气
                if (String.valueOf(reply.getConfig().get("item")).equalsIgnoreCase("air")) {
                    continue;
                }
                replies.add(reply);
            }
            // 物品
            for (int i = 0; i < slotReply.length && i < replies.size(); i++) {
                DialogPack reply = replies.get(i);
                inventory.setItem(slotReply[i], toItem(MaterialControl.fromString(reply.getConfig().getOrDefault("item", reply.isQuest() ? "MAP" : "PAPER")), reply.getText()));
                slots.put(slotReply[i], reply);
            }
            // 返回主线程打开菜单
            Bukkit.getScheduler().runTask(Cronus.getInst(), () -> player.openInventory(inventory));
        });
    }

    public ItemStack toItem(MaterialControl material, List<String> list) {
        ItemStack itemStack = material.parseItem();
        if (ItemUtils.isNull(itemStack)) {
            itemStack.setType(Material.STONE);
        }
        if (list.size() > 0) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(list.get(0));
            List<String> lore = Lists.newArrayList(list);
            lore.remove(0);
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    @Override
    public String toString() {
        return "DisplayMenu{" +
                "rowsScript=" + rowsScript +
                ", slotMessage=" + slotMessage +
                ", slotReply=" + Arrays.toString(slotReply) +
                '}';
    }
}
