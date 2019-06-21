package ink.ptms.cronus.builder.element;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.builder.element.condition.MatchEntry;
import ink.ptms.cronus.command.CronusCommand;
import ink.ptms.cronus.database.data.time.Time;
import ink.ptms.cronus.database.data.time.TimeType;
import ink.ptms.cronus.internal.version.MaterialControl;
import ink.ptms.cronus.uranus.program.ProgramLoader;
import ink.ptms.cronus.util.StringDate;
import ink.ptms.cronus.util.Utils;
import me.skymc.taboolib.inventory.builder.ItemBuilder;
import me.skymc.taboolib.inventory.builder.v2.ClickType;
import me.skymc.taboolib.inventory.builder.v2.MenuBuilder;
import me.skymc.taboolib.json.tellraw.TellrawJson;
import me.skymc.taboolib.message.ChatCatcher;
import me.skymc.taboolib.timeutil.TimeFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-06-18 19:50
 */
public class BuilderQuest extends CronusCommand {

    private String id;
    private String display;
    private List<String> bookTag = Lists.newArrayList();
    private String label;
    private String cooldown;
    private String timeout;
    private List<String> actionAccept = Lists.newArrayList();
    private List<String> actionAcceptFail = Lists.newArrayList();
    private List<String> actionSuccess = Lists.newArrayList();
    private List<String> actionFailure = Lists.newArrayList();
    private List<String> actionCooldown = Lists.newArrayList();
    private MatchEntry conditionAccept;
    private MatchEntry conditionFailure;
    private BuilderStageList stageList;

    public BuilderQuest(String id) {
        this.id = id;
    }

    public void open(Player player) {
        if (stageList == null) {
            stageList = new BuilderStageList();
        }
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
                .put('1', new ItemBuilder(Material.NAME_TAG)
                        .name("§b任务类型")
                        .lore(toLore(bookTag))
                        .build())
                .put('2', new ItemBuilder(Material.NAME_TAG)
                        .name("§b任务标签")
                        .lore("", (label == null ? "§f无" : "§f" + label))
                        .build())
                .put('3', new ItemBuilder(Material.WATCH)
                        .name("§b任务冷却时间")
                        .lore("", "§f" + (cooldown == null ? "无" : displayCooldown()))
                        .build())
                .put('4', new ItemBuilder(Material.WATCH)
                        .name("§b任务超时时间")
                        .lore("", "§f" + (timeout == null ? "无" : displayTimeout()))
                        .build())
                .put('5', new ItemBuilder(Material.DIODE)
                        .name("§b任务接受动作")
                        .lore(toLore(actionAccept))
                        .build())
                .put('6', new ItemBuilder(Material.DIODE)
                        .name("§b任务接受动作 (条件不足)")
                        .lore(toLore(actionAcceptFail))
                        .build())
                .put('7', new ItemBuilder(Material.DIODE)
                        .name("§b任务完成动作")
                        .lore(toLore(actionSuccess))
                        .build())
                .put('8', new ItemBuilder(Material.DIODE)
                        .name("§b任务失败动作")
                        .lore(toLore(actionFailure))
                        .build())
                .put('9', new ItemBuilder(Material.DIODE)
                        .name("§b任务冷却动作")
                        .lore(toLore(actionCooldown))
                        .build())
                .put('A', new ItemBuilder(Material.TRIPWIRE_HOOK)
                        .name("§b任务接受条件")
                        .lore(toLore(conditionAccept == null ? Lists.newArrayList() : conditionAccept.asList(0)))
                        .build())
                .put('B', new ItemBuilder(Material.TRIPWIRE_HOOK)
                        .name("§b任务失败条件")
                        .lore(toLore(conditionFailure == null ? Lists.newArrayList() : conditionFailure.asList(0)))
                        .build())
                .put('C', new ItemBuilder(Material.BOOK)
                        .name("§b任务阶段")
                        .lore(toLore(stageList.getStages().stream().map(BuilderStage::getId).collect(Collectors.toList())))
                        .build())
                .put('%', new ItemBuilder(Material.BOOK_AND_QUILL)
                        .name("§a保存配置")
                        .lore("", "§7文件位置", "§8§nplugins/Cronus/quests/builder/" + id + ".yml")
                        .build())
                .items(
                        "#########",
                        "$01234  $",
                        "$56789  $",
                        "$AB     $",
                        "$   C   $",
                        "####%####")
                .event(e -> {
                    if (e.getClickType() == ClickType.CLICK) {
                        e.castClick().setCancelled(true);
                        switch (e.castClick().getRawSlot()) {
                            case 10:
                                editString(e.getClicker(), "任务名称", display, r -> display = r);
                                break;
                            case 11:
                                new BuilderList("任务索引", bookTag).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getBookTag);
                                break;
                            case 12:
                                editString(e.getClicker(), "任务标签", label, r -> label = r);
                                break;
                            case 13:
                                editString(e.getClicker(), "任务冷却时间", cooldown, r -> cooldown = r);
                                normal(e.getClicker(), "可用：");
                                TellrawJson.create()
                                        .append("§7§l[§f§lCronus§7§l] §7- ")
                                        .append(formatTime("[number][time]"))
                                        .hoverText(String.join("\n", Lists.newArrayList(
                                                "§7延迟冷却",
                                                "§7任务完成或失败后一段时间内无法再次接受",
                                                "",
                                                "§7示范:",
                                                "§f1h §8(1小时后)",
                                                "§f1h30m §8(1小时30分钟后)",
                                                "",
                                                "§7单位:",
                                                "§fd §8(天)",
                                                "§fh §8(时)",
                                                "§fm §8(分)",
                                                "§fs §8(秒)"
                                        ))).clickSuggest("[number][time]").send(e.getClicker());
                                break;
                            case 14:
                                editString(e.getClicker(), "任务超时时间", timeout, r -> timeout = r);
                                normal(e.getClicker(), "可用：");
                                TellrawJson.create()
                                        .append("§7§l[§f§lCronus§7§l] §7- ")
                                        .append(formatTime("[number][time]"))
                                        .hoverText(String.join("\n", Lists.newArrayList(
                                                "§7延迟刷新",
                                                "§7任务接受起一段时间后超时",
                                                "",
                                                "§7示范:",
                                                "§f1h §8(1小时后)",
                                                "§f1h30m §8(1小时30分钟后)",
                                                "",
                                                "§7单位:",
                                                "§fd §8(天)",
                                                "§fh §8(时)",
                                                "§fm §8(分)",
                                                "§fs §8(秒)"
                                        ))).clickSuggest("[number][time]").send(e.getClicker());
                                TellrawJson.create()
                                        .append("§7§l[§f§lCronus§7§l] §7- ")
                                        .append(formatTime("day:[hour]:[minute]"))
                                        .hoverText(String.join("\n", Lists.newArrayList(
                                                "§7每日刷新",
                                                "§7任务接受起每日固定时间超时",
                                                "",
                                                "§7示范:",
                                                "§fday:23:59 §8(每天 23时59分)",
                                                "§fday:00:00 §8(每天 00时00分)"
                                        ))).clickSuggest("day:[hour]:[minute]").send(e.getClicker());
                                TellrawJson.create()
                                        .append("§7§l[§f§lCronus§7§l] §7- ")
                                        .append(formatTime("week:[day]:[hour]:[minute]"))
                                        .hoverText(String.join("\n", Lists.newArrayList(
                                                "§7每周刷新",
                                                "§7任务接受起每周固定日期超时",
                                                "",
                                                "§7示范:",
                                                "§fweek:0:23:59 §8(每周日 23时59分)",
                                                "§fweek:6:00:00 §8(每周六 00时00分)",
                                                "",
                                                "§7单位:",
                                                "§f0 §8(周日)",
                                                "§f1 §8(周一)",
                                                "§f2 §8(周二)",
                                                "§f3 §8(周三)",
                                                "§f4 §8(周四)",
                                                "§f5 §8(周五)",
                                                "§f6 §8(周六)"
                                        ))).clickSuggest("week:[day]:[hour]:[minute]").send(e.getClicker());
                                TellrawJson.create()
                                        .append("§7§l[§f§lCronus§7§l] §7- ")
                                        .append(formatTime("month:[day]:[hour]:[minute]"))
                                        .hoverText(String.join("\n", Lists.newArrayList(
                                                "§7每月刷新",
                                                "§7任务接受起每月固定日期超时",
                                                "",
                                                "§7示范:",
                                                "§fmonth:1:23:59 §8(每月1日 23时59分)",
                                                "§fmonth:1:00:00 §8(每月1日 00时00分)"
                                        ))).clickSuggest("month:[day]:[hour]:[minute]").send(e.getClicker());
                                break;
                            case 19:
                                new BuilderListEffect("任务接受动作", actionAccept).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 20:
                                new BuilderListEffect("任务接受动作 (条件不足)", actionAcceptFail).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 21:
                                new BuilderListEffect("任务完成动作", actionSuccess).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 22:
                                new BuilderListEffect("任务失败动作", actionFailure).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 23:
                                new BuilderListEffect("任务冷却动作", actionCooldown).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 28: {
                                BuilderCondition condition = new BuilderCondition(conditionAccept, e.getClicker(), "任务接受条件");
                                condition.setCloseTask(c -> {
                                    conditionAccept = condition.getEntry();
                                    Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> open(e.getClicker()), 1);
                                });
                                condition.open(player, 0);
                                break;
                            }
                            case 29: {
                                BuilderCondition condition = new BuilderCondition(conditionFailure, e.getClicker(), "任务失败条件");
                                condition.setCloseTask(c -> {
                                    conditionFailure = condition.getEntry();
                                    Bukkit.getScheduler().runTaskLater(Cronus.getInst(), () -> open(e.getClicker()), 1);
                                });
                                condition.open(player, 0);
                                break;
                            }
                            case 40:
                                stageList.open(e.getClicker(), 0, c -> open(e.getClicker()), Maps::newHashMap);
                                break;
                        }
                    }
                }).build());
    }

    protected String formatTime(String example) {
        return "§8" + example.replaceAll("[.:]", "§f$0§8").replaceAll("\\[(.+?)]", "§8[§7$1§8]§8");
    }

    protected String formatEffect(String example) {
        return "§7" + example.replaceAll("\\.", "§8$0§7").replaceAll("\\[(\\S+)]", "§e[§6$1§e]§7");
    }

    protected Map<String, String> getBookTag() {
        return Cronus.getCronusService().getRegisteredQuest().entrySet().stream().flatMap(entry -> entry.getValue().getBookTag().stream()).collect(Collectors.toMap(k -> k, k -> k, (a, b) -> b, Maps::newTreeMap));
    }

    protected Map<String, String> getEffect() {
        return ProgramLoader.getEffects().stream().collect(Collectors.toMap(effect -> effect.getClass().getSimpleName().substring("Effect".length()), e -> formatEffect(e.getExample()), (a, b) -> b, Maps::newTreeMap));
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

    protected List<String> toLore(List<String> list) {
        List<String> array = Lists.newArrayList("");
        array.addAll(list.isEmpty() ? Lists.newArrayList("§f无") : list.stream().map(k -> "§f" + k).collect(Collectors.toList()));
        boolean more = false;
        while (array.size() > 8) {
            array.remove(8);
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
                TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7在对话框中输入新的" + display + ". ")
                        .append("§8(取消)").hoverText("§7点击").clickCommand("quit()")
                        .send(player);
                TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7当前: ")
                        .append("§f" + Utils.NonNull(origin)).hoverText("§7点击").clickSuggest(Utils.NonNull(origin))
                        .send(player);
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

    interface EditTask {

        void run(String in);
    }
}
