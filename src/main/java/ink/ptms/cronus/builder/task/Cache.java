package ink.ptms.cronus.builder.task;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ptms.cronus.internal.version.MaterialControl;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author 坏黑
 * @Since 2019-06-22 22:12
 */
public class Cache {

    public static final List<ItemStack> BLOCKS = Lists.newArrayList();
    public static final List<String> NOT_ITEM = Lists.newArrayList();
    public static final Map<Material, Integer> DAMAGEABLE = Maps.newHashMap();

    static {
        // 非物品
        NOT_ITEM.add("WATER");
        NOT_ITEM.add("LAVA");
        NOT_ITEM.add("PISTON_EXTENSION");
        NOT_ITEM.add("PISTON_MOVING_PIECE");
        NOT_ITEM.add("WALL_SIGN");
        NOT_ITEM.add("BREWING_STAND");
        NOT_ITEM.add("CAULDRON");
        NOT_ITEM.add("WALL_BANNER");
        NOT_ITEM.add("SPRUCE_DOOR");
        NOT_ITEM.add("BIRCH_DOOR");
        NOT_ITEM.add("JUNGLE_DOOR");
        NOT_ITEM.add("ACACIA_DOOR");
        NOT_ITEM.add("DARK_OAK_DOOR");
        NOT_ITEM.add("FROSTED_ICE");
        // 获取方块
        if (MaterialControl.isNewVersion()) {
            Arrays.stream(MaterialControl.values()).map(material -> {
                try {
                    ItemStack parseItem = material.parseItem();
                    if (parseItem != null && parseItem.getType().isBlock() && !parseItem.getType().isTransparent() && !NOT_ITEM.contains(material.name())) {
                        return parseItem;
                    }
                } catch (Throwable ignored) {
                }
                return null;
            }).forEach(BLOCKS::add);
        } else {
            // 附加值特例
            DAMAGEABLE.put(Material.STONE, 6);
            DAMAGEABLE.put(Material.DIRT, 2);
            DAMAGEABLE.put(Material.WOOD, 5);
            DAMAGEABLE.put(Material.SAND, 1);
            DAMAGEABLE.put(Material.LOG, 3);
            DAMAGEABLE.put(Material.SPONGE, 1);
            DAMAGEABLE.put(Material.SANDSTONE, 2);
            DAMAGEABLE.put(Material.WOOL, 15);
            DAMAGEABLE.put(Material.STEP, 7);
            DAMAGEABLE.put(Material.STAINED_GLASS, 15);
            DAMAGEABLE.put(Material.SMOOTH_BRICK, 3);
            DAMAGEABLE.put(Material.WOOD_STEP, 5);
            DAMAGEABLE.put(Material.QUARTZ_BLOCK, 2);
            DAMAGEABLE.put(Material.STAINED_CLAY, 15);
            DAMAGEABLE.put(Material.LOG_2, 2);
            DAMAGEABLE.put(Material.PRISMARINE, 2);
            DAMAGEABLE.put(Material.RED_SANDSTONE, 2);
            DAMAGEABLE.put(Material.SAPLING, 5);
            DAMAGEABLE.put(Material.LEAVES, 3);
            DAMAGEABLE.put(Material.LONG_GRASS, 2);
            DAMAGEABLE.put(Material.RED_ROSE, 8);
            DAMAGEABLE.put(Material.MONSTER_EGGS, 5);
            DAMAGEABLE.put(Material.COBBLE_WALL, 1);
            DAMAGEABLE.put(Material.ANVIL, 2);
            DAMAGEABLE.put(Material.STAINED_GLASS_PANE, 15);
            DAMAGEABLE.put(Material.LEAVES_2, 1);
            DAMAGEABLE.put(Material.CARPET, 15);
            DAMAGEABLE.put(Material.DOUBLE_PLANT, 5);
            DAMAGEABLE.put(Material.BED, 15);
            DAMAGEABLE.put(Material.SKULL, 5);
            DAMAGEABLE.put(Material.BANNER, 15);
            // 1.12
            try {
                DAMAGEABLE.put(Material.CONCRETE, 15);
                DAMAGEABLE.put(Material.CONCRETE_POWDER, 15);
            } catch (Throwable ignored) {
            }
            // 获取物品
            for (Material material : Material.values()) {
                if (material.isBlock() && !material.isTransparent() && MaterialControl.fromMaterial(material) != null && !NOT_ITEM.contains(material.name())) {
                    try {
                        if (DAMAGEABLE.containsKey(material)) {
                            int damage = DAMAGEABLE.get(material);
                            for (int i = 0; i < damage; i++) {
                                // 木半砖特例
                                if (material == Material.STEP && i == 2) {
                                    continue;
                                }
                                BLOCKS.add(new ItemStack(material, 1, (short) i));
                            }
                        } else {
                            BLOCKS.add(new ItemStack(material));
                        }
                    } catch (Throwable ignored) {
                    }
                }
            }
        }
    }

}
