package ink.ptms.cronus.builder.element;

import com.google.common.collect.Lists;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.command.CronusCommand;
import ink.ptms.cronus.database.data.time.Time;
import ink.ptms.cronus.database.data.time.TimeType;
import ink.ptms.cronus.internal.version.MaterialControl;
import ink.ptms.cronus.util.StringDate;
import ink.ptms.cronus.util.Utils;
import me.skymc.taboolib.inventory.builder.ItemBuilder;
import me.skymc.taboolib.inventory.builder.v2.ClickType;
import me.skymc.taboolib.inventory.builder.v2.MenuBuilder;
import me.skymc.taboolib.message.ChatCatcher;
import me.skymc.taboolib.timeutil.TimeFormatter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-06-18 19:50
 */
public class BuilderQuest extends CronusCommand {

    private String id;
    private String display;
    private List<String> keyword = Lists.newArrayList();
    private String label;
    private String cooldown;
    private String timeout;
    private List<String> actionAccept = Lists.newArrayList();
    private List<String> actionSuccess = Lists.newArrayList();
    private List<String> actionFailure = Lists.newArrayList();

    public BuilderQuest(String id) {
        this.id = id;
    }

    public void open(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        player.openInventory(MenuBuilder.builder(Cronus.getInst())
                .title("任务构建 : " + id)
                .rows(6)
                .put('#', MaterialControl.BLACK_STAINED_GLASS_PANE.parseItem())
                .put('$', MaterialControl.BLUE_STAINED_GLASS_PANE.parseItem())
                .put('0', new ItemBuilder(Material.NAME_TAG)
                        .name("§b任务名称")
                        .lore("", (display == null ? "§f无" : "§f" + display))
                        .build())
                .put('4', new ItemBuilder(Material.NAME_TAG)
                        .name("§b任务类型")
                        .lore(toArray(keyword))
                        .build())
                .put('7', new ItemBuilder(Material.NAME_TAG)
                        .name("§b任务标签")
                        .lore("", (label == null ? "§f无" : "§f" + label))
                        .build())
                .put('1', new ItemBuilder(Material.WATCH)
                        .name("§b任务冷却时间")
                        .lore("", "§f" + (cooldown == null ? "无" : displayCooldown()))
                        .build())
                .put('5', new ItemBuilder(Material.WATCH)
                        .name("§b任务超时时间")
                        .lore("", "§f" + (timeout == null ? "无" : displayTimeout()))
                        .build())
                .put('2', new ItemBuilder(Material.DIODE)
                        .name("§b任务接受动作")
                        .lore(toArray(actionAccept))
                        .build())
                .put('6', new ItemBuilder(Material.DIODE)
                        .name("§b任务完成动作")
                        .lore(toArray(actionSuccess))
                        .build())
                .put('8', new ItemBuilder(Material.DIODE)
                        .name("§b任务失败动作")
                        .lore(toArray(actionFailure))
                        .build())
                .put('%', new ItemBuilder(Material.BOOK_AND_QUILL)
                        .name("§a导出任务配置")
                        .lore("", "§7文件位置", "§8§nplugins/Cronus/quests/builder/" + id + ".yml")
                        .build())
                .items(
                        "#########",
                        "$0123   $",
                        "$456    $",
                        "$7 8    $",
                        "$       $",
                        "####%####")
                .event(e -> {
                    if (e.getClickType() == ClickType.CLICK) {
                        e.castClick().setCancelled(true);
                        switch (e.castClick().getRawSlot()) {
                            case 10:
                                editString(e.getClicker(), "任务名称", display, r -> display = r);
                                break;
                            case 28:
                                editString(e.getClicker(), "任务标签", label, r -> label = r);
                                break;
                            case 11:
                                editString(e.getClicker(), "任务冷却时间", cooldown, r -> cooldown = r);
                                break;
                            case 20:
                                editString(e.getClicker(), "任务超时时间", timeout, r -> timeout = r);
                                break;
                            case 19:
                                new BuilderList("任务类型", keyword).open(e.getClicker(), 0, c -> open(e.getClicker()));
                                break;
                            case 12:
                                new BuilderList("任务接受动作", actionAccept).open(e.getClicker(), 0, c -> open(e.getClicker()));
                                break;
                            case 21:
                                new BuilderList("任务完成动作", actionSuccess).open(e.getClicker(), 0, c -> open(e.getClicker()));
                                break;
                            case 30:
                                new BuilderList("任务失败动作", actionFailure).open(e.getClicker(), 0, c -> open(e.getClicker()));
                                break;
                        }
                    }
                }).build());
    }

    protected List<String> toArray(List<String> list) {
        List<String> array = Lists.newArrayList("");
        array.addAll(list.isEmpty() ? Lists.newArrayList("§f无") : list.stream().map(k -> "§f" + k).collect(Collectors.toList()));
        boolean more = false;
        while (array.size() > 5) {
            array.remove(5);
            more = true;
        }
        if (more) {
            array.add("§f...");
        }
        return array;
    }

    protected void editString(Player player, String display, String origin, EditTask edit) {
        ChatCatcher.call(player, new ChatCatcher.Catcher() {
            @Override
            public ChatCatcher.Catcher before() {
                player.closeInventory();
                normal(player, "在对话框中输入新的" + display + ".");
                normal(player, "当前: §f" + Utils.NonNull(origin));
                return this;
            }

            @Override
            public boolean after(String s) {
                edit.run(s);
                open(player);
                return false;
            }

            @Override
            public void cancel() {
                open(player);
            }
        });
    }

    protected String displayCooldown() {
        return cooldown.equalsIgnoreCase("never") || cooldown.equals("-1") ? "永不" : getTimeDisplay(StringDate.parse(cooldown));
    }

    protected String displayTimeout() {
        Time parse = Time.parse(timeout);
        if (parse == null) {
            return "永不";
        } else if (parse.getType() == TimeType.DAY) {
            return "每天 " + parse.getHour() + "时" + parse.getMinute() + "分";
        } else if (parse.getType() == TimeType.WEEK) {
            return "每周" + getWeekDisplay(parse.getDay()) + " " + parse.getHour() + "时" + parse.getMinute() + "分";
        } else if (parse.getType() == TimeType.MONTH) {
            return "每月" + parse.getDay() + "日 " + parse.getHour() + "时" + parse.getMinute() + "分";
        } else {
            return getTimeDisplay(parse.getTime());
        }
    }

    protected String getTimeDisplay(long in) {
        TimeFormatter t = new TimeFormatter(in);
        String time = (t.getDays() > 0 ? t.getDays() + "天" : "") + (t.getHours() > 0 ? t.getHours() + "时" : "") + (t.getMinutes() > 0 ? t.getMinutes() + "分" : "") + (t.getSeconds() > 0 ? t.getSeconds() + "秒" : "");
        return time.isEmpty() ? "无" : time;
    }

    protected String getWeekDisplay(int day) {
        switch (day) {
            case 0:
                return "日";
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            default:
                return "八";
        }
    }

    interface EditTask {

        void run(String in);
    }
}
