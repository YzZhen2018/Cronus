package ink.ptms.cronus.service.dialog;

import com.google.common.collect.Lists;
import com.ilummc.tlib.logger.TLogger;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.event.CronusDialogEvent;
import ink.ptms.cronus.event.CronusReloadEvent;
import ink.ptms.cronus.service.Service;
import ink.ptms.cronus.service.selector.EntitySelector;
import ink.ptms.cronus.uranus.annotations.Auto;
import me.skymc.taboolib.common.inject.TInject;
import me.skymc.taboolib.cooldown.seconds.CooldownPack2;
import me.skymc.taboolib.fileutils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-06-10 18:14
 */
@Auto
public class Dialog implements Service, Listener {

    @TInject
    private static TLogger logger;
    @TInject
    private static CooldownPack2 cooldown = new CooldownPack2("cronus|dialog", 200);

    private File folder;
    private List<DialogGroup> dialogs = Lists.newArrayList();

    @Override
    public void init() {
    }

    @Override
    public void cancel() {
    }

    @EventHandler
    public void e(CronusReloadEvent e) {
        long time = System.currentTimeMillis();
        folder = new File(Cronus.getInst().getDataFolder(), "dialog");
        if (!folder.exists()) {
            Cronus.getInst().saveResource("dialog/def.yml", true);
        }
        dialogs.clear();
        loadDialog(folder);
        logger.info(dialogs.size() + " Dialog Loaded. (" + (System.currentTimeMillis() - time + "ms)"));
    }

    @EventHandler(ignoreCancelled = true)
    public void e(PlayerInteractEntityEvent e) {
        if (cooldown.isCooldown(e.getPlayer().getName(), 0)) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(Cronus.getInst(), () -> {
            EntitySelector selector = Cronus.getCronusService().getService(EntitySelector.class);
            for (DialogGroup dialog : dialogs) {
                if (selector.isSelect(e.getRightClicked(), dialog.getTarget())) {
                    CronusDialogEvent dialogEvent = CronusDialogEvent.call(dialog.getDialog(), e.getRightClicked(), e.getPlayer());
                    if (!dialogEvent.isCancelled()) {
                        try {
                            dialogEvent.getPack().display(e.getPlayer());
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                    return;
                }
            }
        });
    }

    public void loadDialog(File file) {
        if (file.isDirectory()) {
            Arrays.stream(file.listFiles()).forEach(this::loadDialog);
        } else {
            YamlConfiguration yaml = ConfigUtils.loadYaml(Cronus.getInst(), file);
            for (String id : yaml.getKeys(false)) {
                ConfigurationSection config = yaml.getConfigurationSection(id);
                try {
                    dialogs.add(new DialogGroup(config));
                } catch (Throwable t) {
                    logger.error("Dialog " + id + " failed to load.");
                    t.printStackTrace();
                }
            }
        }
    }

    public DialogGroup getDialog(String id) {
        return dialogs.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
    }

    public List<DialogGroup> getDialogs() {
        return dialogs;
    }

    public File getFolder() {
        return folder;
    }
}
