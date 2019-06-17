package ink.ptms.cronus.database.data.item;

import com.google.common.collect.Lists;
import ink.ptms.cronus.Cronus;
import me.skymc.taboolib.playerdata.DataUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-05-28 23:42
 */
public class ItemStorageCronus extends ItemStorage {

    private FileConfiguration items = DataUtils.addPluginData("data/items.yml", Cronus.getInst());

    @Override
    public ItemStack getItem(String name) {
        return items.getItemStack(name);
    }

    @Override
    public void addItem(String name, ItemStack itemStack) {
        items.set(name, itemStack);
    }

    @Override
    public void delItem(String name) {
        addItem(name, null);
    }

    @Override
    public List<String> getItems() {
        return Lists.newArrayList(items.getKeys(false));
    }
}
