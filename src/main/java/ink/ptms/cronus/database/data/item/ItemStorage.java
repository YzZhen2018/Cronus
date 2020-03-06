package ink.ptms.cronus.database.data.item;

import ink.ptms.cronus.service.Service;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-05-28 23:31
 */
public abstract class ItemStorage implements Service {

    public String depend() {
        return "Cronus";
    }

    abstract public ItemStack getItem(String name);

    abstract public void addItem(String name, ItemStack itemStack);

    abstract public void delItem(String name);

    abstract public List<String> getItems();

}
