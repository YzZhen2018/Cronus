package ink.ptms.cronus.internal.bukkit.parser;

import org.bukkit.inventory.ItemStack;

/**
 * @Author sky
 * @Since 2020-03-04 23:16
 */
public abstract class CustomParser {

    abstract public boolean isItem(ItemStack itemStack, String value);

    abstract public String getPrefix();
}
