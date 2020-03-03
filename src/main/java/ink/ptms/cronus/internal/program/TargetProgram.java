package ink.ptms.cronus.internal.program;

import ink.ptms.cronus.database.data.DataQuest;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * @Author 坏黑
 * @Since 2019-05-26 22:38
 */
public class TargetProgram extends QuestProgram {

    private static final DataQuest GLOBAL = new DataQuest("null", "null");
    private Set<Entity> entities;
    private Set<Location> locations;

    public TargetProgram(Player player, Set<Entity> entities, Set<Location> locations) {
        super(player, GLOBAL);
        this.entities = entities;
        this.locations = locations;
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public Set<Location> getLocations() {
        return locations;
    }
}
