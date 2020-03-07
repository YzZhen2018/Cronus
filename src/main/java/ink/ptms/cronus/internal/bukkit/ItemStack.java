package ink.ptms.cronus.internal.bukkit;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ptms.cronus.internal.bukkit.parser.CustomParser;
import ink.ptms.cronus.util.UtilsKt;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.item.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-05-23 22:45
 */
public class ItemStack {

    private List<String> type;
    private String name;
    private String lore;
    private int damage;
    private int amount;
    private org.bukkit.inventory.ItemStack bukkitItem;
    private Map<CustomParser, String> custom;

    public ItemStack(org.bukkit.inventory.ItemStack bukkitItem) {
        this.bukkitItem = bukkitItem;
        this.amount = bukkitItem.getAmount();
    }

    public ItemStack(String type, String name, String lore, int damage, int amount) {
        this.type = type == null ? null : Lists.newArrayList(type.split("\\|"));
        this.name = name;
        this.lore = lore;
        this.damage = damage;
        this.amount = amount;
        this.custom = Maps.newHashMap();
    }

    public ItemStack(String type, String name, String lore, int damage, int amount, Map<CustomParser, String> custom) {
        this.type = type == null ? null : Lists.newArrayList(type.split("\\|"));
        this.name = name;
        this.lore = lore;
        this.damage = damage;
        this.amount = amount;
        this.custom = custom;
    }

    public org.bukkit.inventory.ItemStack toBukkitItem() {
        ItemBuilder builder = new ItemBuilder(Items.asMaterial(type.get(0)), amount, damage);
        if (name != null) {
            builder.name(name);
        }
        if (lore != null) {
            builder.lore(lore.split("\\\\n"));
        }
        return builder.colored().build();
    }

    public boolean isType(org.bukkit.inventory.ItemStack itemStack) {
        return type == null || UtilsKt.INSTANCE.any(type, t -> itemStack.getType().name().equalsIgnoreCase(t));
    }

    public boolean isName(org.bukkit.inventory.ItemStack itemStack) {
        return name == null || Items.getName(itemStack).contains(name);
    }

    public boolean isLore(org.bukkit.inventory.ItemStack itemStack) {
        return lore == null || Items.hasLore(itemStack, lore);
    }

    public boolean isDamage(org.bukkit.inventory.ItemStack itemStack) {
        return damage == -1 || itemStack.getDurability() == damage;
    }

    public boolean isAmount(org.bukkit.inventory.ItemStack itemStack) {
        return itemStack.getAmount() >= amount;
    }

    public boolean isCustom(org.bukkit.inventory.ItemStack itemStack) {
        return custom.isEmpty() || custom.entrySet().stream().allMatch(entry -> entry.getKey().isItem(itemStack, entry.getValue()));
    }

    public boolean isItem(org.bukkit.inventory.ItemStack itemStack) {
        return bukkitItem == null ? isType(itemStack) && isName(itemStack) && isLore(itemStack) && isDamage(itemStack) && isAmount(itemStack) && isCustom(itemStack) : bukkitItem.isSimilar(itemStack) && bukkitItem.getAmount() <= itemStack.getAmount();
    }

    public boolean hasItem(Player player) {
        int checkAmount = amount;
        for (org.bukkit.inventory.ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && !itemStack.getType().equals(Material.AIR) && isItem(itemStack)) {
                checkAmount -= itemStack.getAmount();
                if (checkAmount <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean takeItem(Player player) {
        int takeAmount = amount;
        org.bukkit.inventory.ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            org.bukkit.inventory.ItemStack itemStack = contents[i];
            if (itemStack != null && !itemStack.getType().equals(Material.AIR) && isItem(itemStack)) {
                takeAmount -= itemStack.getAmount();
                if (takeAmount < 0) {
                    itemStack.setAmount(itemStack.getAmount() - (takeAmount + itemStack.getAmount()));
                    return true;
                } else {
                    player.getInventory().setItem(i, null);
                    if (takeAmount == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<String> getType() {
        return Lists.newArrayList(type);
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getLore() {
        return lore;
    }

    public int getAmount() {
        return amount;
    }

    public int getDamage() {
        return damage;
    }

    public org.bukkit.inventory.ItemStack getBukkitItem() {
        return bukkitItem;
    }

    public Map<CustomParser, String> getCustom() {
        return custom;
    }

    public String asString() {
        if (!Items.isNull(bukkitItem)) {
            return Items.getName(bukkitItem);
        }
        StringBuilder builder = new StringBuilder();
        if (type != null) {
            builder.append(type);
        }
        if (damage != -1) {
            builder.append(":").append(damage);
        }
        if (name != null) {
            builder.append(",").append("n=").append(name);
        }
        if (lore != null) {
            builder.append(",").append("l=").append(lore);
        }
        if (amount > 1) {
            builder.append(",").append("a=").append(amount);
        }
        custom.forEach((k, v) -> {
            builder.append(",").append(k.getPrefix()).append("=").append(v);
        });
        return builder.toString();
    }

    @Override
    public String toString() {
        return "ItemStack{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", lore='" + lore + '\'' +
                ", damage=" + damage +
                ", amount=" + amount +
                ", bukkitItem=" + bukkitItem +
                ", custom=" + custom +
                '}';
    }
}
