package ink.ptms.cronus.uranus.program.effect;

import com.ilummc.tlib.logger.TLogger;
import com.ilummc.tlib.resources.TLocale;
import ink.ptms.cronus.uranus.Uranus;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.FunctionParser;
import ink.ptms.cronus.uranus.program.Program;
import me.skymc.taboolib.common.inject.TInject;
import me.skymc.taboolib.particle.pack.ParticlePack;
import me.skymc.taboolib.sound.SoundPack;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto
public class EffectDisplay extends Effect {

    @TInject
    private static TLogger logger;
    private String type;
    private String value;

    @Override
    public String pattern() {
        return "(display|send)\\.(?<type>\\S+) (?<value>.+)";
    }

    @Override
    public void match(Matcher matcher) {
        type = matcher.group("type");
        value = matcher.group("value");
    }

    @Override
    public void eval(Program program) {
        String parsed = TLocale.Translate.setColored(FunctionParser.parseAll(program, value));
        switch (type.toLowerCase()) {
            case "text":
                program.getSender().sendMessage(parsed);
                break;
            case "text.all":
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(parsed));
                break;
            case "action":
                if (program.getSender() instanceof Player) {
                    TLocale.Display.sendActionBar((Player) program.getSender(), parsed);
                }
                break;
            case "action.all":
                Bukkit.getOnlinePlayers().forEach(player -> TLocale.Display.sendActionBar(player, parsed));
                break;
            case "title":
                if (program.getSender() instanceof Player) {
                    Bukkit.getScheduler().runTaskAsynchronously(Uranus.getInst(), () -> sendTitle((Player) program.getSender(), parsed));
                }
                break;
            case "title.all":
                Bukkit.getScheduler().runTaskAsynchronously(Uranus.getInst(), () -> Bukkit.getOnlinePlayers().forEach(p -> sendTitle(p, parsed)));
                break;
            case "particle":
                if (program.getSender() instanceof Player) {
                    Bukkit.getScheduler().runTaskAsynchronously(Uranus.getInst(), () -> new ParticlePack(parsed).play(((Player) program.getSender()).getLocation().add(0, 1, 0)));
                }
                break;
            case "sound":
                if (program.getSender() instanceof Player) {
                    Bukkit.getScheduler().runTaskAsynchronously(Uranus.getInst(), () -> new SoundPack(parsed).play((Player) program.getSender()));
                }
                break;
            case "sound.all":
                Bukkit.getScheduler().runTaskAsynchronously(Uranus.getInst(), () -> {
                    SoundPack sound = new SoundPack(parsed);
                    Bukkit.getOnlinePlayers().forEach(sound::play);
                });
                break;
            case "sound.loc":
            case "sound.location":
                if (program.getSender() instanceof Player) {
                    Bukkit.getScheduler().runTaskAsynchronously(Uranus.getInst(), () -> new SoundPack(parsed).play(((Player) program.getSender()).getLocation()));
                }
                break;
            default:
                program.getSender().sendMessage(parsed);
                break;
        }
    }

    public void sendTitle(Player player, String parsed) {
        String[] title = parsed.split("\\|");
        TLocale.Display.sendTitle(player,
                title[0],
                title.length > 1 ? title[1] : "",
                title.length > 2 ? NumberConversions.toInt(title[2]) : 0,
                title.length > 3 ? NumberConversions.toInt(title[3]) : 20,
                title.length > 4 ? NumberConversions.toInt(title[4]) : 0);
    }

    @Override
    public String toString() {
        return "EffectMessage{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
