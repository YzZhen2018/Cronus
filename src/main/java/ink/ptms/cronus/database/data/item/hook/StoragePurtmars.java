package ink.ptms.cronus.database.data.item.hook;

import ink.ptms.cronus.database.data.item.ItemStorage;
import me.skymc.purtmars.item.PurtmarsItem;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-05-28 23:38
 */
public class StoragePurtmars extends ItemStorage {

    @Override
    public ItemStack getItem(String name) {
        return PurtmarsItem.getManager().getItem(name);
    }

    @Override
    public void addItem(String name, ItemStack itemStack) {
    }

    @Override
    public void delItem(String name) {
    }

    @Override
    public List<String> getItems() {
        return PurtmarsItem.getManager().getItems();
    }
}
