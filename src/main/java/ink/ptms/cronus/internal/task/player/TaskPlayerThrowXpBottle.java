package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Entity;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ExpBottleEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_throw_xp_bottle")
public class TaskPlayerThrowXpBottle extends Countable<ExpBottleEvent> {

    private Entity bottle;

    public TaskPlayerThrowXpBottle(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        bottle = data.containsKey("bottle") ? BukkitParser.toEntity(data.get("bottle")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, ExpBottleEvent e) {
        return (bottle == null || bottle.isSelect(e.getEntity()));
    }

    @Override
    public String toString() {
        return "TaskPlayerThrowXpBottle{" +
                "bottle=" + bottle +
                ", count=" + count +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", guide=" + guide +
                ", action=" + action +
                '}';
    }
}
