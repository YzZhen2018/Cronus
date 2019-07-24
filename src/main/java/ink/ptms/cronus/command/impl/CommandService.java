package ink.ptms.cronus.command.impl;

import com.google.common.collect.Lists;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.service.Service;
import io.izzel.taboolib.module.command.base.Argument;
import io.izzel.taboolib.module.command.base.BaseCommand;
import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-06-10 20:26
 */
@BaseCommand(name = "CronusService", aliases = {"cService", "cs"}, permission = "*")
public class CommandService extends Command {

    @SubCommand
    BaseSubCommand all = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "所有服务";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            normal(sender, "当前已注册 &f" + Cronus.getCronusService().getServices().size() + " &7项服务:");
            for (Map.Entry<String, Service> entry : Cronus.getCronusService().getServices().entrySet()) {
                normal(sender, "  &f" + entry.getKey() + ": &8" + entry.getValue().getClass().getName());
            }
        }
    };

    @SubCommand
    BaseSubCommand reload = new BaseSubCommand() {

        @Override
        public Argument[] getArguments() {
            return new Argument[] {
                    new Argument("服务", () -> Lists.newArrayList(Cronus.getCronusService().getServices().keySet()))
            };
        }

        @Override
        public String getDescription() {
            return "重载服务";
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
            Service service = Cronus.getCronusService().getService(args[0]);
            if (service == null) {
                error(sender, "服务 &7" + args[0] + " &c无效.");
                return;
            }
            service.cancel();
            service.init();
            normal(sender, "服务 &f" + args[0] + " &7已重载.");
        }
    };

}
