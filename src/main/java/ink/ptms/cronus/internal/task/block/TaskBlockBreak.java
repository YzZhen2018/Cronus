package ink.ptms.cronus.internal.task.block;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Block;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "block_break")
public class TaskBlockBreak extends Countable {

    private Block block;
    private Location location;

    public TaskBlockBreak(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        block = data.containsKey("block") ? BukkitParser.toBlock(data.get("block")) : null;
        location = data.containsKey("location") ? BukkitParser.toLocation(data.get("location")) : null;
    }

    @Override
    public boolean isValid(Player player, DataQuest dataQuest, Event event) {
        BlockBreakEvent e = ((BlockBreakEvent) event);
        return (block == null || block.isSelect(e.getBlock())) && (location == null || location.inSelect(e.getBlock().getLocation()));
    }

    @Override
    public String toString() {
        return "TaskBlockBreak{" +
                "count=" + count +
                ", block=" + block +
                ", location=" + location +
                ", action=" + action +
                ", config=" + config +
                ", condition=" + condition +
                '}';
    }
}
