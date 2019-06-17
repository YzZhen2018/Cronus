package ink.ptms.cronus.internal.task.listener;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.internal.task.other.TaskLocation;
import ink.ptms.cronus.internal.task.player.*;
import ink.ptms.cronus.internal.task.player.damage.TaskPlayerAttack;
import ink.ptms.cronus.internal.task.player.damage.TaskPlayerDamaged;
import ink.ptms.cronus.internal.task.player.damage.TaskPlayerDeath;
import ink.ptms.cronus.internal.task.player.damage.TaskPlayerKill;
import me.skymc.taboolib.damage.DamageUtils;
import me.skymc.taboolib.inventory.ItemUtils;
import me.skymc.taboolib.listener.TListener;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;

/**
 * @Author 坏黑
 * @Since 2019-05-31 14:50
 */
@TListener
public class ListenerPlayer implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerFishEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerFish.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerEditBookEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerBook.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerBucketEmptyEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerBucketEmpty.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerBucketFillEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerBucketFill.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(AsyncPlayerChatEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerChat.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerCommandPreprocessEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerCommand.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerLeashEntityEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerLeash.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerShearEntityEvent e) {
        CronusAPI.stageHandle(e.getPlayer(), e, TaskPlayerShear.class);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(EntityTameEvent e) {
        if (e.getOwner() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getOwner(), e, TaskPlayerTame.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getEntity(), e, TaskPlayerChat.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getEntity().getShooter(), e, TaskPlayerShoot.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(EntityBreedEvent e) {
        if (e.getBreeder() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getBreeder(), e, TaskPlayerBreed.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getEntity(), e, TaskPlayerDamaged.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(EntityDeathEvent e) {
        if (e.getEntity().getKiller() instanceof Player) {
            CronusAPI.stageHandle(e.getEntity().getKiller(), e, TaskPlayerKill.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            CronusAPI.stageHandle(e.getEntity(), e, TaskPlayerDeath.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(InventoryClickEvent e) {
        if (e.getInventory().getType() == InventoryType.MERCHANT && e.getRawSlot() == 2 && !ItemUtils.isNull(e.getCurrentItem())) {
            CronusAPI.stageHandle((Player) e.getWhoClicked(), e, TaskPlayerTrade.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(PlayerMoveEvent e) {
        if (!e.getFrom().getBlock().equals(e.getTo().getBlock())) {
            CronusAPI.stageHandle(e.getPlayer(), e, TaskLocation.class, TaskPlayerWalk.class, TaskPlayerSwim.class, TaskPlayerRide.class);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void e(EntityDamageByEntityEvent e) {
        LivingEntity attacker = DamageUtils.getLivingAttackerInDamageEvent(e);
        if (attacker instanceof Player) {
            CronusAPI.stageHandle((Player) attacker, e, TaskPlayerAttack.class);
        }
        if (e.getEntity() instanceof Player) {
            CronusAPI.stageHandle((Player) e.getEntity(), e, TaskPlayerDamaged.class);
        }
    }
}
