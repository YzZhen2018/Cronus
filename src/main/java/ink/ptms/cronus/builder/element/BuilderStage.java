package ink.ptms.cronus.builder.element;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-06-18 23:09
 */
public class BuilderStage{

    private String id;
    private List<String> actionAccept;
    private List<String> actionSuccess;

    public BuilderStage(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void open(Player player, BuilderStageList list) {
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

    }
}
