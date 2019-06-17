package ink.ptms.cronus.service.hook;

import com.nisovin.shopkeepers.Shopkeeper;
import com.nisovin.shopkeepers.ShopkeepersPlugin;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.service.Service;
import ink.ptms.cronus.uranus.annotations.Auto;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.NumberConversions;

/**
 * @Author 坏黑
 * @Since 2019-06-10 16:36
 */
@Auto
public class EntitySelector implements Service {

    private Plugin citizens;
    private Plugin mythicmobs;
    private Plugin shopkeepers;


    @Override
    public void init() {
        citizens = Bukkit.getPluginManager().getPlugin("Citizens");
        mythicmobs = Bukkit.getPluginManager().getPlugin("MythicMobs");
        shopkeepers = Bukkit.getPluginManager().getPlugin("Shopkeepers");
    }

    @Override
    public void cancel() {

    }

    public boolean isSelect(Entity entity, String in) {
        String[] v = in.split("=");
        if (v.length > 1) {
            switch (v[0].toLowerCase()) {
                case "citizens":
                case "npc":
                    return isCitizensSelect(entity, v[1]);
                case "mythicmobs":
                case "mm":
                    return isMythicMobsSelect(entity, v[1]);
                case "shopkeeper":
                case "shop":
                    return isShopkeepersSelect(entity, v[1]);
            }
        }
        return BukkitParser.toEntity(in).isSelect(entity);
    }

    public boolean isCitizensSelect(Entity entity, String in) {
        if (citizens == null) {
            return false;
        }
        NPC npc = ((Citizens) citizens).getNPCRegistry().getNPC(entity);
        return npc != null && npc.getId() == NumberConversions.toInt(in);
    }

    public boolean isMythicMobsSelect(Entity entity, String in) {
        if (mythicmobs == null) {
            return false;
        }
        ActiveMob activeMob = ((MythicMobs) mythicmobs).getMobManager().getActiveMob(entity.getUniqueId()).orElse(null);
        return activeMob != null && activeMob.getType().getInternalName().equals(in);
    }

    public boolean isShopkeepersSelect(Entity entity, String in) {
        if (shopkeepers == null) {
            return false;
        }
        Shopkeeper shopkeeperByEntity = ((ShopkeepersPlugin) shopkeepers).getShopkeeperByEntity(entity);
        return shopkeeperByEntity != null && shopkeeperByEntity.getUniqueId().toString().equals(in);
    }

    public boolean isCitizensHooked() {
        return citizens != null;
    }

    public boolean isMythicMobsHooked() {
        return mythicmobs != null;
    }

    public boolean isShopkeepersHooked() {
        return shopkeepers != null;
    }

    public Plugin getCitizens() {
        return citizens;
    }

    public Plugin getMythicmobs() {
        return mythicmobs;
    }

    public Plugin getShopkeepers() {
        return shopkeepers;
    }
}
