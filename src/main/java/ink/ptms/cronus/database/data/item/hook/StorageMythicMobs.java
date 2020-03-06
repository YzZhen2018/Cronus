package ink.ptms.cronus.database.data.item.hook;

import com.google.common.collect.Lists;
import ink.ptms.cronus.database.data.item.ItemStorage;
import io.lumine.xikage.mythicmobs.MythicMobs;
import me.asgard.sacreditem.item.SacredItemManager;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-05-28 23:41
 */
public class StorageMythicMobs extends ItemStorage {

    @Override
    public String depend() {
        return "MythicMobs";
    }

    @Override
    public ItemStack getItem(String name) {
        return MythicMobs.inst().getItemManager().getItemStack(name);
    }

    @Override
    public void addItem(String name, ItemStack itemStack) {
    }

    @Override
    public void delItem(String name) {
    }

    @Override
    public List<String> getItems() {
        return Lists.newArrayList(MythicMobs.inst().getItemManager().getItemNames());
    }
}
