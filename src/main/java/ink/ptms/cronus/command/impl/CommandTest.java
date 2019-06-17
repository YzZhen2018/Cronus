package ink.ptms.cronus.command.impl;

import ink.ptms.cronus.command.CronusCommand;
import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.condition.Condition;
import ink.ptms.cronus.internal.condition.ConditionParser;
import ink.ptms.cronus.internal.condition.impl.CondNull;
import me.skymc.taboolib.commands.internal.BaseSubCommand;
import me.skymc.taboolib.commands.internal.TCommand;
import me.skymc.taboolib.commands.internal.type.CommandArgument;
import me.skymc.taboolib.commands.internal.type.CommandRegister;
import me.skymc.taboolib.commands.internal.type.CommandType;
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
                    normal(sender, "条件执行结果: " + (parse.isValid((Player) sender, new DataQuest(), null) ? "&a成功" : "&c失败"));
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

}
