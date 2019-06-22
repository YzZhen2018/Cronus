package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Entity;
import ink.ptms.cronus.internal.bukkit.FishState;
import ink.ptms.cronus.internal.bukkit.ItemStack;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.util.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_fish")
public class TaskPlayerFish extends Countable<PlayerFishEvent> {

    private Entity entity;
    private ItemStack item;
    private FishState state;

    public TaskPlayerFish(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        item = data.containsKey("item") ? BukkitParser.toItemStack(data.get("item")) : null;
        entity = data.containsKey("entity") ? BukkitParser.toEntity(data.get("entity")) : null;
        state = data.containsKey("state") ? BukkitParser.toFishState(data.get("cause")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, PlayerFishEvent e) {
        return (state == null || state.isSelect(e.getState())) && (item == null || item.isItem(Utils.NonNull(Utils.getUsingItem(e.getPlayer(), Material.FISHING_ROD)))) && (entity == null || (e.getCaught() != null && entity.isSelect(e.getCaught())));
    }

    @Override
    public String toString() {
        return "TaskItemConsume{" +
                "count=" + count +
                ", item=" + item +
                ", action=" + action +
                ", config=" + config +
                ", condition=" + condition +
                '}';
    }
}
