package ink.ptms.cronus.command.impl;

import com.google.common.collect.Lists;
import com.ilummc.tlib.bungee.chat.ComponentSerializer;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.CronusMirror;
import ink.ptms.cronus.command.CronusCommand;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.event.CronusQuestStopEvent;
import ink.ptms.cronus.internal.Quest;
import ink.ptms.cronus.internal.QuestBook;
import ink.ptms.cronus.internal.program.Action;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.util.Utils;
import me.skymc.taboolib.bookformatter.BookFormatter;
import me.skymc.taboolib.bookformatter.builder.BookBuilder;
import me.skymc.taboolib.bookformatter.builder.PageBuilder;
import me.skymc.taboolib.commands.internal.BaseSubCommand;
import me.skymc.taboolib.commands.internal.TCommand;
import me.skymc.taboolib.commands.internal.type.CommandArgument;
import me.skymc.taboolib.commands.internal.type.CommandRegister;
import me.skymc.taboolib.commands.internal.type.CommandType;
import me.skymc.taboolib.json.tellraw.TellrawJson;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-05-29 17:25
 */
@TCommand(name = "Cronus", aliases = {"CronusQuest", "cq"}, permission = "*")
public class Command extends CronusCommand {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private List<String> getQuests() {
        List<String> label = Lists.newArrayList();
        for (Quest quest : Cronus.getCronusService().getRegisteredQuest().values()) {
            label.add(quest.getId());
            label.add(quest.getLabel());
        }
        return label;
    }

    @CommandRegister
    BaseSubCommand info = new BaseSubCommand() {

        @Override
        public CommandArgument[] getArguments() {
            return new CommandArgument[] {
                    new CommandArgument("玩家")
            };
        }

        @Override
        public String getDescription() {
            return "查看玩家任务信息.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &f" + args[0] + " &c离线.");
                return;
            }
            DataPlayer playerData = CronusAPI.getData(player);
            normal(sender, "玩家 &f" + player.getName() + " &7的任务信息:");
            normal(sender, "全局数据:");
            for (String line : Utils.NonNull(playerData.getDataGlobal().saveToString()).split("\n")) {
                normal(sender, "§f" + line);
            }
            normal(sender, "临时数据:");
            for (String line : Utils.NonNull(playerData.getDataTemp().saveToString()).split("\n")) {
                normal(sender, "§f" + line);
            }
            normal(sender, "任务数据:");
            for (Map.Entry<String, DataQuest> questEntry : playerData.getQuest().entrySet()) {
                normal(sender, "  §f" + questEntry.getKey() + ":");
                normal(sender, "    任务数据:");
                for (String line : Utils.NonNull(questEntry.getValue().getDataQuest().saveToString()).split("\n")) {
                    normal(sender, "    §f" + line);
                }
                normal(sender, "    阶段数据:");
                for (String line : Utils.NonNull(questEntry.getValue().getDataStage().saveToString()).split("\n")) {
                    normal(sender, "    §f" + line);
                }
                normal(sender, "    当前阶段: §f" + questEntry.getValue().getCurrentStage());
                normal(sender, "    开始时间: §f" + dateFormat.format(questEntry.getValue().getTimeStart()));
                normal(sender, "    结束时间: §f" + (playerData.isQuestCompleted(questEntry.getKey()) ? dateFormat.format(playerData.getQuestCompleted().get(questEntry.getKey())) : "-"));
            }
            if (playerData.getQuest().isEmpty()) {
                normal(sender, "§f-");
            }
            normal(sender, "完成时间:");
            for (Map.Entry<String, Long> entry : playerData.getQuestCompleted().entrySet()) {
                normal(sender, "  " + entry.getKey() + ": §f" + (entry.getValue() == 0 ? "-" : dateFormat.format(entry.getValue())));
            }
            if (playerData.getQuestCompleted().isEmpty()) {
                normal(sender, "§f-");
            }
        }
    };

    @CommandRegister
    BaseSubCommand accept = new BaseSubCommand() {

        @Override
        public CommandArgument[] getArguments() {
            return new CommandArgument[] {
                    new CommandArgument("玩家"), new CommandArgument("任务", () -> Lists.newArrayList(Cronus.getCronusService().getRegisteredQuest().keySet()))
            };
        }

        @Override
        public String getDescription() {
            return "使玩家接受任务.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &f" + args[0] + " &c离线.");
                return;
            }
            Quest quest = Cronus.getCronusService().getRegisteredQuest().get(args[1]);
            if (quest == null) {
                error(sender, "任务 &7" + args[1] + " &c无效.");
                return;
            }
            DataPlayer playerData = CronusAPI.getData(player);
            if (playerData.getQuest().containsKey(args[1])) {
                error(sender, "玩家 &7" + args[1] + " &c已接受该任务.");
                return;
            }
            playerData.acceptQuest(player, quest);
            playerData.push();
        }
    };

    @CommandRegister
    BaseSubCommand quit = new BaseSubCommand() {

        @Override
        public CommandArgument[] getArguments() {
            return new CommandArgument[] {
                    new CommandArgument("玩家"), new CommandArgument("任务/标签", () -> getQuests())
            };
        }

        @Override
        public String getDescription() {
            return "使玩家放弃任务.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &f" + args[0] + " &c离线.");
                return;
            }
            DataPlayer dataPlayer = CronusAPI.getData(player);
            DataQuest dataQuest = dataPlayer.getQuest(args[1]);
            if (dataQuest == null) {
                error(sender, "玩家 &7" + args[1] + " &c未接受该任务.");
                return;
            }
            dataPlayer.failureQuest(player, dataQuest.getQuest());
            dataPlayer.push();
        }
    };

    @CommandRegister
    BaseSubCommand stop = new BaseSubCommand() {

        @Override
        public CommandArgument[] getArguments() {
            return new CommandArgument[] {
                    new CommandArgument("玩家"), new CommandArgument("任务/标签", () -> getQuests())
            };
        }

        @Override
        public String getDescription() {
            return "使玩家停止任务.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &f" + args[0] + " &c离线.");
                return;
            }
            DataPlayer dataPlayer = CronusAPI.getData(player);
            DataQuest dataQuest = dataPlayer.getQuest(args[1]);
            if (dataQuest == null) {
                error(sender, "玩家 &7" + args[1] + " &c未接受该任务.");
                return;
            }
            dataPlayer.getQuest().remove(dataQuest.getQuest().getId());
            dataPlayer.push();
            CronusQuestStopEvent.call(player, dataQuest.getQuest());
        }
    };

    @CommandRegister
    BaseSubCommand open = new BaseSubCommand() {

        @Override
        public CommandArgument[] getArguments() {
            return new CommandArgument[] {
                    new CommandArgument("玩家"), new CommandArgument("任务/标签", () -> getQuests())
            };
        }

        @Override
        public String getDescription() {
            return "使玩家打开日志.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &f" + args[0] + " &c离线.");
                return;
            }
            DataQuest dataQuest = CronusAPI.getData(player).getQuest(args[1]);
            if (dataQuest == null) {
                error(sender, "玩家 &7" + args[1] + " &c未接受该任务.");
                return;
            }
            dataQuest.open(player);
        }
    };

    @CommandRegister
    BaseSubCommand book = new BaseSubCommand() {

        @Override
        public CommandArgument[] getArguments() {
            return new CommandArgument[] {
                    new CommandArgument("玩家"), new CommandArgument("纵览", () -> Lists.newArrayList(Cronus.getCronusService().getRegisteredQuestBook().keySet()))
            };
        }

        @Override
        public String getDescription() {
            return "使玩家打开纵览.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &f" + args[0] + " &c离线.");
                return;
            }
            QuestBook questBook = Cronus.getCronusService().getRegisteredQuestBook().get(args[1]);
            if (questBook == null) {
                error(sender, "纵览 &7" + args[1] + " &c无效.");
                return;
            }
            questBook.open(player);
        }
    };

    @CommandRegister
    BaseSubCommand action = new BaseSubCommand() {

        @Override
        public CommandArgument[] getArguments() {
            return new CommandArgument[] {
                    new CommandArgument("玩家"),
                    new CommandArgument("任务", () -> Lists.newArrayList(Cronus.getCronusService().getRegisteredQuest().keySet())),
                    new CommandArgument("状态", () -> Arrays.stream(Action.values()).map(Action::name).collect(Collectors.toList()))
            };
        }

        @Override
        public String getDescription() {
            return "使玩家执行任务动作.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                error(sender, "玩家 &f" + args[0] + " &c离线.");
                return;
            }
            Quest quest = Cronus.getCronusService().getRegisteredQuest().get(args[1]);
            if (quest == null) {
                error(sender, "任务 &7" + args[1] + " &c无效.");
                return;
            }
            Action action = Action.fromName(args[2]);
            if (action == null) {
                error(sender, "状态 &7" + args[2] + " &c无效.");
                return;
            }
            DataQuest dataQuest = CronusAPI.getData(player).getQuest().getOrDefault(quest.getId(), new DataQuest(quest.getId(), quest.getFirstStage()));
            quest.eval(new QuestProgram(player, dataQuest), action);
        }
    };

    @CommandRegister
    BaseSubCommand mirror = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "耗能监控.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            normal(sender, "正在创建统计...");
            BookBuilder bookBuilder = BookFormatter.writtenBook();
            bookBuilder.addPages(new PageBuilder()
                    .add("").newLine()
                    .add("").newLine()
                    .add("      §lCronus Mirror").newLine()
                    .add("").newLine()
                    .add("         性能监控").newLine()
                    .build());
            CronusMirror.getMirrors().forEach((k, v) -> {
                String name = k.substring(k.indexOf(":") + 1);
                bookBuilder.addPages(ComponentSerializer.parse(TellrawJson.create()
                        .append("  §1§l§n" + simple(name)).hoverText(name).newLine()
                        .append("").newLine()
                        .append("  执行 " + v.getTimes() + " 次").newLine()
                        .append("  平均 " + v.getTimeLatest() + " 毫秒").newLine()
                        .append("  总计 " + v.getTimeTotal() + " 毫秒").newLine()
                        .toRawMessage((Player) sender)));
            });
            normal(sender, "创建完成!");
            BookFormatter.openPlayer(((Player) sender), bookBuilder.build());
        }

        @Override
        public CommandType getType() {
            return CommandType.PLAYER;
        }

        public String simple(String in) {
            if (in.length() > 20) {
                return in.substring(0, in.length() - (in.length() - 10)) + "..." + in.substring(in.length() - 7);
            }
            return in;
        }
    };

    @CommandRegister
    BaseSubCommand reload = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "重载任务.";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Cronus.getInst().reloadQuest();
            normal(sender, "重载完成.");
        }
    };
}
