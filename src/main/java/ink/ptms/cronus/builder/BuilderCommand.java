package ink.ptms.cronus.builder;

import ink.ptms.cronus.builder.element.BuilderQuest;
import ink.ptms.cronus.command.CronusCommand;
import me.skymc.taboolib.commands.internal.BaseSubCommand;
import me.skymc.taboolib.commands.internal.TCommand;
import me.skymc.taboolib.commands.internal.type.CommandArgument;
import me.skymc.taboolib.commands.internal.type.CommandRegister;
import me.skymc.taboolib.commands.internal.type.CommandType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-06-18 19:43
 */
@TCommand(name = "CronusBuilder", aliases = {"cb"}, permission = "*")
public class BuilderCommand extends CronusCommand {

    @CommandRegister
    BaseSubCommand create = new BaseSubCommand() {

        @Override
        public CommandArgument[] getArguments() {
            return new CommandArgument[] {
                    new CommandArgument("名称")
            };
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            new BuilderQuest("quest_1").open((Player) sender);
        }

        @Override
        public CommandType getType() {
            return CommandType.PLAYER;
        }
    };

    @CommandRegister
    BaseSubCommand open = new BaseSubCommand() {

        @Override
        public CommandArgument[] getArguments() {
            return new CommandArgument[] {
                    new CommandArgument("名称")
            };
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {

        }

        @Override
        public CommandType getType() {
            return CommandType.PLAYER;
        }
    };

}
