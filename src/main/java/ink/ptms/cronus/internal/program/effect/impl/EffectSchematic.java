package ink.ptms.cronus.internal.program.effect.impl;

import com.ilummc.tlib.logger.TLogger;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditAPI;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.adapter.BukkitImplAdapter;
import com.sk89q.worldedit.command.SchematicCommands;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.registry.WorldData;
import ink.ptms.cronus.internal.bukkit.Location;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.service.hook.WorldEditHook;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.FunctionParser;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;
import io.lumine.xikage.mythicmobs.adapters.AbstractWorld;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import me.skymc.taboolib.common.inject.TInject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.NumberConversions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto
public class EffectSchematic extends Effect {

    @TInject
    private static TLogger logger;
    private String name;
    private String symbol;
    private Location location;
    private WorldEditPlugin plugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

    @Override
    public String pattern() {
        return "schematic (?<symbol>-a)?[ ]?(?<name>\\S+) (?<location>.+)";
    }

    @Override
    public String getExample() {
        return "schematic <-a> [name] [location]";
    }

    @Override
    public void match(Matcher matcher) {
        name = matcher.group("name");
        symbol = matcher.group("symbol");
        location = BukkitParser.toLocation(matcher.group("location"));
    }

    @Override
    public void eval(Program program) {
        if (program instanceof QuestProgram && location.isBukkit()) {
            WorldEditHook.pasteSchematic(((QuestProgram) program).getPlayer(), name, location.toBukkit(), symbol != null);
        }
    }

    @Override
    public String toString() {
        return "EffectSchematic{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", location=" + location +
                ", plugin=" + plugin +
                '}';
    }
}
