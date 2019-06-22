package ink.ptms.cronus.builder.element;

import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.builder.Builders;
import ink.ptms.cronus.builder.task.TaskData;
import ink.ptms.cronus.internal.version.MaterialControl;
import me.skymc.taboolib.inventory.InventoryUtil;
import me.skymc.taboolib.inventory.builder.ItemBuilder;
import me.skymc.taboolib.inventory.builder.v2.ClickType;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-06-22 22:41
 */
public class BuilderTaskData {

    private BuilderTask builderTask;
    private Player player;
    private boolean toggle;
    private List<TaskData> dataInstance;
    private Map<Integer, TaskData> map = Maps.newHashMap();

    public BuilderTaskData(Player player, BuilderTask builderTask) {
        this.player = player;
        this.builderTask = builderTask;
        this.dataInstance = builderTask.getTypeEntry().getObjective().stream().map(o -> {
            try {
                return o.getConstructor(Player.class, BuilderTaskData.class).newInstance(player, this);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void open() {
        this.map.clear();
        this.toggle = false;
        // 创建界面
        Inventory inventory = Builders.normal("结构编辑 : 条目要求",
                e -> {
                    if (e.getClickType() == ClickType.CLICK) {
                        e.castClick().setCancelled(true);
                        if (e.castClick().getRawSlot() == 49) {
                            toggle = true;
                            builderTask.open(player);
                        } else if (map.containsKey(e.castClick().getRawSlot())) {
                            toggle = true;
                            TaskData taskData = map.get(e.castClick().getRawSlot());
                            taskData.onClick(e.castClick());
                            builderTask.getData().put(taskData.getKey(), taskData.getData());
                            e.castClick().setCurrentItem(taskData.getItem());
                        }
                    }
                },
                e -> {
                    if (!toggle) {
                        Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> {
                            builderTask.open(player);
                        }, 1);
                    }
                }
        );
        for (int i = 0; i < dataInstance.size(); i++) {
            TaskData taskData = dataInstance.get(i);
            taskData.setData(builderTask.getData().getOrDefault(taskData.getKey(), taskData.defaultValue()));
            inventory.setItem(InventoryUtil.SLOT_OF_CENTENTS.get(i), taskData.getItem());
            map.put(InventoryUtil.SLOT_OF_CENTENTS.get(i), taskData);
        }
        inventory.setItem(49, new ItemBuilder(MaterialControl.RED_STAINED_GLASS_PANE.parseItem()).name("§c上级目录").lore("", "§7点击").build());
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
    }

    public BuilderTask getBuilderTask() {
        return builderTask;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isToggle() {
        return toggle;
    }

    public List<TaskData> getDataInstance() {
        return dataInstance;
    }

    public Map<Integer, TaskData> getMap() {
        return map;
    }
}
