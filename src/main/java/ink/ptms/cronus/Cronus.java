package ink.ptms.cronus;

import com.ilummc.tlib.logger.TLogger;
import com.ilummc.tlib.util.Strings;
import me.skymc.taboolib.common.configuration.TConfiguration;
import me.skymc.taboolib.common.inject.TInject;
import me.skymc.taboolib.common.schedule.TSchedule;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @Author 坏黑
 * @Since 2019-05-23 18:06
 */
public class Cronus extends JavaPlugin {

    private static Cronus inst;
    private static CronusLoader cronusLoader = new CronusLoader();
    private static CronusService cronusService = new CronusService();
    @TInject
    private static TLogger logger;
    private static TConfiguration conf;

    @Override
    public void onLoad() {
        inst = this;
        conf = TConfiguration.createInResource(this, "config.yml");
    }

    @Override
    public void onEnable() {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inst.getResource("motd.txt"), StandardCharsets.UTF_8); BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            bufferedReader.lines().forEach(l -> Bukkit.getConsoleSender().sendMessage(Strings.replaceWithOrder(l, inst.getDescription().getVersion())));
        } catch (Throwable ignored) {
        }
        cronusService.init();
        cronusLoader.init();
        cronusLoader.start();
    }

    @Override
    public void onDisable() {
        cronusService.cancel();
    }

    public void reloadQuest() {
        cronusLoader.start();
    }

    // *********************************
    //
    //        Getter and Setter
    //
    // *********************************

    public static Cronus getInst() {
        return inst;
    }

    public static CronusLoader getCronusLoader() {
        return cronusLoader;
    }

    public static CronusService getCronusService() {
        return cronusService;
    }

    public static TConfiguration getConf() {
        return conf;
    }
}
