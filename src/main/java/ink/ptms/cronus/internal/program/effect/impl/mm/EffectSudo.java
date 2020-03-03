package ink.ptms.cronus.internal.program.effect.impl.mm;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.internal.program.TargetProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitPlayer;
import io.lumine.xikage.mythicmobs.mobs.GenericCaster;
import io.lumine.xikage.mythicmobs.skills.SkillManager;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto(depend = "MythicMobs")
public class EffectSudo extends Effect {

    private SkillMechanic mechanic;

    @Override
    public String pattern() {
        return "mm\\.(sudo) (?<value>.+)";
    }

    @Override
    public String getExample() {
        return "mm.sudo [name]";
    }

    @Override
    public void match(Matcher matcher) {
        mechanic = MythicMobs.inst().getSkillManager().getSkillMechanic(matcher.group("value"));
    }

    @Override
    public void eval(Program program) {
        if (program.getSender() instanceof Player) {
            HashSet<AbstractEntity> entities = Sets.newHashSet();
            HashSet<AbstractLocation> location = Sets.newHashSet();
            if (program instanceof TargetProgram) {
                entities.addAll(((TargetProgram) program).getEntities().stream().map(BukkitAdapter::adapt).collect(Collectors.toList()));
                location.addAll(((TargetProgram) program).getLocations().stream().map(BukkitAdapter::adapt).collect(Collectors.toList()));
            }
            MythicMobs.inst().getSkillManager().runSecondPass();
            BukkitPlayer bukkitPlayer = new BukkitPlayer(program.asPlayer());
            GenericCaster genericCaster = new GenericCaster(bukkitPlayer);
            SkillMetadata skillMetadata = new SkillMetadata(
                    SkillTrigger.API,
                    genericCaster,
                    bukkitPlayer,
                    BukkitAdapter.adapt(program.asPlayer().getLocation()),
                    entities,
                    location,
                    0);
            Bukkit.getScheduler().runTask(Cronus.getPlugin(), () -> {
                mechanic.executeSkills(skillMetadata);
            });
        }
    }
}
