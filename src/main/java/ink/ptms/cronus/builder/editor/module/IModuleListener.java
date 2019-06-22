package ink.ptms.cronus.builder.editor.module;

import ink.ptms.cronus.builder.editor.EditorAPI;
import me.skymc.taboolib.common.inject.TInject;
import me.skymc.taboolib.common.packet.TPacketListener;
import me.skymc.taboolib.common.schedule.TSchedule;
import me.skymc.taboolib.common.util.SimpleReflection;
import me.skymc.taboolib.listener.TListener;
import me.skymc.taboolib.nms.NMSUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-03-11 18:03
 */
@TListener
public class IModuleListener implements Listener {

    @TSchedule
    static void load() {
        SimpleReflection.saveField(NMSUtils.getNMSClass("PacketPlayInChat"));
    }

    @TInject
    static TPacketListener listener = new TPacketListener() {
        @Override
        public boolean onReceive(Player player, Object packet) {
            if (packet.getClass().getSimpleName().equals("PacketPlayInChat")) {
                if (EditorAPI.isEditMode(player)) {
                    String message = String.valueOf(SimpleReflection.getFieldValue(packet.getClass(), packet, "a"));
                    EditorAPI.eval(player, message);
                    return message.startsWith("/");
                }
            }
            return true;
        }
    };

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (EditorAPI.isEditMode(e.getPlayer()) ) {
            e.setCancelled(true);
            EditorAPI.eval(e.getPlayer(), e.getMessage());
        }
    }

    @EventHandler
    public void onTab(PlayerChatTabCompleteEvent e) {
        if (EditorAPI.isEditMode(e.getPlayer())) {
            try {
                if (e.getChatMessage().startsWith(":")) {
                    e.getTabCompletions().clear();
                    if (e.getChatMessage().split(" ").length == 1 && !e.getChatMessage().endsWith(" ")) {
                        e.getTabCompletions().addAll(IModuleHandler.getModules().keySet().stream().filter(command -> command.toLowerCase().startsWith(e.getChatMessage().substring(1).toLowerCase())).map(command -> ":" + command).collect(Collectors.toList()));
                        return;
                    }
                    IModule module = IModuleHandler.getModules().get(e.getChatMessage().substring(1).split(" ")[0]);
                    if (module == null) {
                        e.getPlayer().sendMessage("§c[PurtmarsEditor] §7无效的命令.");
                    } else {
                        e.getTabCompletions().addAll(module.complete(e.getPlayer(), e.getChatMessage().substring(e.getChatMessage().split(" ")[0].length() + 1)));
                    }
                }
            } catch (Throwable ignored) {
            }
        }
    }
}
