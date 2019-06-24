package ink.ptms.cronus.command.impl;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.builder.element.BuilderQuest;
import ink.ptms.cronus.command.CronusCommand;
import me.skymc.taboolib.commands.internal.BaseSubCommand;
import me.skymc.taboolib.commands.internal.TCommand;
import me.skymc.taboolib.commands.internal.type.CommandArgument;
import me.skymc.taboolib.commands.internal.type.CommandRegister;
import me.skymc.taboolib.commands.internal.type.CommandType;
import me.skymc.taboolib.fileutils.FileUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-06-18 19:43
 */
@TCommand(name = "CronusBuilder", aliases = {"cb"}, permission = "*")
public class CommandBuilder extends CronusCommand {

    @CommandRegister
    BaseSubCommand create = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "创建任务构造器";
        }

        @Override
        public CommandArgument[] getArguments() {
            return new CommandArgument[] {
                    new CommandArgument("名称")
            };
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            File file = new File(Cronus.getCronusLoader().getFolder(), "builder/" + args[0] + ".yml");
            if (file.exists()) {
                error(sender, "任务 §7" + args[0] + " §c已存在.");
                return;
            }
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.createSection(args[0]);
            try {
                yaml.save(file);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            normal(sender, "任务 §f" + args[0] + " §7创建成功.");
        }

        @Override
        public CommandType getType() {
            return CommandType.PLAYER;
        }
    };

    @CommandRegister
    BaseSubCommand open = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "打开任务构造器";
        }

        @Override
        public CommandArgument[] getArguments() {
            return new CommandArgument[] {
                    new CommandArgument("名称", () -> {
                        File file = FileUtils.folder(new File(Cronus.getCronusLoader().getFolder(), "builder"));
                        return Arrays.stream(file.listFiles()).map(s -> {
                            try {
                                return s.getName().substring(0, s.getName().indexOf("."));
                            } catch (Throwable ignored) {
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList());
                    })
            };
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            File file = new File(Cronus.getCronusLoader().getFolder(), "builder/" + args[0] + ".yml");
            if (!file.exists()) {
                error(sender, "任务 §7" + args[0] + " §c无效.");
                return;
            }
            new BuilderQuest(file).open((Player) sender);
        }

        @Override
        public CommandType getType() {
            return CommandType.PLAYER;
        }
    };

}
