package ink.ptms.cronus.command.impl;

import com.ilummc.tlib.bungee.chat.ComponentSerializer;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.command.CronusCommand;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Cond;
import ink.ptms.cronus.internal.condition.CondNull;
import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.internal.condition.ConditionParser;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.internal.program.effect.EffectNull;
import ink.ptms.cronus.internal.program.effect.EffectParser;
import ink.ptms.cronus.uranus.program.ProgramLoader;
import ink.ptms.cronus.uranus.program.effect.Effect;
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
import me.skymc.taboolib.string.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-05-31 22:10
 */
@TCommand(name = "CronusTest", aliases = {"ct"}, permission = "*")
public class CommandTest extends CronusCommand {

    @CommandRegister
    BaseSubCommand condition = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "测试条件语法";
        }

        @Override
        public CommandArgument[] getArguments() {
            return new CommandArgument[] {
                    new CommandArgument("表达式")
            };
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            Condition parse = ConditionParser.parse(ArrayUtils.arrayJoin(args, 0));
            if (parse instanceof CondNull) {
                error(sender, "条件格式错误.");
            } else {
                try {
                    normal(sender, "条件执行结果: " + (parse.check((Player) sender, new DataQuest(), null) ? "&a成功" : "&c失败"));
                } catch (Throwable t) {
                    error(sender, "条件执行失败: " + t.getMessage());
                }
            }
        }

        @Override
        public CommandType getType() {
            return CommandType.PLAYER;
        }
    };

    @CommandRegister
    BaseSubCommand effect = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "测试动作语法";
        }

        @Override
        public CommandArgument[] getArguments() {
            return new CommandArgument[] {
                    new CommandArgument("表达式")
            };
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            Effect parse = EffectParser.parse(ArrayUtils.arrayJoin(args, 0));
            if (parse instanceof EffectNull) {
                error(sender, "动作格式错误.");
            } else {
                try {
                    parse.eval(new QuestProgram((Player) sender, new DataQuest()));
                } catch (Throwable t) {
                    error(sender, "动作执行失败: " + t.getMessage());
                }
            }
        }

        @Override
        public CommandType getType() {
            return CommandType.PLAYER;
        }
    };

    @CommandRegister
    BaseSubCommand conditions = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "列出可用条件语法";
        }

        @Override
        public CommandArgument[] getArguments() {
            return new CommandArgument[] {
                    new CommandArgument("关键字", false)
            };
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            normal(sender, "正在创建统计...");
            BookBuilder bookBuilder = BookFormatter.writtenBook();
            bookBuilder.addPages(new PageBuilder()
                    .add("").newLine()
                    .add("").newLine()
                    .add("     §lCronus Conditions").newLine()
                    .add("").newLine()
                    .add("         可用条件").newLine()
                    .build());
            Cronus.getCronusService().getRegisteredCondition().entrySet().stream().filter(e -> args.length == 0 || e.getKey().contains(args[0])).forEach(e -> {
                Cond cond = e.getValue().getCondition().getAnnotation(Cond.class);
                bookBuilder.addPages(ComponentSerializer.parse(TellrawJson.create()
                        .append("  §1§l§n" + Utils.toSimple(e.getValue().getCondition().getSimpleName())).hoverText(e.getValue().getCondition().getSimpleName()).newLine()
                        .append("").newLine()
                        .append("  格式 " + Utils.toSimple(cond.example())).hoverText(format(cond.example())).newLine()
                        .append("  位置 " + Utils.toSimple(e.getValue().getCondition().getName())).hoverText(e.getValue().getCondition().getName()).newLine()
                        .toRawMessage((Player) sender)));
            });
            normal(sender, "创建完成!");
            BookFormatter.openPlayer(((Player) sender), bookBuilder.build());
        }

        @Override
        public CommandType getType() {
            return CommandType.PLAYER;
        }
    };

    @CommandRegister
    BaseSubCommand effects = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "列出可用条件动作";
        }

        @Override
        public CommandArgument[] getArguments() {
            return new CommandArgument[] {
                    new CommandArgument("关键字", false)
            };
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            normal(sender, "正在创建统计...");
            BookBuilder bookBuilder = BookFormatter.writtenBook();
            bookBuilder.addPages(new PageBuilder()
                    .add("").newLine()
                    .add("").newLine()
                    .add("      §lCronus Effects").newLine()
                    .add("").newLine()
                    .add("         可用动作").newLine()
                    .build());
            ProgramLoader.getEffects().stream().filter(e -> args.length == 0 || e.getClass().getSimpleName().contains(args[0])).forEach(e -> {
                bookBuilder.addPages(ComponentSerializer.parse(TellrawJson.create()
                        .append("  §1§l§n" + Utils.toSimple(e.getClass().getSimpleName())).hoverText(e.getClass().getSimpleName()).newLine()
                        .append("").newLine()
                        .append("  格式 " + Utils.toSimple(e.getExample())).hoverText(format(e.getExample())).newLine()
                        .append("  位置 " + Utils.toSimple(e.getClass().getName())).hoverText(e.getClass().getName()).newLine()
                        .toRawMessage((Player) sender)));
            });
            normal(sender, "创建完成!");
            BookFormatter.openPlayer(((Player) sender), bookBuilder.build());
        }

        @Override
        public CommandType getType() {
            return CommandType.PLAYER;
        }
    };

    private String format(String example) {
        return "§7" + example.replaceAll("\\.", "§8$0§7").replaceAll("\\[(\\S+)]", "§e[§6$1§e]§7");
    }
}
