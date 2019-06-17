package ink.ptms.cronus.asm;

import ink.ptms.cronus.Cronus;
import me.skymc.taboolib.common.function.TFunction;
import me.skymc.taboolib.common.versioncontrol.SimpleVersionControl;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 坏黑
 * @Since 2019-05-30 22:25
 */
@TFunction(enable = "init")
public abstract class AsmHandler {

    private static AsmHandler impl;

    public static AsmHandler getImpl() {
        return impl;
    }

    static void init() {
        try {
            impl = (AsmHandler) SimpleVersionControl.createNMS("ink.ptms.cronus.asm.AsmHandlerImpl").useCache().translate(Cronus.getInst()).newInstance();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    abstract public Entity getEntityByEntityId(int id);

    abstract public ItemStack[] getRecipe(Inventory inventory);

}
