package ink.ptms.cronus.internal.task.player;

import ink.ptms.cronus.database.data.DataQuest;
import ink.ptms.cronus.internal.bukkit.Entity;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.task.special.Countable;
import ink.ptms.cronus.internal.task.Task;
import ink.ptms.cronus.util.StringExpression;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.HorseJumpEvent;

import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:21
 */
@Task(name = "player_jump_horse")
public class TaskPlayerJumpHorse extends Countable<HorseJumpEvent> {

    private Entity horse;
    private StringExpression power;

    public TaskPlayerJumpHorse(ConfigurationSection config) {
        super(config);
    }

    @Override
    public void init(Map<String, Object> data) {
        super.init(data);
        horse = data.containsKey("horse") ? BukkitParser.toEntity(data.get("horse")) : null;
        power = data.containsKey("power") ? new StringExpression(data.get("power")) : null;
    }

    @Override
    public boolean check(Player player, DataQuest dataQuest, HorseJumpEvent e) {
        return (horse == null || horse.isSelect(e.getEntity())) && (power == null || power.isSelect(e.getPower()));
    }

    @Override
    public String toString() {
        return "TaskPlayerJumpHorse{" +
                "horse=" + horse +
                ", power=" + power +
                ", count=" + count +
                ", id='" + id + '\'' +
                ", config=" + config +
                ", condition=" + condition +
                ", guide=" + guide +
                ", action=" + action +
                '}';
    }
}
