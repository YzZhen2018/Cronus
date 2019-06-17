package ink.ptms.cronus.service.guide;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.asm.AsmHandler;
import me.skymc.taboolib.common.packet.TPacketListener;
import me.skymc.taboolib.common.util.SimpleReflection;
import me.skymc.taboolib.nms.NMSUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-05-29 19:35
 */
public class GuideWayPacket extends TPacketListener {

    private GuideWay service = Cronus.getCronusService().getService(GuideWay.class);

    public GuideWayPacket() {
        SimpleReflection.saveField(NMSUtils.getNMSClass("PacketPlayOutSpawnEntity"));
    }

    @Override
    public boolean onSend(Player player, Object packet) {
        if (packet.getClass().getSimpleName().equals("PacketPlayOutSpawnEntity")) {
            try {
                Entity entity = AsmHandler.getImpl().getEntityByEntityId((int) SimpleReflection.getFieldValue(packet.getClass(), packet, "a"));
                if (entity != null && entity.hasMetadata("cronus_guide_owner")) {
                    return player.getName().equals(entity.getMetadata("cronus_guide_owner").get(0).asString());
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return true;
    }
}
