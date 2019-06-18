package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Block;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_bucket_empty")
public class TaskPlayerBucketEmpty extends Countable {

    private Block block;
    private ItemStack bucket;

    public TaskPlayerBucketEmpty(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        block = data.containsKey("block") ? BukkitParser.toBlock(data.get("block")) : null;
        bucket = data.containsKey("bucket") ? BukkitParser.toItemStack(data.get("bucket")) : null;
    }

    @Override
    public boolean isValid(Player player, DataQuest dataQuest, Event event) {
        PlayerBucketEmptyEvent e = ((PlayerBucketEmptyEvent) event);
        return (block == null || block.isSelect(e.getBlockClicked())) && (bucket == null || bucket.isItem(e.getItemStack()));
    }

    @Override
    public String toString() {
        return "TaskPlayerBucketEmpty{" +
                "count=" + count +
                ", block=" + block +
                ", bucket=" + bucket +
                ", action=" + action +
                ", config=" + config +
                ", condition=" + condition +
                ", guide=" + guide +
                '}';
    }
}
